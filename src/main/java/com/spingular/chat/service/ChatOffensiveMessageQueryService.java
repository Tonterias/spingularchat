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

import com.spingular.chat.domain.ChatOffensiveMessage;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatOffensiveMessageRepository;
import com.spingular.chat.service.dto.ChatOffensiveMessageCriteria;
import com.spingular.chat.service.dto.ChatOffensiveMessageDTO;
import com.spingular.chat.service.mapper.ChatOffensiveMessageMapper;

/**
 * Service for executing complex queries for {@link ChatOffensiveMessage} entities in the database.
 * The main input is a {@link ChatOffensiveMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatOffensiveMessageDTO} or a {@link Page} of {@link ChatOffensiveMessageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatOffensiveMessageQueryService extends QueryService<ChatOffensiveMessage> {

    private final Logger log = LoggerFactory.getLogger(ChatOffensiveMessageQueryService.class);

    private final ChatOffensiveMessageRepository chatOffensiveMessageRepository;

    private final ChatOffensiveMessageMapper chatOffensiveMessageMapper;

    public ChatOffensiveMessageQueryService(ChatOffensiveMessageRepository chatOffensiveMessageRepository, ChatOffensiveMessageMapper chatOffensiveMessageMapper) {
        this.chatOffensiveMessageRepository = chatOffensiveMessageRepository;
        this.chatOffensiveMessageMapper = chatOffensiveMessageMapper;
    }

    /**
     * Return a {@link List} of {@link ChatOffensiveMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatOffensiveMessageDTO> findByCriteria(ChatOffensiveMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatOffensiveMessage> specification = createSpecification(criteria);
        return chatOffensiveMessageMapper.toDto(chatOffensiveMessageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatOffensiveMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatOffensiveMessageDTO> findByCriteria(ChatOffensiveMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatOffensiveMessage> specification = createSpecification(criteria);
        return chatOffensiveMessageRepository.findAll(specification, page)
            .map(chatOffensiveMessageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatOffensiveMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatOffensiveMessage> specification = createSpecification(criteria);
        return chatOffensiveMessageRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatOffensiveMessageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatOffensiveMessage> createSpecification(ChatOffensiveMessageCriteria criteria) {
        Specification<ChatOffensiveMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatOffensiveMessage_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatOffensiveMessage_.creationDate));
            }
            if (criteria.getIsOffensive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsOffensive(), ChatOffensiveMessage_.isOffensive));
            }
            if (criteria.getChatUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatUserId(),
                    root -> root.join(ChatOffensiveMessage_.chatUser, JoinType.LEFT).get(ChatUser_.id)));
            }
            if (criteria.getChatMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatMessageId(),
                    root -> root.join(ChatOffensiveMessage_.chatMessage, JoinType.LEFT).get(ChatMessage_.id)));
            }
        }
        return specification;
    }
}
