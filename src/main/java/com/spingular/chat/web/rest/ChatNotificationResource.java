package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatNotificationService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatNotificationDTO;
import com.spingular.chat.service.dto.ChatNotificationCriteria;
import com.spingular.chat.service.ChatNotificationQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.spingular.chat.domain.ChatNotification}.
 */
@RestController
@RequestMapping("/api")
public class ChatNotificationResource {

    private final Logger log = LoggerFactory.getLogger(ChatNotificationResource.class);

    private static final String ENTITY_NAME = "chatNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatNotificationService chatNotificationService;

    private final ChatNotificationQueryService chatNotificationQueryService;

    public ChatNotificationResource(ChatNotificationService chatNotificationService, ChatNotificationQueryService chatNotificationQueryService) {
        this.chatNotificationService = chatNotificationService;
        this.chatNotificationQueryService = chatNotificationQueryService;
    }

    /**
     * {@code POST  /chat-notifications} : Create a new chatNotification.
     *
     * @param chatNotificationDTO the chatNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatNotificationDTO, or with status {@code 400 (Bad Request)} if the chatNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-notifications")
    public ResponseEntity<ChatNotificationDTO> createChatNotification(@Valid @RequestBody ChatNotificationDTO chatNotificationDTO) throws URISyntaxException {
        log.debug("REST request to save ChatNotification : {}", chatNotificationDTO);
        if (chatNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatNotificationDTO result = chatNotificationService.save(chatNotificationDTO);
        return ResponseEntity.created(new URI("/api/chat-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-notifications} : Updates an existing chatNotification.
     *
     * @param chatNotificationDTO the chatNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the chatNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-notifications")
    public ResponseEntity<ChatNotificationDTO> updateChatNotification(@Valid @RequestBody ChatNotificationDTO chatNotificationDTO) throws URISyntaxException {
        log.debug("REST request to update ChatNotification : {}", chatNotificationDTO);
        if (chatNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatNotificationDTO result = chatNotificationService.save(chatNotificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-notifications} : get all the chatNotifications.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatNotifications in body.
     */
    @GetMapping("/chat-notifications")
    public ResponseEntity<List<ChatNotificationDTO>> getAllChatNotifications(ChatNotificationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatNotifications by criteria: {}", criteria);
        Page<ChatNotificationDTO> page = chatNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-notifications/count} : count all the chatNotifications.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-notifications/count")
    public ResponseEntity<Long> countChatNotifications(ChatNotificationCriteria criteria) {
        log.debug("REST request to count ChatNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-notifications/:id} : get the "id" chatNotification.
     *
     * @param id the id of the chatNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-notifications/{id}")
    public ResponseEntity<ChatNotificationDTO> getChatNotification(@PathVariable Long id) {
        log.debug("REST request to get ChatNotification : {}", id);
        Optional<ChatNotificationDTO> chatNotificationDTO = chatNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatNotificationDTO);
    }

    /**
     * {@code DELETE  /chat-notifications/:id} : delete the "id" chatNotification.
     *
     * @param id the id of the chatNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-notifications/{id}")
    public ResponseEntity<Void> deleteChatNotification(@PathVariable Long id) {
        log.debug("REST request to delete ChatNotification : {}", id);
        chatNotificationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
