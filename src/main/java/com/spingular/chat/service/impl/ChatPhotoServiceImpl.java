package com.spingular.chat.service.impl;

import com.spingular.chat.service.ChatPhotoService;
import com.spingular.chat.domain.ChatPhoto;
import com.spingular.chat.repository.ChatPhotoRepository;
import com.spingular.chat.service.dto.ChatPhotoDTO;
import com.spingular.chat.service.mapper.ChatPhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link ChatPhoto}.
 */
@Service
@Transactional
public class ChatPhotoServiceImpl implements ChatPhotoService {

    private final Logger log = LoggerFactory.getLogger(ChatPhotoServiceImpl.class);

    private final ChatPhotoRepository chatPhotoRepository;

    private final ChatPhotoMapper chatPhotoMapper;

    public ChatPhotoServiceImpl(ChatPhotoRepository chatPhotoRepository, ChatPhotoMapper chatPhotoMapper) {
        this.chatPhotoRepository = chatPhotoRepository;
        this.chatPhotoMapper = chatPhotoMapper;
    }

    /**
     * Save a chatPhoto.
     *
     * @param chatPhotoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChatPhotoDTO save(ChatPhotoDTO chatPhotoDTO) {
        log.debug("Request to save ChatPhoto : {}", chatPhotoDTO);
        ChatPhoto chatPhoto = chatPhotoMapper.toEntity(chatPhotoDTO);
        chatPhoto = chatPhotoRepository.save(chatPhoto);
        return chatPhotoMapper.toDto(chatPhoto);
    }

    /**
     * Get all the chatPhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatPhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatPhotos");
        return chatPhotoRepository.findAll(pageable)
            .map(chatPhotoMapper::toDto);
    }



    /**
    *  Get all the chatPhotos where ChatUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ChatPhotoDTO> findAllWhereChatUserIsNull() {
        log.debug("Request to get all chatPhotos where ChatUser is null");
        return StreamSupport
            .stream(chatPhotoRepository.findAll().spliterator(), false)
            .filter(chatPhoto -> chatPhoto.getChatUser() == null)
            .map(chatPhotoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one chatPhoto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatPhotoDTO> findOne(Long id) {
        log.debug("Request to get ChatPhoto : {}", id);
        return chatPhotoRepository.findById(id)
            .map(chatPhotoMapper::toDto);
    }

    /**
     * Delete the chatPhoto by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatPhoto : {}", id);
        chatPhotoRepository.deleteById(id);
    }
}
