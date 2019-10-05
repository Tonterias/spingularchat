package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatNotificationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatNotification}.
 */
public interface ChatNotificationService {

    /**
     * Save a chatNotification.
     *
     * @param chatNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    ChatNotificationDTO save(ChatNotificationDTO chatNotificationDTO);

    /**
     * Get all the chatNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatNotificationDTO> findAll(Pageable pageable);

    /**
     * Get all the chatNotifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ChatNotificationDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" chatNotification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatNotificationDTO> findOne(Long id);

    /**
     * Delete the "id" chatNotification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
