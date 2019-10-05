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

import com.spingular.chat.domain.ChatRoom;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatRoomRepository;
import com.spingular.chat.service.dto.ChatRoomCriteria;
import com.spingular.chat.service.dto.ChatRoomDTO;
import com.spingular.chat.service.mapper.ChatRoomMapper;

/**
 * Service for executing complex queries for {@link ChatRoom} entities in the database.
 * The main input is a {@link ChatRoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatRoomDTO} or a {@link Page} of {@link ChatRoomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatRoomQueryService extends QueryService<ChatRoom> {

    private final Logger log = LoggerFactory.getLogger(ChatRoomQueryService.class);

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomQueryService(ChatRoomRepository chatRoomRepository, ChatRoomMapper chatRoomMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMapper = chatRoomMapper;
    }

    /**
     * Return a {@link List} of {@link ChatRoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatRoomDTO> findByCriteria(ChatRoomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatRoom> specification = createSpecification(criteria);
        return chatRoomMapper.toDto(chatRoomRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatRoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatRoomDTO> findByCriteria(ChatRoomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatRoom> specification = createSpecification(criteria);
        return chatRoomRepository.findAll(specification, page)
            .map(chatRoomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatRoomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatRoom> specification = createSpecification(criteria);
        return chatRoomRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatRoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatRoom> createSpecification(ChatRoomCriteria criteria) {
        Specification<ChatRoom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatRoom_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatRoom_.creationDate));
            }
            if (criteria.getRoomName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomName(), ChatRoom_.roomName));
            }
            if (criteria.getRoomDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomDescription(), ChatRoom_.roomDescription));
            }
            if (criteria.getPrivateRoom() != null) {
                specification = specification.and(buildSpecification(criteria.getPrivateRoom(), ChatRoom_.privateRoom));
            }
            if (criteria.getChatMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatMessageId(),
                    root -> root.join(ChatRoom_.chatMessages, JoinType.LEFT).get(ChatMessage_.id)));
            }
            if (criteria.getChatRoomAllowedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomAllowedUserId(),
                    root -> root.join(ChatRoom_.chatRoomAllowedUsers, JoinType.LEFT).get(ChatRoomAllowedUser_.id)));
            }
            if (criteria.getChatNotificationId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatNotificationId(),
                    root -> root.join(ChatRoom_.chatNotifications, JoinType.LEFT).get(ChatNotification_.id)));
            }
            if (criteria.getChatInvitationId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatInvitationId(),
                    root -> root.join(ChatRoom_.chatInvitations, JoinType.LEFT).get(ChatInvitation_.id)));
            }
            if (criteria.getChatUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatUserId(),
                    root -> root.join(ChatRoom_.chatUser, JoinType.LEFT).get(ChatUser_.id)));
            }
        }
        return specification;
    }
}
