package com.spingular.chat.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.spingular.chat.domain.ChatUser;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatUserRepository;
import com.spingular.chat.service.dto.ChatUserCriteria;
import com.spingular.chat.service.dto.ChatUserDTO;
import com.spingular.chat.service.mapper.ChatUserMapper;

/**
 * Service for executing complex queries for {@link ChatUser} entities in the database.
 * The main input is a {@link ChatUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatUserDTO} or a {@link Page} of {@link ChatUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatUserQueryService extends QueryService<ChatUser> {

    private final Logger log = LoggerFactory.getLogger(ChatUserQueryService.class);

    private final ChatUserRepository chatUserRepository;

    private final ChatUserMapper chatUserMapper;

    public ChatUserQueryService(ChatUserRepository chatUserRepository, ChatUserMapper chatUserMapper) {
        this.chatUserRepository = chatUserRepository;
        this.chatUserMapper = chatUserMapper;
    }

    /**
     * Return a {@link List} of {@link ChatUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatUserDTO> findByCriteria(ChatUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatUser> specification = createSpecification(criteria);
        return chatUserMapper.toDto(chatUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatUserDTO> findByCriteria(ChatUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatUser> specification = createSpecification(criteria);
        return chatUserRepository.findAll(specification, page)
            .map(chatUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatUser> specification = createSpecification(criteria);
        return chatUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatUser> createSpecification(ChatUserCriteria criteria) {
        Specification<ChatUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatUser_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatUser_.creationDate));
            }
            if (criteria.getBannedUser() != null) {
                specification = specification.and(buildSpecification(criteria.getBannedUser(), ChatUser_.bannedUser));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(ChatUser_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getChatPhotoId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatPhotoId(),
                    root -> root.join(ChatUser_.chatPhoto, JoinType.LEFT).get(ChatPhoto_.id)));
            }
            if (criteria.getChatRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomId(),
                    root -> root.join(ChatUser_.chatRooms, JoinType.LEFT).get(ChatRoom_.id)));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(),
                    root -> root.join(ChatUser_.senders, JoinType.LEFT).get(ChatInvitation_.id)));
            }
            if (criteria.getReceiverId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceiverId(),
                    root -> root.join(ChatUser_.receivers, JoinType.LEFT).get(ChatInvitation_.id)));
            }
            if (criteria.getChatMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatMessageId(),
                    root -> root.join(ChatUser_.chatMessages, JoinType.LEFT).get(ChatMessage_.id)));
            }
            if (criteria.getChatRoomAllowedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomAllowedUserId(),
                    root -> root.join(ChatUser_.chatRoomAllowedUsers, JoinType.LEFT).get(ChatRoomAllowedUser_.id)));
            }
            if (criteria.getChatOffensiveMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatOffensiveMessageId(),
                    root -> root.join(ChatUser_.chatOffensiveMessages, JoinType.LEFT).get(ChatOffensiveMessage_.id)));
            }
            if (criteria.getChatNotificationId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatNotificationId(),
                    root -> root.join(ChatUser_.chatNotifications, JoinType.LEFT).get(ChatNotification_.id)));
            }
        }
        return specification;
    }
}
