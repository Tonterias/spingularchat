package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatUser}.
 */
public interface ChatUserService {

    /**
     * Save a chatUser.
     *
     * @param chatUserDTO the entity to save.
     * @return the persisted entity.
     */
    ChatUserDTO save(ChatUserDTO chatUserDTO);

    /**
     * Get all the chatUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" chatUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatUserDTO> findOne(Long id);

    /**
     * Delete the "id" chatUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
