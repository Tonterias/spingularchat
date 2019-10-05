package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatRoomAllowedUser}.
 */
public interface ChatRoomAllowedUserService {

    /**
     * Save a chatRoomAllowedUser.
     *
     * @param chatRoomAllowedUserDTO the entity to save.
     * @return the persisted entity.
     */
    ChatRoomAllowedUserDTO save(ChatRoomAllowedUserDTO chatRoomAllowedUserDTO);

    /**
     * Get all the chatRoomAllowedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatRoomAllowedUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" chatRoomAllowedUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatRoomAllowedUserDTO> findOne(Long id);

    /**
     * Delete the "id" chatRoomAllowedUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
