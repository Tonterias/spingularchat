package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatPhotoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatPhoto}.
 */
public interface ChatPhotoService {

    /**
     * Save a chatPhoto.
     *
     * @param chatPhotoDTO the entity to save.
     * @return the persisted entity.
     */
    ChatPhotoDTO save(ChatPhotoDTO chatPhotoDTO);

    /**
     * Get all the chatPhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatPhotoDTO> findAll(Pageable pageable);
    /**
     * Get all the ChatPhotoDTO where ChatUser is {@code null}.
     *
     * @return the list of entities.
     */
    List<ChatPhotoDTO> findAllWhereChatUserIsNull();


    /**
     * Get the "id" chatPhoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatPhotoDTO> findOne(Long id);

    /**
     * Delete the "id" chatPhoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
