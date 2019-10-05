package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatRoomAllowedUserService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;
import com.spingular.chat.service.dto.ChatRoomAllowedUserCriteria;
import com.spingular.chat.service.ChatRoomAllowedUserQueryService;

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
 * REST controller for managing {@link com.spingular.chat.domain.ChatRoomAllowedUser}.
 */
@RestController
@RequestMapping("/api")
public class ChatRoomAllowedUserResource {

    private final Logger log = LoggerFactory.getLogger(ChatRoomAllowedUserResource.class);

    private static final String ENTITY_NAME = "chatRoomAllowedUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatRoomAllowedUserService chatRoomAllowedUserService;

    private final ChatRoomAllowedUserQueryService chatRoomAllowedUserQueryService;

    public ChatRoomAllowedUserResource(ChatRoomAllowedUserService chatRoomAllowedUserService, ChatRoomAllowedUserQueryService chatRoomAllowedUserQueryService) {
        this.chatRoomAllowedUserService = chatRoomAllowedUserService;
        this.chatRoomAllowedUserQueryService = chatRoomAllowedUserQueryService;
    }

    /**
     * {@code POST  /chat-room-allowed-users} : Create a new chatRoomAllowedUser.
     *
     * @param chatRoomAllowedUserDTO the chatRoomAllowedUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatRoomAllowedUserDTO, or with status {@code 400 (Bad Request)} if the chatRoomAllowedUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-room-allowed-users")
    public ResponseEntity<ChatRoomAllowedUserDTO> createChatRoomAllowedUser(@Valid @RequestBody ChatRoomAllowedUserDTO chatRoomAllowedUserDTO) throws URISyntaxException {
        log.debug("REST request to save ChatRoomAllowedUser : {}", chatRoomAllowedUserDTO);
        if (chatRoomAllowedUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatRoomAllowedUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatRoomAllowedUserDTO result = chatRoomAllowedUserService.save(chatRoomAllowedUserDTO);
        return ResponseEntity.created(new URI("/api/chat-room-allowed-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-room-allowed-users} : Updates an existing chatRoomAllowedUser.
     *
     * @param chatRoomAllowedUserDTO the chatRoomAllowedUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatRoomAllowedUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatRoomAllowedUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatRoomAllowedUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-room-allowed-users")
    public ResponseEntity<ChatRoomAllowedUserDTO> updateChatRoomAllowedUser(@Valid @RequestBody ChatRoomAllowedUserDTO chatRoomAllowedUserDTO) throws URISyntaxException {
        log.debug("REST request to update ChatRoomAllowedUser : {}", chatRoomAllowedUserDTO);
        if (chatRoomAllowedUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatRoomAllowedUserDTO result = chatRoomAllowedUserService.save(chatRoomAllowedUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatRoomAllowedUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-room-allowed-users} : get all the chatRoomAllowedUsers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatRoomAllowedUsers in body.
     */
    @GetMapping("/chat-room-allowed-users")
    public ResponseEntity<List<ChatRoomAllowedUserDTO>> getAllChatRoomAllowedUsers(ChatRoomAllowedUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatRoomAllowedUsers by criteria: {}", criteria);
        Page<ChatRoomAllowedUserDTO> page = chatRoomAllowedUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-room-allowed-users/count} : count all the chatRoomAllowedUsers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-room-allowed-users/count")
    public ResponseEntity<Long> countChatRoomAllowedUsers(ChatRoomAllowedUserCriteria criteria) {
        log.debug("REST request to count ChatRoomAllowedUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatRoomAllowedUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-room-allowed-users/:id} : get the "id" chatRoomAllowedUser.
     *
     * @param id the id of the chatRoomAllowedUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatRoomAllowedUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-room-allowed-users/{id}")
    public ResponseEntity<ChatRoomAllowedUserDTO> getChatRoomAllowedUser(@PathVariable Long id) {
        log.debug("REST request to get ChatRoomAllowedUser : {}", id);
        Optional<ChatRoomAllowedUserDTO> chatRoomAllowedUserDTO = chatRoomAllowedUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatRoomAllowedUserDTO);
    }

    /**
     * {@code DELETE  /chat-room-allowed-users/:id} : delete the "id" chatRoomAllowedUser.
     *
     * @param id the id of the chatRoomAllowedUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-room-allowed-users/{id}")
    public ResponseEntity<Void> deleteChatRoomAllowedUser(@PathVariable Long id) {
        log.debug("REST request to delete ChatRoomAllowedUser : {}", id);
        chatRoomAllowedUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
