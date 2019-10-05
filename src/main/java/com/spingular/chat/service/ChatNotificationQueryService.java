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

import com.spingular.chat.domain.ChatNotification;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatNotificationRepository;
import com.spingular.chat.service.dto.ChatNotificationCriteria;
import com.spingular.chat.service.dto.ChatNotificationDTO;
import com.spingular.chat.service.mapper.ChatNotificationMapper;

/**
 * Service for executing complex queries for {@link ChatNotification} entities in the database.
 * The main input is a {@link ChatNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatNotificationDTO} or a {@link Page} of {@link ChatNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatNotificationQueryService extends QueryService<ChatNotification> {

    private final Logger log = LoggerFactory.getLogger(ChatNotificationQueryService.class);

    private final ChatNotificationRepository chatNotificationRepository;

    private final ChatNotificationMapper chatNotificationMapper;

    public ChatNotificationQueryService(ChatNotificationRepository chatNotificationRepository, ChatNotificationMapper chatNotificationMapper) {
        this.chatNotificationRepository = chatNotificationRepository;
        this.chatNotificationMapper = chatNotificationMapper;
    }

    /**
     * Return a {@link List} of {@link ChatNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatNotificationDTO> findByCriteria(ChatNotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatNotification> specification = createSpecification(criteria);
        return chatNotificationMapper.toDto(chatNotificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatNotificationDTO> findByCriteria(ChatNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatNotification> specification = createSpecification(criteria);
        return chatNotificationRepository.findAll(specification, page)
            .map(chatNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatNotification> specification = createSpecification(criteria);
        return chatNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatNotification> createSpecification(ChatNotificationCriteria criteria) {
        Specification<ChatNotification> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatNotification_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatNotification_.creationDate));
            }
            if (criteria.getChatNotificationReason() != null) {
                specification = specification.and(buildSpecification(criteria.getChatNotificationReason(), ChatNotification_.chatNotificationReason));
            }
            if (criteria.getChatUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatUserId(),
                    root -> root.join(ChatNotification_.chatUser, JoinType.LEFT).get(ChatUser_.id)));
            }
            if (criteria.getChatInvitationId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatInvitationId(),
                    root -> root.join(ChatNotification_.chatInvitations, JoinType.LEFT).get(ChatInvitation_.id)));
            }
            if (criteria.getChatRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomId(),
                    root -> root.join(ChatNotification_.chatRoom, JoinType.LEFT).get(ChatRoom_.id)));
            }
            if (criteria.getChatMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatMessageId(),
                    root -> root.join(ChatNotification_.chatMessage, JoinType.LEFT).get(ChatMessage_.id)));
            }
        }
        return specification;
    }
}
