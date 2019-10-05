package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatInvitation;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.repository.ChatInvitationRepository;
import com.spingular.chat.service.ChatInvitationService;
import com.spingular.chat.service.dto.ChatInvitationDTO;
import com.spingular.chat.service.mapper.ChatInvitationMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatInvitationCriteria;
import com.spingular.chat.service.ChatInvitationQueryService;

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
 * Integration tests for the {@link ChatInvitationResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatInvitationResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_ACCEPTANCE = false;
    private static final Boolean UPDATED_ACCEPTANCE = true;

    private static final Boolean DEFAULT_DENIAL = false;
    private static final Boolean UPDATED_DENIAL = true;

    private static final Instant DEFAULT_ACCEPTANCE_DENIAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTANCE_DENIAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_ACCEPTANCE_DENIAL_DATE = Instant.ofEpochMilli(-1L);

    @Autowired
    private ChatInvitationRepository chatInvitationRepository;

    @Autowired
    private ChatInvitationMapper chatInvitationMapper;

    @Autowired
    private ChatInvitationService chatInvitationService;

    @Autowired
    private ChatInvitationQueryService chatInvitationQueryService;

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

    private MockMvc restChatInvitationMockMvc;

    private ChatInvitation chatInvitation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatInvitationResource chatInvitationResource = new ChatInvitationResource(chatInvitationService, chatInvitationQueryService);
        this.restChatInvitationMockMvc = MockMvcBuilders.standaloneSetup(chatInvitationResource)
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
    public static ChatInvitation createEntity(EntityManager em) {
        ChatInvitation chatInvitation = new ChatInvitation()
            .creationDate(DEFAULT_CREATION_DATE)
            .acceptance(DEFAULT_ACCEPTANCE)
            .denial(DEFAULT_DENIAL)
            .acceptanceDenialDate(DEFAULT_ACCEPTANCE_DENIAL_DATE);
        return chatInvitation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatInvitation createUpdatedEntity(EntityManager em) {
        ChatInvitation chatInvitation = new ChatInvitation()
            .creationDate(UPDATED_CREATION_DATE)
            .acceptance(UPDATED_ACCEPTANCE)
            .denial(UPDATED_DENIAL)
            .acceptanceDenialDate(UPDATED_ACCEPTANCE_DENIAL_DATE);
        return chatInvitation;
    }

    @BeforeEach
    public void initTest() {
        chatInvitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatInvitation() throws Exception {
        int databaseSizeBeforeCreate = chatInvitationRepository.findAll().size();

        // Create the ChatInvitation
        ChatInvitationDTO chatInvitationDTO = chatInvitationMapper.toDto(chatInvitation);
        restChatInvitationMockMvc.perform(post("/api/chat-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatInvitationDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatInvitation in the database
        List<ChatInvitation> chatInvitationList = chatInvitationRepository.findAll();
        assertThat(chatInvitationList).hasSize(databaseSizeBeforeCreate + 1);
        ChatInvitation testChatInvitation = chatInvitationList.get(chatInvitationList.size() - 1);
        assertThat(testChatInvitation.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatInvitation.isAcceptance()).isEqualTo(DEFAULT_ACCEPTANCE);
        assertThat(testChatInvitation.isDenial()).isEqualTo(DEFAULT_DENIAL);
        assertThat(testChatInvitation.getAcceptanceDenialDate()).isEqualTo(DEFAULT_ACCEPTANCE_DENIAL_DATE);
    }

    @Test
    @Transactional
    public void createChatInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatInvitationRepository.findAll().size();

        // Create the ChatInvitation with an existing ID
        chatInvitation.setId(1L);
        ChatInvitationDTO chatInvitationDTO = chatInvitationMapper.toDto(chatInvitation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatInvitationMockMvc.perform(post("/api/chat-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatInvitationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatInvitation in the database
        List<ChatInvitation> chatInvitationList = chatInvitationRepository.findAll();
        assertThat(chatInvitationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllChatInvitations() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList
        restChatInvitationMockMvc.perform(get("/api/chat-invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].acceptance").value(hasItem(DEFAULT_ACCEPTANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].denial").value(hasItem(DEFAULT_DENIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptanceDenialDate").value(hasItem(DEFAULT_ACCEPTANCE_DENIAL_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getChatInvitation() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get the chatInvitation
        restChatInvitationMockMvc.perform(get("/api/chat-invitations/{id}", chatInvitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatInvitation.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.acceptance").value(DEFAULT_ACCEPTANCE.booleanValue()))
            .andExpect(jsonPath("$.denial").value(DEFAULT_DENIAL.booleanValue()))
            .andExpect(jsonPath("$.acceptanceDenialDate").value(DEFAULT_ACCEPTANCE_DENIAL_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatInvitationShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatInvitationList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatInvitationShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatInvitationShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatInvitationList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatInvitationShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where creationDate is not null
        defaultChatInvitationShouldBeFound("creationDate.specified=true");

        // Get all the chatInvitationList where creationDate is null
        defaultChatInvitationShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptance equals to DEFAULT_ACCEPTANCE
        defaultChatInvitationShouldBeFound("acceptance.equals=" + DEFAULT_ACCEPTANCE);

        // Get all the chatInvitationList where acceptance equals to UPDATED_ACCEPTANCE
        defaultChatInvitationShouldNotBeFound("acceptance.equals=" + UPDATED_ACCEPTANCE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceIsInShouldWork() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptance in DEFAULT_ACCEPTANCE or UPDATED_ACCEPTANCE
        defaultChatInvitationShouldBeFound("acceptance.in=" + DEFAULT_ACCEPTANCE + "," + UPDATED_ACCEPTANCE);

        // Get all the chatInvitationList where acceptance equals to UPDATED_ACCEPTANCE
        defaultChatInvitationShouldNotBeFound("acceptance.in=" + UPDATED_ACCEPTANCE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptance is not null
        defaultChatInvitationShouldBeFound("acceptance.specified=true");

        // Get all the chatInvitationList where acceptance is null
        defaultChatInvitationShouldNotBeFound("acceptance.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByDenialIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where denial equals to DEFAULT_DENIAL
        defaultChatInvitationShouldBeFound("denial.equals=" + DEFAULT_DENIAL);

        // Get all the chatInvitationList where denial equals to UPDATED_DENIAL
        defaultChatInvitationShouldNotBeFound("denial.equals=" + UPDATED_DENIAL);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByDenialIsInShouldWork() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where denial in DEFAULT_DENIAL or UPDATED_DENIAL
        defaultChatInvitationShouldBeFound("denial.in=" + DEFAULT_DENIAL + "," + UPDATED_DENIAL);

        // Get all the chatInvitationList where denial equals to UPDATED_DENIAL
        defaultChatInvitationShouldNotBeFound("denial.in=" + UPDATED_DENIAL);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByDenialIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where denial is not null
        defaultChatInvitationShouldBeFound("denial.specified=true");

        // Get all the chatInvitationList where denial is null
        defaultChatInvitationShouldNotBeFound("denial.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceDenialDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptanceDenialDate equals to DEFAULT_ACCEPTANCE_DENIAL_DATE
        defaultChatInvitationShouldBeFound("acceptanceDenialDate.equals=" + DEFAULT_ACCEPTANCE_DENIAL_DATE);

        // Get all the chatInvitationList where acceptanceDenialDate equals to UPDATED_ACCEPTANCE_DENIAL_DATE
        defaultChatInvitationShouldNotBeFound("acceptanceDenialDate.equals=" + UPDATED_ACCEPTANCE_DENIAL_DATE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceDenialDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptanceDenialDate in DEFAULT_ACCEPTANCE_DENIAL_DATE or UPDATED_ACCEPTANCE_DENIAL_DATE
        defaultChatInvitationShouldBeFound("acceptanceDenialDate.in=" + DEFAULT_ACCEPTANCE_DENIAL_DATE + "," + UPDATED_ACCEPTANCE_DENIAL_DATE);

        // Get all the chatInvitationList where acceptanceDenialDate equals to UPDATED_ACCEPTANCE_DENIAL_DATE
        defaultChatInvitationShouldNotBeFound("acceptanceDenialDate.in=" + UPDATED_ACCEPTANCE_DENIAL_DATE);
    }

    @Test
    @Transactional
    public void getAllChatInvitationsByAcceptanceDenialDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        // Get all the chatInvitationList where acceptanceDenialDate is not null
        defaultChatInvitationShouldBeFound("acceptanceDenialDate.specified=true");

        // Get all the chatInvitationList where acceptanceDenialDate is null
        defaultChatInvitationShouldNotBeFound("acceptanceDenialDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatInvitationsBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);
        ChatUser sender = ChatUserResourceIT.createEntity(em);
        em.persist(sender);
        em.flush();
        chatInvitation.setSender(sender);
        chatInvitationRepository.saveAndFlush(chatInvitation);
        Long senderId = sender.getId();

        // Get all the chatInvitationList where sender equals to senderId
        defaultChatInvitationShouldBeFound("senderId.equals=" + senderId);

        // Get all the chatInvitationList where sender equals to senderId + 1
        defaultChatInvitationShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllChatInvitationsByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);
        ChatUser receiver = ChatUserResourceIT.createEntity(em);
        em.persist(receiver);
        em.flush();
        chatInvitation.setReceiver(receiver);
        chatInvitationRepository.saveAndFlush(chatInvitation);
        Long receiverId = receiver.getId();

        // Get all the chatInvitationList where receiver equals to receiverId
        defaultChatInvitationShouldBeFound("receiverId.equals=" + receiverId);

        // Get all the chatInvitationList where receiver equals to receiverId + 1
        defaultChatInvitationShouldNotBeFound("receiverId.equals=" + (receiverId + 1));
    }


    @Test
    @Transactional
    public void getAllChatInvitationsByChatRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);
        ChatRoom chatRoom = ChatRoomResourceIT.createEntity(em);
        em.persist(chatRoom);
        em.flush();
        chatInvitation.setChatRoom(chatRoom);
        chatInvitationRepository.saveAndFlush(chatInvitation);
        Long chatRoomId = chatRoom.getId();

        // Get all the chatInvitationList where chatRoom equals to chatRoomId
        defaultChatInvitationShouldBeFound("chatRoomId.equals=" + chatRoomId);

        // Get all the chatInvitationList where chatRoom equals to chatRoomId + 1
        defaultChatInvitationShouldNotBeFound("chatRoomId.equals=" + (chatRoomId + 1));
    }


    @Test
    @Transactional
    public void getAllChatInvitationsByChatNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);
        ChatNotification chatNotification = ChatNotificationResourceIT.createEntity(em);
        em.persist(chatNotification);
        em.flush();
        chatInvitation.addChatNotification(chatNotification);
        chatInvitationRepository.saveAndFlush(chatInvitation);
        Long chatNotificationId = chatNotification.getId();

        // Get all the chatInvitationList where chatNotification equals to chatNotificationId
        defaultChatInvitationShouldBeFound("chatNotificationId.equals=" + chatNotificationId);

        // Get all the chatInvitationList where chatNotification equals to chatNotificationId + 1
        defaultChatInvitationShouldNotBeFound("chatNotificationId.equals=" + (chatNotificationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatInvitationShouldBeFound(String filter) throws Exception {
        restChatInvitationMockMvc.perform(get("/api/chat-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].acceptance").value(hasItem(DEFAULT_ACCEPTANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].denial").value(hasItem(DEFAULT_DENIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptanceDenialDate").value(hasItem(DEFAULT_ACCEPTANCE_DENIAL_DATE.toString())));

        // Check, that the count call also returns 1
        restChatInvitationMockMvc.perform(get("/api/chat-invitations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatInvitationShouldNotBeFound(String filter) throws Exception {
        restChatInvitationMockMvc.perform(get("/api/chat-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatInvitationMockMvc.perform(get("/api/chat-invitations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatInvitation() throws Exception {
        // Get the chatInvitation
        restChatInvitationMockMvc.perform(get("/api/chat-invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatInvitation() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        int databaseSizeBeforeUpdate = chatInvitationRepository.findAll().size();

        // Update the chatInvitation
        ChatInvitation updatedChatInvitation = chatInvitationRepository.findById(chatInvitation.getId()).get();
        // Disconnect from session so that the updates on updatedChatInvitation are not directly saved in db
        em.detach(updatedChatInvitation);
        updatedChatInvitation
            .creationDate(UPDATED_CREATION_DATE)
            .acceptance(UPDATED_ACCEPTANCE)
            .denial(UPDATED_DENIAL)
            .acceptanceDenialDate(UPDATED_ACCEPTANCE_DENIAL_DATE);
        ChatInvitationDTO chatInvitationDTO = chatInvitationMapper.toDto(updatedChatInvitation);

        restChatInvitationMockMvc.perform(put("/api/chat-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatInvitationDTO)))
            .andExpect(status().isOk());

        // Validate the ChatInvitation in the database
        List<ChatInvitation> chatInvitationList = chatInvitationRepository.findAll();
        assertThat(chatInvitationList).hasSize(databaseSizeBeforeUpdate);
        ChatInvitation testChatInvitation = chatInvitationList.get(chatInvitationList.size() - 1);
        assertThat(testChatInvitation.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatInvitation.isAcceptance()).isEqualTo(UPDATED_ACCEPTANCE);
        assertThat(testChatInvitation.isDenial()).isEqualTo(UPDATED_DENIAL);
        assertThat(testChatInvitation.getAcceptanceDenialDate()).isEqualTo(UPDATED_ACCEPTANCE_DENIAL_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatInvitation() throws Exception {
        int databaseSizeBeforeUpdate = chatInvitationRepository.findAll().size();

        // Create the ChatInvitation
        ChatInvitationDTO chatInvitationDTO = chatInvitationMapper.toDto(chatInvitation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatInvitationMockMvc.perform(put("/api/chat-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatInvitationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatInvitation in the database
        List<ChatInvitation> chatInvitationList = chatInvitationRepository.findAll();
        assertThat(chatInvitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatInvitation() throws Exception {
        // Initialize the database
        chatInvitationRepository.saveAndFlush(chatInvitation);

        int databaseSizeBeforeDelete = chatInvitationRepository.findAll().size();

        // Delete the chatInvitation
        restChatInvitationMockMvc.perform(delete("/api/chat-invitations/{id}", chatInvitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatInvitation> chatInvitationList = chatInvitationRepository.findAll();
        assertThat(chatInvitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatInvitation.class);
        ChatInvitation chatInvitation1 = new ChatInvitation();
        chatInvitation1.setId(1L);
        ChatInvitation chatInvitation2 = new ChatInvitation();
        chatInvitation2.setId(chatInvitation1.getId());
        assertThat(chatInvitation1).isEqualTo(chatInvitation2);
        chatInvitation2.setId(2L);
        assertThat(chatInvitation1).isNotEqualTo(chatInvitation2);
        chatInvitation1.setId(null);
        assertThat(chatInvitation1).isNotEqualTo(chatInvitation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatInvitationDTO.class);
        ChatInvitationDTO chatInvitationDTO1 = new ChatInvitationDTO();
        chatInvitationDTO1.setId(1L);
        ChatInvitationDTO chatInvitationDTO2 = new ChatInvitationDTO();
        assertThat(chatInvitationDTO1).isNotEqualTo(chatInvitationDTO2);
        chatInvitationDTO2.setId(chatInvitationDTO1.getId());
        assertThat(chatInvitationDTO1).isEqualTo(chatInvitationDTO2);
        chatInvitationDTO2.setId(2L);
        assertThat(chatInvitationDTO1).isNotEqualTo(chatInvitationDTO2);
        chatInvitationDTO1.setId(null);
        assertThat(chatInvitationDTO1).isNotEqualTo(chatInvitationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatInvitationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatInvitationMapper.fromId(null)).isNull();
    }
}
