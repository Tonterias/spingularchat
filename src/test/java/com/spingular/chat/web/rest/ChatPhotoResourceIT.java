package com.spingular.chat.web.rest;

import com.spingular.chat.SpingularchatApp;
import com.spingular.chat.domain.ChatPhoto;
import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.repository.ChatPhotoRepository;
import com.spingular.chat.service.ChatPhotoService;
import com.spingular.chat.service.dto.ChatPhotoDTO;
import com.spingular.chat.service.mapper.ChatPhotoMapper;
import com.spingular.chat.web.rest.errors.ExceptionTranslator;
import com.spingular.chat.service.dto.ChatPhotoCriteria;
import com.spingular.chat.service.ChatPhotoQueryService;

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
 * Integration tests for the {@link ChatPhotoResource} REST controller.
 */
@SpringBootTest(classes = SpingularchatApp.class)
public class ChatPhotoResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATION_DATE = Instant.ofEpochMilli(-1L);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ChatPhotoRepository chatPhotoRepository;

    @Autowired
    private ChatPhotoMapper chatPhotoMapper;

    @Autowired
    private ChatPhotoService chatPhotoService;

    @Autowired
    private ChatPhotoQueryService chatPhotoQueryService;

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

    private MockMvc restChatPhotoMockMvc;

    private ChatPhoto chatPhoto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatPhotoResource chatPhotoResource = new ChatPhotoResource(chatPhotoService, chatPhotoQueryService);
        this.restChatPhotoMockMvc = MockMvcBuilders.standaloneSetup(chatPhotoResource)
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
    public static ChatPhoto createEntity(EntityManager em) {
        ChatPhoto chatPhoto = new ChatPhoto()
            .creationDate(DEFAULT_CREATION_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return chatPhoto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatPhoto createUpdatedEntity(EntityManager em) {
        ChatPhoto chatPhoto = new ChatPhoto()
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return chatPhoto;
    }

    @BeforeEach
    public void initTest() {
        chatPhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatPhoto() throws Exception {
        int databaseSizeBeforeCreate = chatPhotoRepository.findAll().size();

        // Create the ChatPhoto
        ChatPhotoDTO chatPhotoDTO = chatPhotoMapper.toDto(chatPhoto);
        restChatPhotoMockMvc.perform(post("/api/chat-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatPhotoDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatPhoto in the database
        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        ChatPhoto testChatPhoto = chatPhotoList.get(chatPhotoList.size() - 1);
        assertThat(testChatPhoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testChatPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testChatPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createChatPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatPhotoRepository.findAll().size();

        // Create the ChatPhoto with an existing ID
        chatPhoto.setId(1L);
        ChatPhotoDTO chatPhotoDTO = chatPhotoMapper.toDto(chatPhoto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatPhotoMockMvc.perform(post("/api/chat-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatPhoto in the database
        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatPhotoRepository.findAll().size();
        // set the field null
        chatPhoto.setCreationDate(null);

        // Create the ChatPhoto, which fails.
        ChatPhotoDTO chatPhotoDTO = chatPhotoMapper.toDto(chatPhoto);

        restChatPhotoMockMvc.perform(post("/api/chat-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatPhotoDTO)))
            .andExpect(status().isBadRequest());

        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatPhotos() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        // Get all the chatPhotoList
        restChatPhotoMockMvc.perform(get("/api/chat-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getChatPhoto() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        // Get the chatPhoto
        restChatPhotoMockMvc.perform(get("/api/chat-photos/{id}", chatPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatPhoto.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getAllChatPhotosByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        // Get all the chatPhotoList where creationDate equals to DEFAULT_CREATION_DATE
        defaultChatPhotoShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the chatPhotoList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatPhotoShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatPhotosByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        // Get all the chatPhotoList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultChatPhotoShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the chatPhotoList where creationDate equals to UPDATED_CREATION_DATE
        defaultChatPhotoShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllChatPhotosByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        // Get all the chatPhotoList where creationDate is not null
        defaultChatPhotoShouldBeFound("creationDate.specified=true");

        // Get all the chatPhotoList where creationDate is null
        defaultChatPhotoShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatPhotosByChatUserIsEqualToSomething() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);
        ChatUser chatUser = ChatUserResourceIT.createEntity(em);
        em.persist(chatUser);
        em.flush();
        chatPhoto.setChatUser(chatUser);
        chatUser.setChatPhoto(chatPhoto);
        chatPhotoRepository.saveAndFlush(chatPhoto);
        Long chatUserId = chatUser.getId();

        // Get all the chatPhotoList where chatUser equals to chatUserId
        defaultChatPhotoShouldBeFound("chatUserId.equals=" + chatUserId);

        // Get all the chatPhotoList where chatUser equals to chatUserId + 1
        defaultChatPhotoShouldNotBeFound("chatUserId.equals=" + (chatUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatPhotoShouldBeFound(String filter) throws Exception {
        restChatPhotoMockMvc.perform(get("/api/chat-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restChatPhotoMockMvc.perform(get("/api/chat-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatPhotoShouldNotBeFound(String filter) throws Exception {
        restChatPhotoMockMvc.perform(get("/api/chat-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatPhotoMockMvc.perform(get("/api/chat-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatPhoto() throws Exception {
        // Get the chatPhoto
        restChatPhotoMockMvc.perform(get("/api/chat-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatPhoto() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        int databaseSizeBeforeUpdate = chatPhotoRepository.findAll().size();

        // Update the chatPhoto
        ChatPhoto updatedChatPhoto = chatPhotoRepository.findById(chatPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedChatPhoto are not directly saved in db
        em.detach(updatedChatPhoto);
        updatedChatPhoto
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ChatPhotoDTO chatPhotoDTO = chatPhotoMapper.toDto(updatedChatPhoto);

        restChatPhotoMockMvc.perform(put("/api/chat-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatPhotoDTO)))
            .andExpect(status().isOk());

        // Validate the ChatPhoto in the database
        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeUpdate);
        ChatPhoto testChatPhoto = chatPhotoList.get(chatPhotoList.size() - 1);
        assertThat(testChatPhoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testChatPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChatPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatPhoto() throws Exception {
        int databaseSizeBeforeUpdate = chatPhotoRepository.findAll().size();

        // Create the ChatPhoto
        ChatPhotoDTO chatPhotoDTO = chatPhotoMapper.toDto(chatPhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatPhotoMockMvc.perform(put("/api/chat-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatPhoto in the database
        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatPhoto() throws Exception {
        // Initialize the database
        chatPhotoRepository.saveAndFlush(chatPhoto);

        int databaseSizeBeforeDelete = chatPhotoRepository.findAll().size();

        // Delete the chatPhoto
        restChatPhotoMockMvc.perform(delete("/api/chat-photos/{id}", chatPhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatPhoto> chatPhotoList = chatPhotoRepository.findAll();
        assertThat(chatPhotoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatPhoto.class);
        ChatPhoto chatPhoto1 = new ChatPhoto();
        chatPhoto1.setId(1L);
        ChatPhoto chatPhoto2 = new ChatPhoto();
        chatPhoto2.setId(chatPhoto1.getId());
        assertThat(chatPhoto1).isEqualTo(chatPhoto2);
        chatPhoto2.setId(2L);
        assertThat(chatPhoto1).isNotEqualTo(chatPhoto2);
        chatPhoto1.setId(null);
        assertThat(chatPhoto1).isNotEqualTo(chatPhoto2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatPhotoDTO.class);
        ChatPhotoDTO chatPhotoDTO1 = new ChatPhotoDTO();
        chatPhotoDTO1.setId(1L);
        ChatPhotoDTO chatPhotoDTO2 = new ChatPhotoDTO();
        assertThat(chatPhotoDTO1).isNotEqualTo(chatPhotoDTO2);
        chatPhotoDTO2.setId(chatPhotoDTO1.getId());
        assertThat(chatPhotoDTO1).isEqualTo(chatPhotoDTO2);
        chatPhotoDTO2.setId(2L);
        assertThat(chatPhotoDTO1).isNotEqualTo(chatPhotoDTO2);
        chatPhotoDTO1.setId(null);
        assertThat(chatPhotoDTO1).isNotEqualTo(chatPhotoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatPhotoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatPhotoMapper.fromId(null)).isNull();
    }
}
