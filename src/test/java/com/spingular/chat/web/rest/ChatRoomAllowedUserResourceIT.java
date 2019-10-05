package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatRoomAllowedUser;
import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.repository.ChatRoomAllowedUserRepository;
import com.spingular.chat.service.ChatRoomAllowedUserService;
import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;
import com.spingular.chat.service.mapper.ChatRoomAllowedUserMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatRoomAllowedUserCriteria;
import com.spingular.chat.service.ChatRoomAllowedUserQueryService;

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
 * Integration tests for the {@link ChatRoomAllowedUserResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatRoomAllowedUserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_BANNED_USER = false;
    private static final Boolean UPDATED_BANNED_USER = true;

    private static final Instant DEFAULT_BANNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BANNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_BANNED_DATE = Instant.ofEpochMilli(-1L);

    @Autowired
    private ChatRoomAllowedUserRepository chatRoomAllowedUserRepository;

    @Autowired
    private ChatRoomAllowedUserMapper chatRoomAllowedUserMapper;

    @Autowired
    private ChatRoomAllowedUserService chatRoomAllowedUserService;

    @Autowired
    private ChatRoomAllowedUserQueryService chatRoomAllowedUserQueryService;

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

    private MockMvc restChatRoomAllowedUserMockMvc;

    private ChatRoomAllowedUser chatRoomAllowedUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatRoomAllowedUserResource chatRoomAllowedUserResource = new ChatRoomAllowedUserResource(chatRoomAllowedUserService, chatRoomAllowedUserQueryService);
        this.restChatRoomAllowedUserMockMvc = MockMvcBuilders.standaloneSetup(chatRoomAllowedUserResource)
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
    public static ChatRoomAllowedUser createEntity(EntityManager em) {
        ChatRoomAllowedUser chatRoomAllowedUser = new ChatRoomAllowedUser()
            .creationDate(DEFAULT_CREATION_DATE)
            .bannedUser(DEFAULT_BANNED_USER)
            .bannedDate(DEFAULT_BANNED_DATE);
        return chatRoomAllowedUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoomAllowedUser createUpdatedEntity(EntityManager em) {
        ChatRoomAllowedUser chatRoomAllowedUser = new ChatRoomAllowedUser()
            .creationDate(UPDATED_CREATION_DATE)
            .bannedUser(UPDATED_BANNED_USER)
            .bannedDate(UPDATED_BANNED_DATE);
        return chatRoomAllowedUser;
    }

    @BeforeEach
    public void initTest() {
        chatRoomAllowedUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatRoomAllowedUser() throws Exception {
        int databaseSizeBeforeCreate = chatRoomAllowedUserRepository.findAll().size();

        // Create the ChatRoomAllowedUser
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = chatRoomAllowedUserMapper.toDto(chatRoomAllowedUser);
        restChatRoomAllowedUserMockMvc.perform(post("/api/chat-room-allowed-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomAllowedUserDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatRoomAllowedUser in the database
        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeCreate + 1);
        ChatRoomAllowedUser testChatRoomAllowedUser = chatRoomAllowedUserList.get(chatRoomAllowedUserList.size() - 1);
        assertThat(testChatRoomAllowedUser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatRoomAllowedUser.isBannedUser()).isEqualTo(DEFAULT_BANNED_USER);
        assertThat(testChatRoomAllowedUser.getBannedDate()).isEqualTo(DEFAULT_BANNED_DATE);
    }

    @Test
    @Transactional
    public void createChatRoomAllowedUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatRoomAllowedUserRepository.findAll().size();

        // Create the ChatRoomAllowedUser with an existing ID
        chatRoomAllowedUser.setId(1L);
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = chatRoomAllowedUserMapper.toDto(chatRoomAllowedUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatRoomAllowedUserMockMvc.perform(post("/api/chat-room-allowed-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomAllowedUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoomAllowedUser in the database
        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomAllowedUserRepository.findAll().size();
        // set the field null
        chatRoomAllowedUser.setCreationDate(null);

        // Create the ChatRoomAllowedUser, which fails.
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = chatRoomAllowedUserMapper.toDto(chatRoomAllowedUser);

        restChatRoomAllowedUserMockMvc.perform(post("/api/chat-room-allowed-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomAllowedUserDTO)))
            .andExpect(status().isBadRequest());

        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsers() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoomAllowedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bannedUser").value(hasItem(DEFAULT_BANNED_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].bannedDate").value(hasItem(DEFAULT_BANNED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getChatRoomAllowedUser() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get the chatRoomAllowedUser
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users/{id}", chatRoomAllowedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatRoomAllowedUser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.bannedUser").value(DEFAULT_BANNED_USER.booleanValue()))
            .andExpect(jsonPath("$.bannedDate").value(DEFAULT_BANNED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatRoomAllowedUserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatRoomAllowedUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatRoomAllowedUserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatRoomAllowedUserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatRoomAllowedUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatRoomAllowedUserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where creationDate is not null
        defaultChatRoomAllowedUserShouldBeFound("creationDate.specified=true");

        // Get all the chatRoomAllowedUserList where creationDate is null
        defaultChatRoomAllowedUserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedUser equals to DEFAULT_BANNED_USER
        defaultChatRoomAllowedUserShouldBeFound("bannedUser.equals=" + DEFAULT_BANNED_USER);

        // Get all the chatRoomAllowedUserList where bannedUser equals to UPDATED_BANNED_USER
        defaultChatRoomAllowedUserShouldNotBeFound("bannedUser.equals=" + UPDATED_BANNED_USER);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedUserIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedUser in DEFAULT_BANNED_USER or UPDATED_BANNED_USER
        defaultChatRoomAllowedUserShouldBeFound("bannedUser.in=" + DEFAULT_BANNED_USER + "," + UPDATED_BANNED_USER);

        // Get all the chatRoomAllowedUserList where bannedUser equals to UPDATED_BANNED_USER
        defaultChatRoomAllowedUserShouldNotBeFound("bannedUser.in=" + UPDATED_BANNED_USER);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedUser is not null
        defaultChatRoomAllowedUserShouldBeFound("bannedUser.specified=true");

        // Get all the chatRoomAllowedUserList where bannedUser is null
        defaultChatRoomAllowedUserShouldNotBeFound("bannedUser.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedDate equals to DEFAULT_BANNED_DATE
        defaultChatRoomAllowedUserShouldBeFound("bannedDate.equals=" + DEFAULT_BANNED_DATE);

        // Get all the chatRoomAllowedUserList where bannedDate equals to UPDATED_BANNED_DATE
        defaultChatRoomAllowedUserShouldNotBeFound("bannedDate.equals=" + UPDATED_BANNED_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedDate in DEFAULT_BANNED_DATE or UPDATED_BANNED_DATE
        defaultChatRoomAllowedUserShouldBeFound("bannedDate.in=" + DEFAULT_BANNED_DATE + "," + UPDATED_BANNED_DATE);

        // Get all the chatRoomAllowedUserList where bannedDate equals to UPDATED_BANNED_DATE
        defaultChatRoomAllowedUserShouldNotBeFound("bannedDate.in=" + UPDATED_BANNED_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByBannedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        // Get all the chatRoomAllowedUserList where bannedDate is not null
        defaultChatRoomAllowedUserShouldBeFound("bannedDate.specified=true");

        // Get all the chatRoomAllowedUserList where bannedDate is null
        defaultChatRoomAllowedUserShouldNotBeFound("bannedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByChatRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);
        ChatRoom chatRoom = ChatRoomResourceIT.createEntity(em);
        em.persist(chatRoom);
        em.flush();
        chatRoomAllowedUser.setChatRoom(chatRoom);
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);
        Long chatRoomId = chatRoom.getId();

        // Get all the chatRoomAllowedUserList where chatRoom equals to chatRoomId
        defaultChatRoomAllowedUserShouldBeFound("chatRoomId.equals=" + chatRoomId);

        // Get all the chatRoomAllowedUserList where chatRoom equals to chatRoomId + 1
        defaultChatRoomAllowedUserShouldNotBeFound("chatRoomId.equals=" + (chatRoomId + 1));
    }


    @Test
    @Transactional
    public void getAllChatRoomAllowedUsersByChatUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);
        ChatUser chatUser = ChatUserResourceIT.createEntity(em);
        em.persist(chatUser);
        em.flush();
        chatRoomAllowedUser.setChatUser(chatUser);
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);
        Long chatUserId = chatUser.getId();

        // Get all the chatRoomAllowedUserList where chatUser equals to chatUserId
        defaultChatRoomAllowedUserShouldBeFound("chatUserId.equals=" + chatUserId);

        // Get all the chatRoomAllowedUserList where chatUser equals to chatUserId + 1
        defaultChatRoomAllowedUserShouldNotBeFound("chatUserId.equals=" + (chatUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatRoomAllowedUserShouldBeFound(String filter) throws Exception {
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoomAllowedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bannedUser").value(hasItem(DEFAULT_BANNED_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].bannedDate").value(hasItem(DEFAULT_BANNED_DATE.toString())));

        // Check, that the count call also returns 1
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatRoomAllowedUserShouldNotBeFound(String filter) throws Exception {
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatRoomAllowedUser() throws Exception {
        // Get the chatRoomAllowedUser
        restChatRoomAllowedUserMockMvc.perform(get("/api/chat-room-allowed-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatRoomAllowedUser() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        int databaseSizeBeforeUpdate = chatRoomAllowedUserRepository.findAll().size();

        // Update the chatRoomAllowedUser
        ChatRoomAllowedUser updatedChatRoomAllowedUser = chatRoomAllowedUserRepository.findById(chatRoomAllowedUser.getId()).get();
        // Disconnect from session so that the updates on updatedChatRoomAllowedUser are not directly saved in db
        em.detach(updatedChatRoomAllowedUser);
        updatedChatRoomAllowedUser
            .creationDate(UPDATED_CREATION_DATE)
            .bannedUser(UPDATED_BANNED_USER)
            .bannedDate(UPDATED_BANNED_DATE);
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = chatRoomAllowedUserMapper.toDto(updatedChatRoomAllowedUser);

        restChatRoomAllowedUserMockMvc.perform(put("/api/chat-room-allowed-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomAllowedUserDTO)))
            .andExpect(status().isOk());

        // Validate the ChatRoomAllowedUser in the database
        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeUpdate);
        ChatRoomAllowedUser testChatRoomAllowedUser = chatRoomAllowedUserList.get(chatRoomAllowedUserList.size() - 1);
        assertThat(testChatRoomAllowedUser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatRoomAllowedUser.isBannedUser()).isEqualTo(UPDATED_BANNED_USER);
        assertThat(testChatRoomAllowedUser.getBannedDate()).isEqualTo(UPDATED_BANNED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatRoomAllowedUser() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomAllowedUserRepository.findAll().size();

        // Create the ChatRoomAllowedUser
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = chatRoomAllowedUserMapper.toDto(chatRoomAllowedUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomAllowedUserMockMvc.perform(put("/api/chat-room-allowed-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomAllowedUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoomAllowedUser in the database
        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatRoomAllowedUser() throws Exception {
        // Initialize the database
        chatRoomAllowedUserRepository.saveAndFlush(chatRoomAllowedUser);

        int databaseSizeBeforeDelete = chatRoomAllowedUserRepository.findAll().size();

        // Delete the chatRoomAllowedUser
        restChatRoomAllowedUserMockMvc.perform(delete("/api/chat-room-allowed-users/{id}", chatRoomAllowedUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatRoomAllowedUser> chatRoomAllowedUserList = chatRoomAllowedUserRepository.findAll();
        assertThat(chatRoomAllowedUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoomAllowedUser.class);
        ChatRoomAllowedUser chatRoomAllowedUser1 = new ChatRoomAllowedUser();
        chatRoomAllowedUser1.setId(1L);
        ChatRoomAllowedUser chatRoomAllowedUser2 = new ChatRoomAllowedUser();
        chatRoomAllowedUser2.setId(chatRoomAllowedUser1.getId());
        assertThat(chatRoomAllowedUser1).isEqualTo(chatRoomAllowedUser2);
        chatRoomAllowedUser2.setId(2L);
        assertThat(chatRoomAllowedUser1).isNotEqualTo(chatRoomAllowedUser2);
        chatRoomAllowedUser1.setId(null);
        assertThat(chatRoomAllowedUser1).isNotEqualTo(chatRoomAllowedUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoomAllowedUserDTO.class);
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO1 = new ChatRoomAllowedUserDTO();
        chatRoomAllowedUserDTO1.setId(1L);
        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO2 = new ChatRoomAllowedUserDTO();
        assertThat(chatRoomAllowedUserDTO1).isNotEqualTo(chatRoomAllowedUserDTO2);
        chatRoomAllowedUserDTO2.setId(chatRoomAllowedUserDTO1.getId());
        assertThat(chatRoomAllowedUserDTO1).isEqualTo(chatRoomAllowedUserDTO2);
        chatRoomAllowedUserDTO2.setId(2L);
        assertThat(chatRoomAllowedUserDTO1).isNotEqualTo(chatRoomAllowedUserDTO2);
        chatRoomAllowedUserDTO1.setId(null);
        assertThat(chatRoomAllowedUserDTO1).isNotEqualTo(chatRoomAllowedUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatRoomAllowedUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatRoomAllowedUserMapper.fromId(null)).isNull();
    }
}
