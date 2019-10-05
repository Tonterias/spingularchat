package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatOffensiveMessageService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatOffensiveMessageDTO;
import com.spingular.chat.service.dto.ChatOffensiveMessageCriteria;
import com.spingular.chat.service.ChatOffensiveMessageQueryService;

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
 * REST controller for managing {@link com.spingular.chat.domain.ChatOffensiveMessage}.
 */
@RestController
@RequestMapping("/api")
public class ChatOffensiveMessageResource {

    private final Logger log = LoggerFactory.getLogger(ChatOffensiveMessageResource.class);

    private static final String ENTITY_NAME = "chatOffensiveMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatOffensiveMessageService chatOffensiveMessageService;

    private final ChatOffensiveMessageQueryService chatOffensiveMessageQueryService;

    public ChatOffensiveMessageResource(ChatOffensiveMessageService chatOffensiveMessageService, ChatOffensiveMessageQueryService chatOffensiveMessageQueryService) {
        this.chatOffensiveMessageService = chatOffensiveMessageService;
        this.chatOffensiveMessageQueryService = chatOffensiveMessageQueryService;
    }

    /**
     * {@code POST  /chat-offensive-messages} : Create a new chatOffensiveMessage.
     *
     * @param chatOffensiveMessageDTO the chatOffensiveMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatOffensiveMessageDTO, or with status {@code 400 (Bad Request)} if the chatOffensiveMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-offensive-messages")
    public ResponseEntity<ChatOffensiveMessageDTO> createChatOffensiveMessage(@Valid @RequestBody ChatOffensiveMessageDTO chatOffensiveMessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatOffensiveMessage : {}", chatOffensiveMessageDTO);
        if (chatOffensiveMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatOffensiveMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatOffensiveMessageDTO result = chatOffensiveMessageService.save(chatOffensiveMessageDTO);
        return ResponseEntity.created(new URI("/api/chat-offensive-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-offensive-messages} : Updates an existing chatOffensiveMessage.
     *
     * @param chatOffensiveMessageDTO the chatOffensiveMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatOffensiveMessageDTO,
     * or with status {@code 400 (Bad Request)} if the chatOffensiveMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatOffensiveMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-offensive-messages")
    public ResponseEntity<ChatOffensiveMessageDTO> updateChatOffensiveMessage(@Valid @RequestBody ChatOffensiveMessageDTO chatOffensiveMessageDTO) throws URISyntaxException {
        log.debug("REST request to update ChatOffensiveMessage : {}", chatOffensiveMessageDTO);
        if (chatOffensiveMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatOffensiveMessageDTO result = chatOffensiveMessageService.save(chatOffensiveMessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatOffensiveMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-offensive-messages} : get all the chatOffensiveMessages.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatOffensiveMessages in body.
     */
    @GetMapping("/chat-offensive-messages")
    public ResponseEntity<List<ChatOffensiveMessageDTO>> getAllChatOffensiveMessages(ChatOffensiveMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatOffensiveMessages by criteria: {}", criteria);
        Page<ChatOffensiveMessageDTO> page = chatOffensiveMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-offensive-messages/count} : count all the chatOffensiveMessages.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-offensive-messages/count")
    public ResponseEntity<Long> countChatOffensiveMessages(ChatOffensiveMessageCriteria criteria) {
        log.debug("REST request to count ChatOffensiveMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatOffensiveMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-offensive-messages/:id} : get the "id" chatOffensiveMessage.
     *
     * @param id the id of the chatOffensiveMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatOffensiveMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-offensive-messages/{id}")
    public ResponseEntity<ChatOffensiveMessageDTO> getChatOffensiveMessage(@PathVariable Long id) {
        log.debug("REST request to get ChatOffensiveMessage : {}", id);
        Optional<ChatOffensiveMessageDTO> chatOffensiveMessageDTO = chatOffensiveMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatOffensiveMessageDTO);
    }

    /**
     * {@code DELETE  /chat-offensive-messages/:id} : delete the "id" chatOffensiveMessage.
     *
     * @param id the id of the chatOffensiveMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-offensive-messages/{id}")
    public ResponseEntity<Void> deleteChatOffensiveMessage(@PathVariable Long id) {
        log.debug("REST request to delete ChatOffensiveMessage : {}", id);
        chatOffensiveMessageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
