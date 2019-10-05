package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatInvitationService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatInvitationDTO;
import com.spingular.chat.service.dto.ChatInvitationCriteria;
import com.spingular.chat.service.ChatInvitationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.spingular.chat.domain.ChatInvitation}.
 */
@RestController
@RequestMapping("/api")
public class ChatInvitationResource {

    private final Logger log = LoggerFactory.getLogger(ChatInvitationResource.class);

    private static final String ENTITY_NAME = "chatInvitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatInvitationService chatInvitationService;

    private final ChatInvitationQueryService chatInvitationQueryService;

    public ChatInvitationResource(ChatInvitationService chatInvitationService, ChatInvitationQueryService chatInvitationQueryService) {
        this.chatInvitationService = chatInvitationService;
        this.chatInvitationQueryService = chatInvitationQueryService;
    }

    /**
     * {@code POST  /chat-invitations} : Create a new chatInvitation.
     *
     * @param chatInvitationDTO the chatInvitationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatInvitationDTO, or with status {@code 400 (Bad Request)} if the chatInvitation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-invitations")
    public ResponseEntity<ChatInvitationDTO> createChatInvitation(@RequestBody ChatInvitationDTO chatInvitationDTO) throws URISyntaxException {
        log.debug("REST request to save ChatInvitation : {}", chatInvitationDTO);
        if (chatInvitationDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatInvitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatInvitationDTO result = chatInvitationService.save(chatInvitationDTO);
        return ResponseEntity.created(new URI("/api/chat-invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-invitations} : Updates an existing chatInvitation.
     *
     * @param chatInvitationDTO the chatInvitationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatInvitationDTO,
     * or with status {@code 400 (Bad Request)} if the chatInvitationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatInvitationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-invitations")
    public ResponseEntity<ChatInvitationDTO> updateChatInvitation(@RequestBody ChatInvitationDTO chatInvitationDTO) throws URISyntaxException {
        log.debug("REST request to update ChatInvitation : {}", chatInvitationDTO);
        if (chatInvitationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatInvitationDTO result = chatInvitationService.save(chatInvitationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatInvitationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-invitations} : get all the chatInvitations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatInvitations in body.
     */
    @GetMapping("/chat-invitations")
    public ResponseEntity<List<ChatInvitationDTO>> getAllChatInvitations(ChatInvitationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatInvitations by criteria: {}", criteria);
        Page<ChatInvitationDTO> page = chatInvitationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-invitations/count} : count all the chatInvitations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-invitations/count")
    public ResponseEntity<Long> countChatInvitations(ChatInvitationCriteria criteria) {
        log.debug("REST request to count ChatInvitations by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatInvitationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-invitations/:id} : get the "id" chatInvitation.
     *
     * @param id the id of the chatInvitationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatInvitationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-invitations/{id}")
    public ResponseEntity<ChatInvitationDTO> getChatInvitation(@PathVariable Long id) {
        log.debug("REST request to get ChatInvitation : {}", id);
        Optional<ChatInvitationDTO> chatInvitationDTO = chatInvitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatInvitationDTO);
    }

    /**
     * {@code DELETE  /chat-invitations/:id} : delete the "id" chatInvitation.
     *
     * @param id the id of the chatInvitationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-invitations/{id}")
    public ResponseEntity<Void> deleteChatInvitation(@PathVariable Long id) {
        log.debug("REST request to delete ChatInvitation : {}", id);
        chatInvitationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
