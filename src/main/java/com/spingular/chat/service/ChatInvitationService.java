package com.spingular.chat.service;

import com.spingular.chat.service.dto.ChatInvitationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.chat.domain.ChatInvitation}.
 */
public interface ChatInvitationService {

    /**
     * Save a chatInvitation.
     *
     * @param chatInvitationDTO the entity to save.
     * @return the persisted entity.
     */
    ChatInvitationDTO save(ChatInvitationDTO chatInvitationDTO);

    /**
     * Get all the chatInvitations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChatInvitationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" chatInvitation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatInvitationDTO> findOne(Long id);

    /**
     * Delete the "id" chatInvitation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
