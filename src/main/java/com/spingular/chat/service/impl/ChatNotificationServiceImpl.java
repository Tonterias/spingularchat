package com.spingular.chat.service.impl;

import com.spingular.chat.service.ChatNotificationService;
import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.repository.ChatNotificationRepository;
import com.spingular.chat.service.dto.ChatNotificationDTO;
import com.spingular.chat.service.mapper.ChatNotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChatNotification}.
 */
@Service
@Transactional
public class ChatNotificationServiceImpl implements ChatNotificationService {

    private final Logger log = LoggerFactory.getLogger(ChatNotificationServiceImpl.class);

    private final ChatNotificationRepository chatNotificationRepository;

    private final ChatNotificationMapper chatNotificationMapper;

    public ChatNotificationServiceImpl(ChatNotificationRepository chatNotificationRepository, ChatNotificationMapper chatNotificationMapper) {
        this.chatNotificationRepository = chatNotificationRepository;
        this.chatNotificationMapper = chatNotificationMapper;
    }

    /**
     * Save a chatNotification.
     *
     * @param chatNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChatNotificationDTO save(ChatNotificationDTO chatNotificationDTO) {
        log.debug("Request to save ChatNotification : {}", chatNotificationDTO);
        ChatNotification chatNotification = chatNotificationMapper.toEntity(chatNotificationDTO);
        chatNotification = chatNotificationRepository.save(chatNotification);
        return chatNotificationMapper.toDto(chatNotification);
    }

    /**
     * Get all the chatNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatNotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatNotifications");
        return chatNotificationRepository.findAll(pageable)
            .map(chatNotificationMapper::toDto);
    }

    /**
     * Get all the chatNotifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ChatNotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return chatNotificationRepository.findAllWithEagerRelationships(pageable).map(chatNotificationMapper::toDto);
    }
    

    /**
     * Get one chatNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatNotificationDTO> findOne(Long id) {
        log.debug("Request to get ChatNotification : {}", id);
        return chatNotificationRepository.findOneWithEagerRelationships(id)
            .map(chatNotificationMapper::toDto);
    }

    /**
     * Delete the chatNotification by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatNotification : {}", id);
        chatNotificationRepository.deleteById(id);
    }
}
