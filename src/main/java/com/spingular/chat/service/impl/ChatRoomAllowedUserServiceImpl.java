package com.spingular.chat.service.impl;

import com.spingular.chat.service.ChatRoomAllowedUserService;
import com.spingular.chat.domain.ChatRoomAllowedUser;
import com.spingular.chat.repository.ChatRoomAllowedUserRepository;
import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;
import com.spingular.chat.service.mapper.ChatRoomAllowedUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChatRoomAllowedUser}.
 */
@Service
@Transactional
public class ChatRoomAllowedUserServiceImpl implements ChatRoomAllowedUserService {

    private final Logger log = LoggerFactory.getLogger(ChatRoomAllowedUserServiceImpl.class);

    private final ChatRoomAllowedUserRepository chatRoomAllowedUserRepository;

    private final ChatRoomAllowedUserMapper chatRoomAllowedUserMapper;

    public ChatRoomAllowedUserServiceImpl(ChatRoomAllowedUserRepository chatRoomAllowedUserRepository, ChatRoomAllowedUserMapper chatRoomAllowedUserMapper) {
        this.chatRoomAllowedUserRepository = chatRoomAllowedUserRepository;
        this.chatRoomAllowedUserMapper = chatRoomAllowedUserMapper;
    }

    /**
     * Save a chatRoomAllowedUser.
     *
     * @param chatRoomAllowedUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChatRoomAllowedUserDTO save(ChatRoomAllowedUserDTO chatRoomAllowedUserDTO) {
        log.debug("Request to save ChatRoomAllowedUser : {}", chatRoomAllowedUserDTO);
        ChatRoomAllowedUser chatRoomAllowedUser = chatRoomAllowedUserMapper.toEntity(chatRoomAllowedUserDTO);
        chatRoomAllowedUser = chatRoomAllowedUserRepository.save(chatRoomAllowedUser);
        return chatRoomAllowedUserMapper.toDto(chatRoomAllowedUser);
    }

    /**
     * Get all the chatRoomAllowedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomAllowedUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatRoomAllowedUsers");
        return chatRoomAllowedUserRepository.findAll(pageable)
            .map(chatRoomAllowedUserMapper::toDto);
    }


    /**
     * Get one chatRoomAllowedUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatRoomAllowedUserDTO> findOne(Long id) {
        log.debug("Request to get ChatRoomAllowedUser : {}", id);
        return chatRoomAllowedUserRepository.findById(id)
            .map(chatRoomAllowedUserMapper::toDto);
    }

    /**
     * Delete the chatRoomAllowedUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatRoomAllowedUser : {}", id);
        chatRoomAllowedUserRepository.deleteById(id);
    }
}
