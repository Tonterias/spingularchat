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

import com.spingular.chat.domain.ChatInvitation;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatInvitationRepository;
import com.spingular.chat.service.dto.ChatInvitationCriteria;
import com.spingular.chat.service.dto.ChatInvitationDTO;
import com.spingular.chat.service.mapper.ChatInvitationMapper;

/**
 * Service for executing complex queries for {@link ChatInvitation} entities in the database.
 * The main input is a {@link ChatInvitationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatInvitationDTO} or a {@link Page} of {@link ChatInvitationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatInvitationQueryService extends QueryService<ChatInvitation> {

    private final Logger log = LoggerFactory.getLogger(ChatInvitationQueryService.class);

    private final ChatInvitationRepository chatInvitationRepository;

    private final ChatInvitationMapper chatInvitationMapper;

    public ChatInvitationQueryService(ChatInvitationRepository chatInvitationRepository, ChatInvitationMapper chatInvitationMapper) {
        this.chatInvitationRepository = chatInvitationRepository;
        this.chatInvitationMapper = chatInvitationMapper;
    }

    /**
     * Return a {@link List} of {@link ChatInvitationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatInvitationDTO> findByCriteria(ChatInvitationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatInvitation> specification = createSpecification(criteria);
        return chatInvitationMapper.toDto(chatInvitationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatInvitationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatInvitationDTO> findByCriteria(ChatInvitationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatInvitation> specification = createSpecification(criteria);
        return chatInvitationRepository.findAll(specification, page)
            .map(chatInvitationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatInvitationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatInvitation> specification = createSpecification(criteria);
        return chatInvitationRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatInvitationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatInvitation> createSpecification(ChatInvitationCriteria criteria) {
        Specification<ChatInvitation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatInvitation_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatInvitation_.creationDate));
            }
            if (criteria.getAcceptance() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceptance(), ChatInvitation_.acceptance));
            }
            if (criteria.getDenial() != null) {
                specification = specification.and(buildSpecification(criteria.getDenial(), ChatInvitation_.denial));
            }
            if (criteria.getAcceptanceDenialDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcceptanceDenialDate(), ChatInvitation_.acceptanceDenialDate));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(),
                    root -> root.join(ChatInvitation_.sender, JoinType.LEFT).get(ChatUser_.id)));
            }
            if (criteria.getReceiverId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceiverId(),
                    root -> root.join(ChatInvitation_.receiver, JoinType.LEFT).get(ChatUser_.id)));
            }
            if (criteria.getChatRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomId(),
                    root -> root.join(ChatInvitation_.chatRoom, JoinType.LEFT).get(ChatRoom_.id)));
            }
            if (criteria.getChatNotificationId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatNotificationId(),
                    root -> root.join(ChatInvitation_.chatNotifications, JoinType.LEFT).get(ChatNotification_.id)));
            }
        }
        return specification;
    }
}
