package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatOffensiveMessage;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.domain.ChatMessage;
import com.spingular.chat.repository.ChatOffensiveMessageRepository;
import com.spingular.chat.service.ChatOffensiveMessageService;
import com.spingular.chat.service.dto.ChatOffensiveMessageDTO;
import com.spingular.chat.service.mapper.ChatOffensiveMessageMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatOffensiveMessageCriteria;
import com.spingular.chat.service.ChatOffensiveMessageQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.spingular.chat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChatOffensiveMessageResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatOffensiveMessageResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_IS_OFFENSIVE = false;
    private static final Boolean UPDATED_IS_OFFENSIVE = true;

    @Autowired
    private ChatOffensiveMessageRepository chatOffensiveMessageRepository;

    @Autowired
    private ChatOffensiveMessageMapper chatOffensiveMessageMapper;

    @Autowired
    private ChatOffensiveMessageService chatOffensiveMessageService;

    @Autowired
    private ChatOffensiveMessageQueryService chatOffensiveMessageQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restChatOffensiveMessageMockMvc;

    private ChatOffensiveMessage chatOffensiveMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatOffensiveMessageResource chatOffensiveMessageResource = new ChatOffensiveMessageResource(chatOffensiveMessageService, chatOffensiveMessageQueryService);
        this.restChatOffensiveMessageMockMvc = MockMvcBuilders.standaloneSetup(chatOffensiveMessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatOffensiveMessage createEntity(EntityManager em) {
        ChatOffensiveMessage chatOffensiveMessage = new ChatOffensiveMessage()
            .creationDate(DEFAULT_CREATION_DATE)
            .isOffensive(DEFAULT_IS_OFFENSIVE);
        return chatOffensiveMessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatOffensiveMessage createUpdatedEntity(EntityManager em) {
        ChatOffensiveMessage chatOffensiveMessage = new ChatOffensiveMessage()
            .creationDate(UPDATED_CREATION_DATE)
            .isOffensive(UPDATED_IS_OFFENSIVE);
        return chatOffensiveMessage;
    }

    @BeforeEach
    public void initTest() {
        chatOffensiveMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatOffensiveMessage() throws Exception {
        int databaseSizeBeforeCreate = chatOffensiveMessageRepository.findAll().size();

        // Create the ChatOffensiveMessage
        ChatOffensiveMessageDTO chatOffensiveMessageDTO = chatOffensiveMessageMapper.toDto(chatOffensiveMessage);
        restChatOffensiveMessageMockMvc.perform(post("/api/chat-offensive-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatOffensiveMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatOffensiveMessage in the database
        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ChatOffensiveMessage testChatOffensiveMessage = chatOffensiveMessageList.get(chatOffensiveMessageList.size() - 1);
        assertThat(testChatOffensiveMessage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatOffensiveMessage.isIsOffensive()).isEqualTo(DEFAULT_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    public void createChatOffensiveMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatOffensiveMessageRepository.findAll().size();

        // Create the ChatOffensiveMessage with an existing ID
        chatOffensiveMessage.setId(1L);
        ChatOffensiveMessageDTO chatOffensiveMessageDTO = chatOffensiveMessageMapper.toDto(chatOffensiveMessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatOffensiveMessageMockMvc.perform(post("/api/chat-offensive-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatOffensiveMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatOffensiveMessage in the database
        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatOffensiveMessageRepository.findAll().size();
        // set the field null
        chatOffensiveMessage.setCreationDate(null);

        // Create the ChatOffensiveMessage, which fails.
        ChatOffensiveMessageDTO chatOffensiveMessageDTO = chatOffensiveMessageMapper.toDto(chatOffensiveMessage);

        restChatOffensiveMessageMockMvc.perform(post("/api/chat-offensive-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatOffensiveMessageDTO)))
            .andExpect(status().isBadRequest());

        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessages() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatOffensiveMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOffensive").value(hasItem(DEFAULT_IS_OFFENSIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getChatOffensiveMessage() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get the chatOffensiveMessage
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages/{id}", chatOffensiveMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatOffensiveMessage.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.isOffensive").value(DEFAULT_IS_OFFENSIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatOffensiveMessageShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatOffensiveMessageList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatOffensiveMessageShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatOffensiveMessageShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatOffensiveMessageList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatOffensiveMessageShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where creationDate is not null
        defaultChatOffensiveMessageShouldBeFound("creationDate.specified=true");

        // Get all the chatOffensiveMessageList where creationDate is null
        defaultChatOffensiveMessageShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByIsOffensiveIsEqualToSomething() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where isOffensive equals to DEFAULT_IS_OFFENSIVE
        defaultChatOffensiveMessageShouldBeFound("isOffensive.equals=" + DEFAULT_IS_OFFENSIVE);

        // Get all the chatOffensiveMessageList where isOffensive equals to UPDATED_IS_OFFENSIVE
        defaultChatOffensiveMessageShouldNotBeFound("isOffensive.equals=" + UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByIsOffensiveIsInShouldWork() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where isOffensive in DEFAULT_IS_OFFENSIVE or UPDATED_IS_OFFENSIVE
        defaultChatOffensiveMessageShouldBeFound("isOffensive.in=" + DEFAULT_IS_OFFENSIVE + "," + UPDATED_IS_OFFENSIVE);

        // Get all the chatOffensiveMessageList where isOffensive equals to UPDATED_IS_OFFENSIVE
        defaultChatOffensiveMessageShouldNotBeFound("isOffensive.in=" + UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByIsOffensiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        // Get all the chatOffensiveMessageList where isOffensive is not null
        defaultChatOffensiveMessageShouldBeFound("isOffensive.specified=true");

        // Get all the chatOffensiveMessageList where isOffensive is null
        defaultChatOffensiveMessageShouldNotBeFound("isOffensive.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByChatUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);
        ChatUser chatUser = ChatUserResourceIT.createEntity(em);
        em.persist(chatUser);
        em.flush();
        chatOffensiveMessage.setChatUser(chatUser);
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);
        Long chatUserId = chatUser.getId();

        // Get all the chatOffensiveMessageList where chatUser equals to chatUserId
        defaultChatOffensiveMessageShouldBeFound("chatUserId.equals=" + chatUserId);

        // Get all the chatOffensiveMessageList where chatUser equals to chatUserId + 1
        defaultChatOffensiveMessageShouldNotBeFound("chatUserId.equals=" + (chatUserId + 1));
    }


    @Test
    @Transactional
    public void getAllChatOffensiveMessagesByChatMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);
        ChatMessage chatMessage = ChatMessageResourceIT.createEntity(em);
        em.persist(chatMessage);
        em.flush();
        chatOffensiveMessage.setChatMessage(chatMessage);
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);
        Long chatMessageId = chatMessage.getId();

        // Get all the chatOffensiveMessageList where chatMessage equals to chatMessageId
        defaultChatOffensiveMessageShouldBeFound("chatMessageId.equals=" + chatMessageId);

        // Get all the chatOffensiveMessageList where chatMessage equals to chatMessageId + 1
        defaultChatOffensiveMessageShouldNotBeFound("chatMessageId.equals=" + (chatMessageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatOffensiveMessageShouldBeFound(String filter) throws Exception {
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatOffensiveMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOffensive").value(hasItem(DEFAULT_IS_OFFENSIVE.booleanValue())));

        // Check, that the count call also returns 1
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatOffensiveMessageShouldNotBeFound(String filter) throws Exception {
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatOffensiveMessage() throws Exception {
        // Get the chatOffensiveMessage
        restChatOffensiveMessageMockMvc.perform(get("/api/chat-offensive-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatOffensiveMessage() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        int databaseSizeBeforeUpdate = chatOffensiveMessageRepository.findAll().size();

        // Update the chatOffensiveMessage
        ChatOffensiveMessage updatedChatOffensiveMessage = chatOffensiveMessageRepository.findById(chatOffensiveMessage.getId()).get();
        // Disconnect from session so that the updates on updatedChatOffensiveMessage are not directly saved in db
        em.detach(updatedChatOffensiveMessage);
        updatedChatOffensiveMessage
            .creationDate(UPDATED_CREATION_DATE)
            .isOffensive(UPDATED_IS_OFFENSIVE);
        ChatOffensiveMessageDTO chatOffensiveMessageDTO = chatOffensiveMessageMapper.toDto(updatedChatOffensiveMessage);

        restChatOffensiveMessageMockMvc.perform(put("/api/chat-offensive-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatOffensiveMessageDTO)))
            .andExpect(status().isOk());

        // Validate the ChatOffensiveMessage in the database
        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeUpdate);
        ChatOffensiveMessage testChatOffensiveMessage = chatOffensiveMessageList.get(chatOffensiveMessageList.size() - 1);
        assertThat(testChatOffensiveMessage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatOffensiveMessage.isIsOffensive()).isEqualTo(UPDATED_IS_OFFENSIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatOffensiveMessage() throws Exception {
        int databaseSizeBeforeUpdate = chatOffensiveMessageRepository.findAll().size();

        // Create the ChatOffensiveMessage
        ChatOffensiveMessageDTO chatOffensiveMessageDTO = chatOffensiveMessageMapper.toDto(chatOffensiveMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatOffensiveMessageMockMvc.perform(put("/api/chat-offensive-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatOffensiveMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatOffensiveMessage in the database
        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatOffensiveMessage() throws Exception {
        // Initialize the database
        chatOffensiveMessageRepository.saveAndFlush(chatOffensiveMessage);

        int databaseSizeBeforeDelete = chatOffensiveMessageRepository.findAll().size();

        // Delete the chatOffensiveMessage
        restChatOffensiveMessageMockMvc.perform(delete("/api/chat-offensive-messages/{id}", chatOffensiveMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatOffensiveMessage> chatOffensiveMessageList = chatOffensiveMessageRepository.findAll();
        assertThat(chatOffensiveMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatOffensiveMessage.class);
        ChatOffensiveMessage chatOffensiveMessage1 = new ChatOffensiveMessage();
        chatOffensiveMessage1.setId(1L);
        ChatOffensiveMessage chatOffensiveMessage2 = new ChatOffensiveMessage();
        chatOffensiveMessage2.setId(chatOffensiveMessage1.getId());
        assertThat(chatOffensiveMessage1).isEqualTo(chatOffensiveMessage2);
        chatOffensiveMessage2.setId(2L);
        assertThat(chatOffensiveMessage1).isNotEqualTo(chatOffensiveMessage2);
        chatOffensiveMessage1.setId(null);
        assertThat(chatOffensiveMessage1).isNotEqualTo(chatOffensiveMessage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatOffensiveMessageDTO.class);
        ChatOffensiveMessageDTO chatOffensiveMessageDTO1 = new ChatOffensiveMessageDTO();
        chatOffensiveMessageDTO1.setId(1L);
        ChatOffensiveMessageDTO chatOffensiveMessageDTO2 = new ChatOffensiveMessageDTO();
        assertThat(chatOffensiveMessageDTO1).isNotEqualTo(chatOffensiveMessageDTO2);
        chatOffensiveMessageDTO2.setId(chatOffensiveMessageDTO1.getId());
        assertThat(chatOffensiveMessageDTO1).isEqualTo(chatOffensiveMessageDTO2);
        chatOffensiveMessageDTO2.setId(2L);
        assertThat(chatOffensiveMessageDTO1).isNotEqualTo(chatOffensiveMessageDTO2);
        chatOffensiveMessageDTO1.setId(null);
        assertThat(chatOffensiveMessageDTO1).isNotEqualTo(chatOffensiveMessageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatOffensiveMessageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatOffensiveMessageMapper.fromId(null)).isNull();
    }
}
