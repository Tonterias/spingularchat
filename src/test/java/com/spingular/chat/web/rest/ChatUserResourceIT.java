package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.domain.User;
import com.spingular.chat.domain.ChatPhoto;
import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.ChatInvitation;
import com.spingular.chat.domain.ChatMessage;
import com.spingular.chat.domain.ChatRoomAllowedUser;
import com.spingular.chat.domain.ChatOffensiveMessage;
import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.repository.ChatUserRepository;
import com.spingular.chat.service.ChatUserService;
import com.spingular.chat.service.dto.ChatUserDTO;
import com.spingular.chat.service.mapper.ChatUserMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatUserCriteria;
import com.spingular.chat.service.ChatUserQueryService;

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
 * Integration tests for the {@link ChatUserResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatUserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_BANNED_USER = false;
    private static final Boolean UPDATED_BANNED_USER = true;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private ChatUserService chatUserService;

    @Autowired
    private ChatUserQueryService chatUserQueryService;

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

    private MockMvc restChatUserMockMvc;

    private ChatUser chatUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatUserResource chatUserResource = new ChatUserResource(chatUserService, chatUserQueryService);
        this.restChatUserMockMvc = MockMvcBuilders.standaloneSetup(chatUserResource)
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
    public static ChatUser createEntity(EntityManager em) {
        ChatUser chatUser = new ChatUser()
            .creationDate(DEFAULT_CREATION_DATE)
            .bannedUser(DEFAULT_BANNED_USER);
        return chatUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatUser createUpdatedEntity(EntityManager em) {
        ChatUser chatUser = new ChatUser()
            .creationDate(UPDATED_CREATION_DATE)
            .bannedUser(UPDATED_BANNED_USER);
        return chatUser;
    }

    @BeforeEach
    public void initTest() {
        chatUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatUser() throws Exception {
        int databaseSizeBeforeCreate = chatUserRepository.findAll().size();

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);
        restChatUserMockMvc.perform(post("/api/chat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatUserDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatUser in the database
        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeCreate + 1);
        ChatUser testChatUser = chatUserList.get(chatUserList.size() - 1);
        assertThat(testChatUser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatUser.isBannedUser()).isEqualTo(DEFAULT_BANNED_USER);
    }

    @Test
    @Transactional
    public void createChatUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatUserRepository.findAll().size();

        // Create the ChatUser with an existing ID
        chatUser.setId(1L);
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatUserMockMvc.perform(post("/api/chat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatUserRepository.findAll().size();
        // set the field null
        chatUser.setCreationDate(null);

        // Create the ChatUser, which fails.
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        restChatUserMockMvc.perform(post("/api/chat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatUsers() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList
        restChatUserMockMvc.perform(get("/api/chat-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bannedUser").value(hasItem(DEFAULT_BANNED_USER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getChatUser() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get the chatUser
        restChatUserMockMvc.perform(get("/api/chat-users/{id}", chatUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatUser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.bannedUser").value(DEFAULT_BANNED_USER.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllChatUsersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatUserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatUserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatUsersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatUserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatUserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatUsersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where creationDate is not null
        defaultChatUserShouldBeFound("creationDate.specified=true");

        // Get all the chatUserList where creationDate is null
        defaultChatUserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatUsersByBannedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where bannedUser equals to DEFAULT_BANNED_USER
        defaultChatUserShouldBeFound("bannedUser.equals=" + DEFAULT_BANNED_USER);

        // Get all the chatUserList where bannedUser equals to UPDATED_BANNED_USER
        defaultChatUserShouldNotBeFound("bannedUser.equals=" + UPDATED_BANNED_USER);
    }

    @Test
    @Transactional
    public void getAllChatUsersByBannedUserIsInShouldWork() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where bannedUser in DEFAULT_BANNED_USER or UPDATED_BANNED_USER
        defaultChatUserShouldBeFound("bannedUser.in=" + DEFAULT_BANNED_USER + "," + UPDATED_BANNED_USER);

        // Get all the chatUserList where bannedUser equals to UPDATED_BANNED_USER
        defaultChatUserShouldNotBeFound("bannedUser.in=" + UPDATED_BANNED_USER);
    }

    @Test
    @Transactional
    public void getAllChatUsersByBannedUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList where bannedUser is not null
        defaultChatUserShouldBeFound("bannedUser.specified=true");

        // Get all the chatUserList where bannedUser is null
        defaultChatUserShouldNotBeFound("bannedUser.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        chatUser.setUser(user);
        chatUserRepository.saveAndFlush(chatUser);
        Long userId = user.getId();

        // Get all the chatUserList where user equals to userId
        defaultChatUserShouldBeFound("userId.equals=" + userId);

        // Get all the chatUserList where user equals to userId + 1
        defaultChatUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatPhoto chatPhoto = ChatPhotoResourceIT.createEntity(em);
        em.persist(chatPhoto);
        em.flush();
        chatUser.setChatPhoto(chatPhoto);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatPhotoId = chatPhoto.getId();

        // Get all the chatUserList where chatPhoto equals to chatPhotoId
        defaultChatUserShouldBeFound("chatPhotoId.equals=" + chatPhotoId);

        // Get all the chatUserList where chatPhoto equals to chatPhotoId + 1
        defaultChatUserShouldNotBeFound("chatPhotoId.equals=" + (chatPhotoId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatRoom chatRoom = ChatRoomResourceIT.createEntity(em);
        em.persist(chatRoom);
        em.flush();
        chatUser.addChatRoom(chatRoom);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatRoomId = chatRoom.getId();

        // Get all the chatUserList where chatRoom equals to chatRoomId
        defaultChatUserShouldBeFound("chatRoomId.equals=" + chatRoomId);

        // Get all the chatUserList where chatRoom equals to chatRoomId + 1
        defaultChatUserShouldNotBeFound("chatRoomId.equals=" + (chatRoomId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatInvitation sender = ChatInvitationResourceIT.createEntity(em);
        em.persist(sender);
        em.flush();
        chatUser.addSender(sender);
        chatUserRepository.saveAndFlush(chatUser);
        Long senderId = sender.getId();

        // Get all the chatUserList where sender equals to senderId
        defaultChatUserShouldBeFound("senderId.equals=" + senderId);

        // Get all the chatUserList where sender equals to senderId + 1
        defaultChatUserShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatInvitation receiver = ChatInvitationResourceIT.createEntity(em);
        em.persist(receiver);
        em.flush();
        chatUser.addReceiver(receiver);
        chatUserRepository.saveAndFlush(chatUser);
        Long receiverId = receiver.getId();

        // Get all the chatUserList where receiver equals to receiverId
        defaultChatUserShouldBeFound("receiverId.equals=" + receiverId);

        // Get all the chatUserList where receiver equals to receiverId + 1
        defaultChatUserShouldNotBeFound("receiverId.equals=" + (receiverId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatMessage chatMessage = ChatMessageResourceIT.createEntity(em);
        em.persist(chatMessage);
        em.flush();
        chatUser.addChatMessage(chatMessage);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatMessageId = chatMessage.getId();

        // Get all the chatUserList where chatMessage equals to chatMessageId
        defaultChatUserShouldBeFound("chatMessageId.equals=" + chatMessageId);

        // Get all the chatUserList where chatMessage equals to chatMessageId + 1
        defaultChatUserShouldNotBeFound("chatMessageId.equals=" + (chatMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatRoomAllowedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatRoomAllowedUser chatRoomAllowedUser = ChatRoomAllowedUserResourceIT.createEntity(em);
        em.persist(chatRoomAllowedUser);
        em.flush();
        chatUser.addChatRoomAllowedUser(chatRoomAllowedUser);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatRoomAllowedUserId = chatRoomAllowedUser.getId();

        // Get all the chatUserList where chatRoomAllowedUser equals to chatRoomAllowedUserId
        defaultChatUserShouldBeFound("chatRoomAllowedUserId.equals=" + chatRoomAllowedUserId);

        // Get all the chatUserList where chatRoomAllowedUser equals to chatRoomAllowedUserId + 1
        defaultChatUserShouldNotBeFound("chatRoomAllowedUserId.equals=" + (chatRoomAllowedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatOffensiveMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatOffensiveMessage chatOffensiveMessage = ChatOffensiveMessageResourceIT.createEntity(em);
        em.persist(chatOffensiveMessage);
        em.flush();
        chatUser.addChatOffensiveMessage(chatOffensiveMessage);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatOffensiveMessageId = chatOffensiveMessage.getId();

        // Get all the chatUserList where chatOffensiveMessage equals to chatOffensiveMessageId
        defaultChatUserShouldBeFound("chatOffensiveMessageId.equals=" + chatOffensiveMessageId);

        // Get all the chatUserList where chatOffensiveMessage equals to chatOffensiveMessageId + 1
        defaultChatUserShouldNotBeFound("chatOffensiveMessageId.equals=" + (chatOffensiveMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllChatUsersByChatNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);
        ChatNotification chatNotification = ChatNotificationResourceIT.createEntity(em);
        em.persist(chatNotification);
        em.flush();
        chatUser.addChatNotification(chatNotification);
        chatUserRepository.saveAndFlush(chatUser);
        Long chatNotificationId = chatNotification.getId();

        // Get all the chatUserList where chatNotification equals to chatNotificationId
        defaultChatUserShouldBeFound("chatNotificationId.equals=" + chatNotificationId);

        // Get all the chatUserList where chatNotification equals to chatNotificationId + 1
        defaultChatUserShouldNotBeFound("chatNotificationId.equals=" + (chatNotificationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatUserShouldBeFound(String filter) throws Exception {
        restChatUserMockMvc.perform(get("/api/chat-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bannedUser").value(hasItem(DEFAULT_BANNED_USER.booleanValue())));

        // Check, that the count call also returns 1
        restChatUserMockMvc.perform(get("/api/chat-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatUserShouldNotBeFound(String filter) throws Exception {
        restChatUserMockMvc.perform(get("/api/chat-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatUserMockMvc.perform(get("/api/chat-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatUser() throws Exception {
        // Get the chatUser
        restChatUserMockMvc.perform(get("/api/chat-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatUser() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        int databaseSizeBeforeUpdate = chatUserRepository.findAll().size();

        // Update the chatUser
        ChatUser updatedChatUser = chatUserRepository.findById(chatUser.getId()).get();
        // Disconnect from session so that the updates on updatedChatUser are not directly saved in db
        em.detach(updatedChatUser);
        updatedChatUser
            .creationDate(UPDATED_CREATION_DATE)
            .bannedUser(UPDATED_BANNED_USER);
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(updatedChatUser);

        restChatUserMockMvc.perform(put("/api/chat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatUserDTO)))
            .andExpect(status().isOk());

        // Validate the ChatUser in the database
        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeUpdate);
        ChatUser testChatUser = chatUserList.get(chatUserList.size() - 1);
        assertThat(testChatUser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatUser.isBannedUser()).isEqualTo(UPDATED_BANNED_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingChatUser() throws Exception {
        int databaseSizeBeforeUpdate = chatUserRepository.findAll().size();

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatUserMockMvc.perform(put("/api/chat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatUser() throws Exception {
        // Initialize the database
        chatUserRepository.saveAndFlush(chatUser);

        int databaseSizeBeforeDelete = chatUserRepository.findAll().size();

        // Delete the chatUser
        restChatUserMockMvc.perform(delete("/api/chat-users/{id}", chatUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatUser> chatUserList = chatUserRepository.findAll();
        assertThat(chatUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatUser.class);
        ChatUser chatUser1 = new ChatUser();
        chatUser1.setId(1L);
        ChatUser chatUser2 = new ChatUser();
        chatUser2.setId(chatUser1.getId());
        assertThat(chatUser1).isEqualTo(chatUser2);
        chatUser2.setId(2L);
        assertThat(chatUser1).isNotEqualTo(chatUser2);
        chatUser1.setId(null);
        assertThat(chatUser1).isNotEqualTo(chatUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatUserDTO.class);
        ChatUserDTO chatUserDTO1 = new ChatUserDTO();
        chatUserDTO1.setId(1L);
        ChatUserDTO chatUserDTO2 = new ChatUserDTO();
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
        chatUserDTO2.setId(chatUserDTO1.getId());
        assertThat(chatUserDTO1).isEqualTo(chatUserDTO2);
        chatUserDTO2.setId(2L);
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
        chatUserDTO1.setId(null);
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatUserMapper.fromId(null)).isNull();
    }
}
