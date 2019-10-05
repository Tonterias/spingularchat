package com.spingular.chat.service.impl;

import com.spingular.chat.service.ChatOffensiveMessageService;
import com.spingular.chat.domain.ChatOffensiveMessage;
import com.spingular.chat.repository.ChatOffensiveMessageRepository;
import com.spingular.chat.service.dto.ChatOffensiveMessageDTO;
import com.spingular.chat.service.mapper.ChatOffensiveMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChatOffensiveMessage}.
 */
@Service
@Transactional
public class ChatOffensiveMessageServiceImpl implements ChatOffensiveMessageService {

    private final Logger log = LoggerFactory.getLogger(ChatOffensiveMessageServiceImpl.class);

    private final ChatOffensiveMessageRepository chatOffensiveMessageRepository;

    private final ChatOffensiveMessageMapper chatOffensiveMessageMapper;

    public ChatOffensiveMessageServiceImpl(ChatOffensiveMessageRepository chatOffensiveMessageRepository, ChatOffensiveMessageMapper chatOffensiveMessageMapper) {
        this.chatOffensiveMessageRepository = chatOffensiveMessageRepository;
        this.chatOffensiveMessageMapper = chatOffensiveMessageMapper;
    }

    /**
     * Save a chatOffensiveMessage.
     *
     * @param chatOffensiveMessageDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChatOffensiveMessageDTO save(ChatOffensiveMessageDTO chatOffensiveMessageDTO) {
        log.debug("Request to save ChatOffensiveMessage : {}", chatOffensiveMessageDTO);
        ChatOffensiveMessage chatOffensiveMessage = chatOffensiveMessageMapper.toEntity(chatOffensiveMessageDTO);
        chatOffensiveMessage = chatOffensiveMessageRepository.save(chatOffensiveMessage);
        return chatOffensiveMessageMapper.toDto(chatOffensiveMessage);
    }

    /**
     * Get all the chatOffensiveMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatOffensiveMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatOffensiveMessages");
        return chatOffensiveMessageRepository.findAll(pageable)
            .map(chatOffensiveMessageMapper::toDto);
    }


    /**
     * Get one chatOffensiveMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatOffensiveMessageDTO> findOne(Long id) {
        log.debug("Request to get ChatOffensiveMessage : {}", id);
        return chatOffensiveMessageRepository.findById(id)
            .map(chatOffensiveMessageMapper::toDto);
    }

    /**
     * Delete the chatOffensiveMessage by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatOffensiveMessage : {}", id);
        chatOffensiveMessageRepository.deleteById(id);
    }
}
