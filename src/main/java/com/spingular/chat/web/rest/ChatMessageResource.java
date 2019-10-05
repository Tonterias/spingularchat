package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatMessageService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatMessageDTO;
import com.spingular.chat.service.dto.ChatMessageCriteria;
import com.spingular.chat.service.ChatMessageQueryService;

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
 * REST controller for managing {@link com.spingular.chat.domain.ChatMessage}.
 */
@RestController
@RequestMapping("/api")
public class ChatMessageResource {

    private final Logger log = LoggerFactory.getLogger(ChatMessageResource.class);

    private static final String ENTITY_NAME = "chatMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatMessageService chatMessageService;

    private final ChatMessageQueryService chatMessageQueryService;

    public ChatMessageResource(ChatMessageService chatMessageService, ChatMessageQueryService chatMessageQueryService) {
        this.chatMessageService = chatMessageService;
        this.chatMessageQueryService = chatMessageQueryService;
    }

    /**
     * {@code POST  /chat-messages} : Create a new chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatMessageDTO, or with status {@code 400 (Bad Request)} if the chatMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-messages")
    public ResponseEntity<ChatMessageDTO> createChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.created(new URI("/api/chat-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-messages} : Updates an existing chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatMessageDTO,
     * or with status {@code 400 (Bad Request)} if the chatMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-messages")
    public ResponseEntity<ChatMessageDTO> updateChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to update ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-messages} : get all the chatMessages.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatMessages in body.
     */
    @GetMapping("/chat-messages")
    public ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(ChatMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatMessages by criteria: {}", criteria);
        Page<ChatMessageDTO> page = chatMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-messages/count} : count all the chatMessages.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-messages/count")
    public ResponseEntity<Long> countChatMessages(ChatMessageCriteria criteria) {
        log.debug("REST request to count ChatMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-messages/:id} : get the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-messages/{id}")
    public ResponseEntity<ChatMessageDTO> getChatMessage(@PathVariable Long id) {
        log.debug("REST request to get ChatMessage : {}", id);
        Optional<ChatMessageDTO> chatMessageDTO = chatMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatMessageDTO);
    }

    /**
     * {@code DELETE  /chat-messages/:id} : delete the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-messages/{id}")
    public ResponseEntity<Void> deleteChatMessage(@PathVariable Long id) {
        log.debug("REST request to delete ChatMessage : {}", id);
        chatMessageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
