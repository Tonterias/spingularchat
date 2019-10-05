package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatRoomDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatRoom}.
 */
public interface ChatRoomService {

    /**
     * Save a chatRoom.
     *
     * @param chatRoomDTO the entity to save.
     * @return the persisted entity.
     */
    ChatRoomDTO save(ChatRoomDTO chatRoomDTO);

    /**
     * Get all the chatRooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatRoomDTO> findAll(Pageable pageable);


    /**
     * Get the "id" chatRoom.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatRoomDTO> findOne(Long id);

    /**
     * Delete the "id" chatRoom.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
