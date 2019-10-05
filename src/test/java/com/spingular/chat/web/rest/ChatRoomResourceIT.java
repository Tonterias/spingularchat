package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.ChatMessage;
import com.spingular.chat.domain.ChatRoomAllowedUser;
import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.domain.ChatInvitation;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.repository.ChatRoomRepository;
import com.spingular.chat.service.ChatRoomService;
import com.spingular.chat.service.dto.ChatRoomDTO;
import com.spingular.chat.service.mapper.ChatRoomMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatRoomCriteria;
import com.spingular.chat.service.ChatRoomQueryService;

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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link ChatRoomResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatRoomResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_ROOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRIVATE_ROOM = false;
    private static final Boolean UPDATED_PRIVATE_ROOM = true;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomQueryService chatRoomQueryService;

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

    private MockMvc restChatRoomMockMvc;

    private ChatRoom chatRoom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatRoomResource chatRoomResource = new ChatRoomResource(chatRoomService, chatRoomQueryService);
        this.restChatRoomMockMvc = MockMvcBuilders.standaloneSetup(chatRoomResource)
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
    public static ChatRoom createEntity(EntityManager em) {
        ChatRoom chatRoom = new ChatRoom()
            .creationDate(DEFAULT_CREATION_DATE)
            .roomName(DEFAULT_ROOM_NAME)
            .roomDescription(DEFAULT_ROOM_DESCRIPTION)
            .privateRoom(DEFAULT_PRIVATE_ROOM)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return chatRoom;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoom createUpdatedEntity(EntityManager em) {
        ChatRoom chatRoom = new ChatRoom()
            .creationDate(UPDATED_CREATION_DATE)
            .roomName(UPDATED_ROOM_NAME)
            .roomDescription(UPDATED_ROOM_DESCRIPTION)
            .privateRoom(UPDATED_PRIVATE_ROOM)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return chatRoom;
    }

    @BeforeEach
    public void initTest() {
        chatRoom = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatRoom() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);
        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatRoom.getRoomName()).isEqualTo(DEFAULT_ROOM_NAME);
        assertThat(testChatRoom.getRoomDescription()).isEqualTo(DEFAULT_ROOM_DESCRIPTION);
        assertThat(testChatRoom.isPrivateRoom()).isEqualTo(DEFAULT_PRIVATE_ROOM);
        assertThat(testChatRoom.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testChatRoom.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createChatRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // Create the ChatRoom with an existing ID
        chatRoom.setId(1L);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setCreationDate(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoomNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setRoomName(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatRooms() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList
        restChatRoomMockMvc.perform(get("/api/chat-rooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].roomName").value(hasItem(DEFAULT_ROOM_NAME.toString())))
            .andExpect(jsonPath("$.[*].roomDescription").value(hasItem(DEFAULT_ROOM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].privateRoom").value(hasItem(DEFAULT_PRIVATE_ROOM.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chat-rooms/{id}", chatRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatRoom.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.roomName").value(DEFAULT_ROOM_NAME.toString()))
            .andExpect(jsonPath("$.roomDescription").value(DEFAULT_ROOM_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.privateRoom").value(DEFAULT_PRIVATE_ROOM.booleanValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getAllChatRoomsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatRoomShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatRoomList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatRoomShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatRoomShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatRoomList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatRoomShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where creationDate is not null
        defaultChatRoomShouldBeFound("creationDate.specified=true");

        // Get all the chatRoomList where creationDate is null
        defaultChatRoomShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomNameIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomName equals to DEFAULT_ROOM_NAME
        defaultChatRoomShouldBeFound("roomName.equals=" + DEFAULT_ROOM_NAME);

        // Get all the chatRoomList where roomName equals to UPDATED_ROOM_NAME
        defaultChatRoomShouldNotBeFound("roomName.equals=" + UPDATED_ROOM_NAME);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomNameIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomName in DEFAULT_ROOM_NAME or UPDATED_ROOM_NAME
        defaultChatRoomShouldBeFound("roomName.in=" + DEFAULT_ROOM_NAME + "," + UPDATED_ROOM_NAME);

        // Get all the chatRoomList where roomName equals to UPDATED_ROOM_NAME
        defaultChatRoomShouldNotBeFound("roomName.in=" + UPDATED_ROOM_NAME);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomName is not null
        defaultChatRoomShouldBeFound("roomName.specified=true");

        // Get all the chatRoomList where roomName is null
        defaultChatRoomShouldNotBeFound("roomName.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomDescription equals to DEFAULT_ROOM_DESCRIPTION
        defaultChatRoomShouldBeFound("roomDescription.equals=" + DEFAULT_ROOM_DESCRIPTION);

        // Get all the chatRoomList where roomDescription equals to UPDATED_ROOM_DESCRIPTION
        defaultChatRoomShouldNotBeFound("roomDescription.equals=" + UPDATED_ROOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomDescription in DEFAULT_ROOM_DESCRIPTION or UPDATED_ROOM_DESCRIPTION
        defaultChatRoomShouldBeFound("roomDescription.in=" + DEFAULT_ROOM_DESCRIPTION + "," + UPDATED_ROOM_DESCRIPTION);

        // Get all the chatRoomList where roomDescription equals to UPDATED_ROOM_DESCRIPTION
        defaultChatRoomShouldNotBeFound("roomDescription.in=" + UPDATED_ROOM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByRoomDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where roomDescription is not null
        defaultChatRoomShouldBeFound("roomDescription.specified=true");

        // Get all the chatRoomList where roomDescription is null
        defaultChatRoomShouldNotBeFound("roomDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomsByPrivateRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where privateRoom equals to DEFAULT_PRIVATE_ROOM
        defaultChatRoomShouldBeFound("privateRoom.equals=" + DEFAULT_PRIVATE_ROOM);

        // Get all the chatRoomList where privateRoom equals to UPDATED_PRIVATE_ROOM
        defaultChatRoomShouldNotBeFound("privateRoom.equals=" + UPDATED_PRIVATE_ROOM);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByPrivateRoomIsInShouldWork() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where privateRoom in DEFAULT_PRIVATE_ROOM or UPDATED_PRIVATE_ROOM
        defaultChatRoomShouldBeFound("privateRoom.in=" + DEFAULT_PRIVATE_ROOM + "," + UPDATED_PRIVATE_ROOM);

        // Get all the chatRoomList where privateRoom equals to UPDATED_PRIVATE_ROOM
        defaultChatRoomShouldNotBeFound("privateRoom.in=" + UPDATED_PRIVATE_ROOM);
    }

    @Test
    @Transactional
    public void getAllChatRoomsByPrivateRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList where privateRoom is not null
        defaultChatRoomShouldBeFound("privateRoom.specified=true");

        // Get all the chatRoomList where privateRoom is null
        defaultChatRoomShouldNotBeFound("privateRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatRoomsByChatMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        ChatMessage chatMessage = ChatMessageResourceIT.createEntity(em);
        em.persist(chatMessage);
        em.flush();
        chatRoom.addChatMessage(chatMessage);
        chatRoomRepository.saveAndFlush(chatRoom);
        Long chatMessageId = chatMessage.getId();

        // Get all the chatRoomList where chatMessage equals to chatMessageId
        defaultChatRoomShouldBeFound("chatMessageId.equals=" + chatMessageId);

        // Get all the chatRoomList where chatMessage equals to chatMessageId + 1
        defaultChatRoomShouldNotBeFound("chatMessageId.equals=" + (chatMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllChatRoomsByChatRoomAllowedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        ChatRoomAllowedUser chatRoomAllowedUser = ChatRoomAllowedUserResourceIT.createEntity(em);
        em.persist(chatRoomAllowedUser);
        em.flush();
        chatRoom.addChatRoomAllowedUser(chatRoomAllowedUser);
        chatRoomRepository.saveAndFlush(chatRoom);
        Long chatRoomAllowedUserId = chatRoomAllowedUser.getId();

        // Get all the chatRoomList where chatRoomAllowedUser equals to chatRoomAllowedUserId
        defaultChatRoomShouldBeFound("chatRoomAllowedUserId.equals=" + chatRoomAllowedUserId);

        // Get all the chatRoomList where chatRoomAllowedUser equals to chatRoomAllowedUserId + 1
        defaultChatRoomShouldNotBeFound("chatRoomAllowedUserId.equals=" + (chatRoomAllowedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllChatRoomsByChatNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        ChatNotification chatNotification = ChatNotificationResourceIT.createEntity(em);
        em.persist(chatNotification);
        em.flush();
        chatRoom.addChatNotification(chatNotification);
        chatRoomRepository.saveAndFlush(chatRoom);
        Long chatNotificationId = chatNotification.getId();

        // Get all the chatRoomList where chatNotification equals to chatNotificationId
        defaultChatRoomShouldBeFound("chatNotificationId.equals=" + chatNotificationId);

        // Get all the chatRoomList where chatNotification equals to chatNotificationId + 1
        defaultChatRoomShouldNotBeFound("chatNotificationId.equals=" + (chatNotificationId + 1));
    }


    @Test
    @Transactional
    public void getAllChatRoomsByChatInvitationIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        ChatInvitation chatInvitation = ChatInvitationResourceIT.createEntity(em);
        em.persist(chatInvitation);
        em.flush();
        chatRoom.addChatInvitation(chatInvitation);
        chatRoomRepository.saveAndFlush(chatRoom);
        Long chatInvitationId = chatInvitation.getId();

        // Get all the chatRoomList where chatInvitation equals to chatInvitationId
        defaultChatRoomShouldBeFound("chatInvitationId.equals=" + chatInvitationId);

        // Get all the chatRoomList where chatInvitation equals to chatInvitationId + 1
        defaultChatRoomShouldNotBeFound("chatInvitationId.equals=" + (chatInvitationId + 1));
    }


    @Test
    @Transactional
    public void getAllChatRoomsByChatUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        ChatUser chatUser = ChatUserResourceIT.createEntity(em);
        em.persist(chatUser);
        em.flush();
        chatRoom.setChatUser(chatUser);
        chatRoomRepository.saveAndFlush(chatRoom);
        Long chatUserId = chatUser.getId();

        // Get all the chatRoomList where chatUser equals to chatUserId
        defaultChatRoomShouldBeFound("chatUserId.equals=" + chatUserId);

        // Get all the chatRoomList where chatUser equals to chatUserId + 1
        defaultChatRoomShouldNotBeFound("chatUserId.equals=" + (chatUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatRoomShouldBeFound(String filter) throws Exception {
        restChatRoomMockMvc.perform(get("/api/chat-rooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].roomName").value(hasItem(DEFAULT_ROOM_NAME)))
            .andExpect(jsonPath("$.[*].roomDescription").value(hasItem(DEFAULT_ROOM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].privateRoom").value(hasItem(DEFAULT_PRIVATE_ROOM.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restChatRoomMockMvc.perform(get("/api/chat-rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatRoomShouldNotBeFound(String filter) throws Exception {
        restChatRoomMockMvc.perform(get("/api/chat-rooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatRoomMockMvc.perform(get("/api/chat-rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatRoom() throws Exception {
        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chat-rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Update the chatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findById(chatRoom.getId()).get();
        // Disconnect from session so that the updates on updatedChatRoom are not directly saved in db
        em.detach(updatedChatRoom);
        updatedChatRoom
            .creationDate(UPDATED_CREATION_DATE)
            .roomName(UPDATED_ROOM_NAME)
            .roomDescription(UPDATED_ROOM_DESCRIPTION)
            .privateRoom(UPDATED_PRIVATE_ROOM)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(updatedChatRoom);

        restChatRoomMockMvc.perform(put("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatRoom.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testChatRoom.getRoomDescription()).isEqualTo(UPDATED_ROOM_DESCRIPTION);
        assertThat(testChatRoom.isPrivateRoom()).isEqualTo(UPDATED_PRIVATE_ROOM);
        assertThat(testChatRoom.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChatRoom.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomMockMvc.perform(put("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeDelete = chatRoomRepository.findAll().size();

        // Delete the chatRoom
        restChatRoomMockMvc.perform(delete("/api/chat-rooms/{id}", chatRoom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoom.class);
        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setId(1L);
        ChatRoom chatRoom2 = new ChatRoom();
        chatRoom2.setId(chatRoom1.getId());
        assertThat(chatRoom1).isEqualTo(chatRoom2);
        chatRoom2.setId(2L);
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
        chatRoom1.setId(null);
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoomDTO.class);
        ChatRoomDTO chatRoomDTO1 = new ChatRoomDTO();
        chatRoomDTO1.setId(1L);
        ChatRoomDTO chatRoomDTO2 = new ChatRoomDTO();
        assertThat(chatRoomDTO1).isNotEqualTo(chatRoomDTO2);
        chatRoomDTO2.setId(chatRoomDTO1.getId());
        assertThat(chatRoomDTO1).isEqualTo(chatRoomDTO2);
        chatRoomDTO2.setId(2L);
        assertThat(chatRoomDTO1).isNotEqualTo(chatRoomDTO2);
        chatRoomDTO1.setId(null);
        assertThat(chatRoomDTO1).isNotEqualTo(chatRoomDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatRoomMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatRoomMapper.fromId(null)).isNull();
    }
}
