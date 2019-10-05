package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatMessage;
import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.domain.ChatOffensiveMessage;
import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.repository.ChatMessageRepository;
import com.spingular.chat.service.ChatMessageService;
import com.spingular.chat.service.dto.ChatMessageDTO;
import com.spingular.chat.service.mapper.ChatMessageMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatMessageCriteria;
import com.spingular.chat.service.ChatMessageQueryService;

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
import java.util.List;

import static com.spingular.chat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChatMessageResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatMessageResourceIT {

    private static final String DEFAULT_MESSAGE_SENT_AT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_SENT_AT = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RECEIVED = false;
    private static final Boolean UPDATED_IS_RECEIVED = true;

    private static final Boolean DEFAULT_IS_DELIVERED = false;
    private static final Boolean UPDATED_IS_DELIVERED = true;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageQueryService chatMessageQueryService;

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

    private MockMvc restChatMessageMockMvc;

    private ChatMessage chatMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatMessageResource chatMessageResource = new ChatMessageResource(chatMessageService, chatMessageQueryService);
        this.restChatMessageMockMvc = MockMvcBuilders.standaloneSetup(chatMessageResource)
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
    public static ChatMessage createEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .messageSentAt(DEFAULT_MESSAGE_SENT_AT)
            .message(DEFAULT_MESSAGE)
            .isReceived(DEFAULT_IS_RECEIVED)
            .isDelivered(DEFAULT_IS_DELIVERED);
        return chatMessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatMessage createUpdatedEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .messageSentAt(UPDATED_MESSAGE_SENT_AT)
            .message(UPDATED_MESSAGE)
            .isReceived(UPDATED_IS_RECEIVED)
            .isDelivered(UPDATED_IS_DELIVERED);
        return chatMessage;
    }

    @BeforeEach
    public void initTest() {
        chatMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatMessage() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getMessageSentAt()).isEqualTo(DEFAULT_MESSAGE_SENT_AT);
        assertThat(testChatMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testChatMessage.isIsReceived()).isEqualTo(DEFAULT_IS_RECEIVED);
        assertThat(testChatMessage.isIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void createChatMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage with an existing ID
        chatMessage.setId(1L);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMessageSentAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatMessageRepository.findAll().size();
        // set the field null
        chatMessage.setMessageSentAt(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatMessageRepository.findAll().size();
        // set the field null
        chatMessage.setMessage(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatMessages() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList
        restChatMessageMockMvc.perform(get("/api/chat-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageSentAt").value(hasItem(DEFAULT_MESSAGE_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].isReceived").value(hasItem(DEFAULT_IS_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", chatMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatMessage.getId().intValue()))
            .andExpect(jsonPath("$.messageSentAt").value(DEFAULT_MESSAGE_SENT_AT.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.isReceived").value(DEFAULT_IS_RECEIVED.booleanValue()))
            .andExpect(jsonPath("$.isDelivered").value(DEFAULT_IS_DELIVERED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageSentAtIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where messageSentAt equals to DEFAULT_MESSAGE_SENT_AT
        defaultChatMessageShouldBeFound("messageSentAt.equals=" + DEFAULT_MESSAGE_SENT_AT);

        // Get all the chatMessageList where messageSentAt equals to UPDATED_MESSAGE_SENT_AT
        defaultChatMessageShouldNotBeFound("messageSentAt.equals=" + UPDATED_MESSAGE_SENT_AT);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageSentAtIsInShouldWork() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where messageSentAt in DEFAULT_MESSAGE_SENT_AT or UPDATED_MESSAGE_SENT_AT
        defaultChatMessageShouldBeFound("messageSentAt.in=" + DEFAULT_MESSAGE_SENT_AT + "," + UPDATED_MESSAGE_SENT_AT);

        // Get all the chatMessageList where messageSentAt equals to UPDATED_MESSAGE_SENT_AT
        defaultChatMessageShouldNotBeFound("messageSentAt.in=" + UPDATED_MESSAGE_SENT_AT);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageSentAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where messageSentAt is not null
        defaultChatMessageShouldBeFound("messageSentAt.specified=true");

        // Get all the chatMessageList where messageSentAt is null
        defaultChatMessageShouldNotBeFound("messageSentAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where message equals to DEFAULT_MESSAGE
        defaultChatMessageShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the chatMessageList where message equals to UPDATED_MESSAGE
        defaultChatMessageShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultChatMessageShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the chatMessageList where message equals to UPDATED_MESSAGE
        defaultChatMessageShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where message is not null
        defaultChatMessageShouldBeFound("message.specified=true");

        // Get all the chatMessageList where message is null
        defaultChatMessageShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isReceived equals to DEFAULT_IS_RECEIVED
        defaultChatMessageShouldBeFound("isReceived.equals=" + DEFAULT_IS_RECEIVED);

        // Get all the chatMessageList where isReceived equals to UPDATED_IS_RECEIVED
        defaultChatMessageShouldNotBeFound("isReceived.equals=" + UPDATED_IS_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isReceived in DEFAULT_IS_RECEIVED or UPDATED_IS_RECEIVED
        defaultChatMessageShouldBeFound("isReceived.in=" + DEFAULT_IS_RECEIVED + "," + UPDATED_IS_RECEIVED);

        // Get all the chatMessageList where isReceived equals to UPDATED_IS_RECEIVED
        defaultChatMessageShouldNotBeFound("isReceived.in=" + UPDATED_IS_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isReceived is not null
        defaultChatMessageShouldBeFound("isReceived.specified=true");

        // Get all the chatMessageList where isReceived is null
        defaultChatMessageShouldNotBeFound("isReceived.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsDeliveredIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isDelivered equals to DEFAULT_IS_DELIVERED
        defaultChatMessageShouldBeFound("isDelivered.equals=" + DEFAULT_IS_DELIVERED);

        // Get all the chatMessageList where isDelivered equals to UPDATED_IS_DELIVERED
        defaultChatMessageShouldNotBeFound("isDelivered.equals=" + UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsDeliveredIsInShouldWork() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isDelivered in DEFAULT_IS_DELIVERED or UPDATED_IS_DELIVERED
        defaultChatMessageShouldBeFound("isDelivered.in=" + DEFAULT_IS_DELIVERED + "," + UPDATED_IS_DELIVERED);

        // Get all the chatMessageList where isDelivered equals to UPDATED_IS_DELIVERED
        defaultChatMessageShouldNotBeFound("isDelivered.in=" + UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllChatMessagesByIsDeliveredIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList where isDelivered is not null
        defaultChatMessageShouldBeFound("isDelivered.specified=true");

        // Get all the chatMessageList where isDelivered is null
        defaultChatMessageShouldNotBeFound("isDelivered.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatMessagesByChatNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        ChatNotification chatNotification = ChatNotificationResourceIT.createEntity(em);
        em.persist(chatNotification);
        em.flush();
        chatMessage.addChatNotification(chatNotification);
        chatMessageRepository.saveAndFlush(chatMessage);
        Long chatNotificationId = chatNotification.getId();

        // Get all the chatMessageList where chatNotification equals to chatNotificationId
        defaultChatMessageShouldBeFound("chatNotificationId.equals=" + chatNotificationId);

        // Get all the chatMessageList where chatNotification equals to chatNotificationId + 1
        defaultChatMessageShouldNotBeFound("chatNotificationId.equals=" + (chatNotificationId + 1));
    }


    @Test
    @Transactional
    public void getAllChatMessagesByChatOffensiveMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        ChatOffensiveMessage chatOffensiveMessage = ChatOffensiveMessageResourceIT.createEntity(em);
        em.persist(chatOffensiveMessage);
        em.flush();
        chatMessage.addChatOffensiveMessage(chatOffensiveMessage);
        chatMessageRepository.saveAndFlush(chatMessage);
        Long chatOffensiveMessageId = chatOffensiveMessage.getId();

        // Get all the chatMessageList where chatOffensiveMessage equals to chatOffensiveMessageId
        defaultChatMessageShouldBeFound("chatOffensiveMessageId.equals=" + chatOffensiveMessageId);

        // Get all the chatMessageList where chatOffensiveMessage equals to chatOffensiveMessageId + 1
        defaultChatMessageShouldNotBeFound("chatOffensiveMessageId.equals=" + (chatOffensiveMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllChatMessagesByChatRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        ChatRoom chatRoom = ChatRoomResourceIT.createEntity(em);
        em.persist(chatRoom);
        em.flush();
        chatMessage.setChatRoom(chatRoom);
        chatMessageRepository.saveAndFlush(chatMessage);
        Long chatRoomId = chatRoom.getId();

        // Get all the chatMessageList where chatRoom equals to chatRoomId
        defaultChatMessageShouldBeFound("chatRoomId.equals=" + chatRoomId);

        // Get all the chatMessageList where chatRoom equals to chatRoomId + 1
        defaultChatMessageShouldNotBeFound("chatRoomId.equals=" + (chatRoomId + 1));
    }


    @Test
    @Transactional
    public void getAllChatMessagesByChatUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        ChatUser chatUser = ChatUserResourceIT.createEntity(em);
        em.persist(chatUser);
        em.flush();
        chatMessage.setChatUser(chatUser);
        chatMessageRepository.saveAndFlush(chatMessage);
        Long chatUserId = chatUser.getId();

        // Get all the chatMessageList where chatUser equals to chatUserId
        defaultChatMessageShouldBeFound("chatUserId.equals=" + chatUserId);

        // Get all the chatMessageList where chatUser equals to chatUserId + 1
        defaultChatMessageShouldNotBeFound("chatUserId.equals=" + (chatUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatMessageShouldBeFound(String filter) throws Exception {
        restChatMessageMockMvc.perform(get("/api/chat-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageSentAt").value(hasItem(DEFAULT_MESSAGE_SENT_AT)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].isReceived").value(hasItem(DEFAULT_IS_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));

        // Check, that the count call also returns 1
        restChatMessageMockMvc.perform(get("/api/chat-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatMessageShouldNotBeFound(String filter) throws Exception {
        restChatMessageMockMvc.perform(get("/api/chat-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatMessageMockMvc.perform(get("/api/chat-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatMessage() throws Exception {
        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Update the chatMessage
        ChatMessage updatedChatMessage = chatMessageRepository.findById(chatMessage.getId()).get();
        // Disconnect from session so that the updates on updatedChatMessage are not directly saved in db
        em.detach(updatedChatMessage);
        updatedChatMessage
            .messageSentAt(UPDATED_MESSAGE_SENT_AT)
            .message(UPDATED_MESSAGE)
            .isReceived(UPDATED_IS_RECEIVED)
            .isDelivered(UPDATED_IS_DELIVERED);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(updatedChatMessage);

        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getMessageSentAt()).isEqualTo(UPDATED_MESSAGE_SENT_AT);
        assertThat(testChatMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testChatMessage.isIsReceived()).isEqualTo(UPDATED_IS_RECEIVED);
        assertThat(testChatMessage.isIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void updateNonExistingChatMessage() throws Exception {
        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        int databaseSizeBeforeDelete = chatMessageRepository.findAll().size();

        // Delete the chatMessage
        restChatMessageMockMvc.perform(delete("/api/chat-messages/{id}", chatMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setId(1L);
        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);
        chatMessage2.setId(2L);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
        chatMessage1.setId(null);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessageDTO.class);
        ChatMessageDTO chatMessageDTO1 = new ChatMessageDTO();
        chatMessageDTO1.setId(1L);
        ChatMessageDTO chatMessageDTO2 = new ChatMessageDTO();
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(chatMessageDTO1.getId());
        assertThat(chatMessageDTO1).isEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(2L);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO1.setId(null);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatMessageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatMessageMapper.fromId(null)).isNull();
    }
}
