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

import com.spingular.chat.domain.ChatRoomAllowedUser;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatRoomAllowedUserRepository;
import com.spingular.chat.service.dto.ChatRoomAllowedUserCriteria;
import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;
import com.spingular.chat.service.mapper.ChatRoomAllowedUserMapper;

/**
 * Service for executing complex queries for {@link ChatRoomAllowedUser} entities in the database.
 * The main input is a {@link ChatRoomAllowedUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatRoomAllowedUserDTO} or a {@link Page} of {@link ChatRoomAllowedUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatRoomAllowedUserQueryService extends QueryService<ChatRoomAllowedUser> {

    private final Logger log = LoggerFactory.getLogger(ChatRoomAllowedUserQueryService.class);

    private final ChatRoomAllowedUserRepository chatRoomAllowedUserRepository;

    private final ChatRoomAllowedUserMapper chatRoomAllowedUserMapper;

    public ChatRoomAllowedUserQueryService(ChatRoomAllowedUserRepository chatRoomAllowedUserRepository, ChatRoomAllowedUserMapper chatRoomAllowedUserMapper) {
        this.chatRoomAllowedUserRepository = chatRoomAllowedUserRepository;
        this.chatRoomAllowedUserMapper = chatRoomAllowedUserMapper;
    }

    /**
     * Return a {@link List} of {@link ChatRoomAllowedUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatRoomAllowedUserDTO> findByCriteria(ChatRoomAllowedUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatRoomAllowedUser> specification = createSpecification(criteria);
        return chatRoomAllowedUserMapper.toDto(chatRoomAllowedUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatRoomAllowedUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatRoomAllowedUserDTO> findByCriteria(ChatRoomAllowedUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatRoomAllowedUser> specification = createSpecification(criteria);
        return chatRoomAllowedUserRepository.findAll(specification, page)
            .map(chatRoomAllowedUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatRoomAllowedUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatRoomAllowedUser> specification = createSpecification(criteria);
        return chatRoomAllowedUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatRoomAllowedUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatRoomAllowedUser> createSpecification(ChatRoomAllowedUserCriteria criteria) {
        Specification<ChatRoomAllowedUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatRoomAllowedUser_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatRoomAllowedUser_.creationDate));
            }
            if (criteria.getBannedUser() != null) {
                specification = specification.and(buildSpecification(criteria.getBannedUser(), ChatRoomAllowedUser_.bannedUser));
            }
            if (criteria.getBannedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBannedDate(), ChatRoomAllowedUser_.bannedDate));
            }
            if (criteria.getChatRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatRoomId(),
                    root -> root.join(ChatRoomAllowedUser_.chatRoom, JoinType.LEFT).get(ChatRoom_.id)));
            }
            if (criteria.getChatUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatUserId(),
                    root -> root.join(ChatRoomAllowedUser_.chatUser, JoinType.LEFT).get(ChatUser_.id)));
            }
        }
        return specification;
    }
}
