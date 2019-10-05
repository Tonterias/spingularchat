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

import com.spingular.chat.domain.ChatPhoto;
import com.spingular.chat.domain.*; // for static metamodels
import com.spingular.chat.repository.ChatPhotoRepository;
import com.spingular.chat.service.dto.ChatPhotoCriteria;
import com.spingular.chat.service.dto.ChatPhotoDTO;
import com.spingular.chat.service.mapper.ChatPhotoMapper;

/**
 * Service for executing complex queries for {@link ChatPhoto} entities in the database.
 * The main input is a {@link ChatPhotoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatPhotoDTO} or a {@link Page} of {@link ChatPhotoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatPhotoQueryService extends QueryService<ChatPhoto> {

    private final Logger log = LoggerFactory.getLogger(ChatPhotoQueryService.class);

    private final ChatPhotoRepository chatPhotoRepository;

    private final ChatPhotoMapper chatPhotoMapper;

    public ChatPhotoQueryService(ChatPhotoRepository chatPhotoRepository, ChatPhotoMapper chatPhotoMapper) {
        this.chatPhotoRepository = chatPhotoRepository;
        this.chatPhotoMapper = chatPhotoMapper;
    }

    /**
     * Return a {@link List} of {@link ChatPhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatPhotoDTO> findByCriteria(ChatPhotoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatPhoto> specification = createSpecification(criteria);
        return chatPhotoMapper.toDto(chatPhotoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatPhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatPhotoDTO> findByCriteria(ChatPhotoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatPhoto> specification = createSpecification(criteria);
        return chatPhotoRepository.findAll(specification, page)
            .map(chatPhotoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatPhotoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatPhoto> specification = createSpecification(criteria);
        return chatPhotoRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatPhotoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatPhoto> createSpecification(ChatPhotoCriteria criteria) {
        Specification<ChatPhoto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatPhoto_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ChatPhoto_.creationDate));
            }
            if (criteria.getChatUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatUserId(),
                    root -> root.join(ChatPhoto_.chatUser, JoinType.LEFT).get(ChatUser_.id)));
            }
        }
        return specification;
    }
}
