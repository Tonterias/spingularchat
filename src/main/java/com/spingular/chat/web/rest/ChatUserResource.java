package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatUserService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatUserDTO;
import com.spingular.chat.service.dto.ChatUserCriteria;
import com.spingular.chat.service.ChatUserQueryService;

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
 * REST controller for managing {@link com.spingular.chat.domain.ChatUser}.
 */
@RestController
@RequestMapping("/api")
public class ChatUserResource {

    private final Logger log = LoggerFactory.getLogger(ChatUserResource.class);

    private static final String ENTITY_NAME = "chatUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatUserService chatUserService;

    private final ChatUserQueryService chatUserQueryService;

    public ChatUserResource(ChatUserService chatUserService, ChatUserQueryService chatUserQueryService) {
        this.chatUserService = chatUserService;
        this.chatUserQueryService = chatUserQueryService;
    }

    /**
     * {@code POST  /chat-users} : Create a new chatUser.
     *
     * @param chatUserDTO the chatUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatUserDTO, or with status {@code 400 (Bad Request)} if the chatUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-users")
    public ResponseEntity<ChatUserDTO> createChatUser(@Valid @RequestBody ChatUserDTO chatUserDTO) throws URISyntaxException {
        log.debug("REST request to save ChatUser : {}", chatUserDTO);
        if (chatUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatUserDTO result = chatUserService.save(chatUserDTO);
        return ResponseEntity.created(new URI("/api/chat-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-users} : Updates an existing chatUser.
     *
     * @param chatUserDTO the chatUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-users")
    public ResponseEntity<ChatUserDTO> updateChatUser(@Valid @RequestBody ChatUserDTO chatUserDTO) throws URISyntaxException {
        log.debug("REST request to update ChatUser : {}", chatUserDTO);
        if (chatUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatUserDTO result = chatUserService.save(chatUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-users} : get all the chatUsers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatUsers in body.
     */
    @GetMapping("/chat-users")
    public ResponseEntity<List<ChatUserDTO>> getAllChatUsers(ChatUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatUsers by criteria: {}", criteria);
        Page<ChatUserDTO> page = chatUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-users/count} : count all the chatUsers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-users/count")
    public ResponseEntity<Long> countChatUsers(ChatUserCriteria criteria) {
        log.debug("REST request to count ChatUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-users/:id} : get the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-users/{id}")
    public ResponseEntity<ChatUserDTO> getChatUser(@PathVariable Long id) {
        log.debug("REST request to get ChatUser : {}", id);
        Optional<ChatUserDTO> chatUserDTO = chatUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatUserDTO);
    }

    /**
     * {@code DELETE  /chat-users/:id} : delete the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-users/{id}")
    public ResponseEntity<Void> deleteChatUser(@PathVariable Long id) {
        log.debug("REST request to delete ChatUser : {}", id);
        chatUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
