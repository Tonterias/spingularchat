package com.spingular.chat.service.impl;

import com.spingular.chat.service.ChatInvitationService;
import com.spingular.chat.domain.ChatInvitation;
import com.spingular.chat.repository.ChatInvitationRepository;
import com.spingular.chat.service.dto.ChatInvitationDTO;
import com.spingular.chat.service.mapper.ChatInvitationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChatInvitation}.
 */
@Service
@Transactional
public class ChatInvitationServiceImpl implements ChatInvitationService {

    private final Logger log = LoggerFactory.getLogger(ChatInvitationServiceImpl.class);

    private final ChatInvitationRepository chatInvitationRepository;

    private final ChatInvitationMapper chatInvitationMapper;

    public ChatInvitationServiceImpl(ChatInvitationRepository chatInvitationRepository, ChatInvitationMapper chatInvitationMapper) {
        this.chatInvitationRepository = chatInvitationRepository;
        this.chatInvitationMapper = chatInvitationMapper;
    }

    /**
     * Save a chatInvitation.
     *
     * @param chatInvitationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChatInvitationDTO save(ChatInvitationDTO chatInvitationDTO) {
        log.debug("Request to save ChatInvitation : {}", chatInvitationDTO);
        ChatInvitation chatInvitation = chatInvitationMapper.toEntity(chatInvitationDTO);
        chatInvitation = chatInvitationRepository.save(chatInvitation);
        return chatInvitationMapper.toDto(chatInvitation);
    }

    /**
     * Get all the chatInvitations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatInvitationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatInvitations");
        return chatInvitationRepository.findAll(pageable)
            .map(chatInvitationMapper::toDto);
    }


    /**
     * Get one chatInvitation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatInvitationDTO> findOne(Long id) {
        log.debug("Request to get ChatInvitation : {}", id);
        return chatInvitationRepository.findById(id)
            .map(chatInvitationMapper::toDto);
    }

    /**
     * Delete the chatInvitation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatInvitation : {}", id);
        chatInvitationRepository.deleteById(id);
    }
}
