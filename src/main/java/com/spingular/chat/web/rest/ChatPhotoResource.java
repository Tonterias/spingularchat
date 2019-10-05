package com.spingular.chat.web.rest;

import com.spingular.chat.service.ChatPhotoService;
import com.spingular.chat.web.rest.errors.BadRequestAlertException;
import com.spingular.chat.service.dto.ChatPhotoDTO;
import com.spingular.chat.service.dto.ChatPhotoCriteria;
import com.spingular.chat.service.ChatPhotoQueryService;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.spingular.chat.domain.ChatPhoto}.
 */
@RestController
@RequestMapping("/api")
public class ChatPhotoResource {

    private final Logger log = LoggerFactory.getLogger(ChatPhotoResource.class);

    private static final String ENTITY_NAME = "chatPhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatPhotoService chatPhotoService;

    private final ChatPhotoQueryService chatPhotoQueryService;

    public ChatPhotoResource(ChatPhotoService chatPhotoService, ChatPhotoQueryService chatPhotoQueryService) {
        this.chatPhotoService = chatPhotoService;
        this.chatPhotoQueryService = chatPhotoQueryService;
    }

    /**
     * {@code POST  /chat-photos} : Create a new chatPhoto.
     *
     * @param chatPhotoDTO the chatPhotoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatPhotoDTO, or with status {@code 400 (Bad Request)} if the chatPhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-photos")
    public ResponseEntity<ChatPhotoDTO> createChatPhoto(@Valid @RequestBody ChatPhotoDTO chatPhotoDTO) throws URISyntaxException {
        log.debug("REST request to save ChatPhoto : {}", chatPhotoDTO);
        if (chatPhotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatPhotoDTO result = chatPhotoService.save(chatPhotoDTO);
        return ResponseEntity.created(new URI("/api/chat-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-photos} : Updates an existing chatPhoto.
     *
     * @param chatPhotoDTO the chatPhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatPhotoDTO,
     * or with status {@code 400 (Bad Request)} if the chatPhotoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatPhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-photos")
    public ResponseEntity<ChatPhotoDTO> updateChatPhoto(@Valid @RequestBody ChatPhotoDTO chatPhotoDTO) throws URISyntaxException {
        log.debug("REST request to update ChatPhoto : {}", chatPhotoDTO);
        if (chatPhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatPhotoDTO result = chatPhotoService.save(chatPhotoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatPhotoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-photos} : get all the chatPhotos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatPhotos in body.
     */
    @GetMapping("/chat-photos")
    public ResponseEntity<List<ChatPhotoDTO>> getAllChatPhotos(ChatPhotoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatPhotos by criteria: {}", criteria);
        Page<ChatPhotoDTO> page = chatPhotoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chat-photos/count} : count all the chatPhotos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chat-photos/count")
    public ResponseEntity<Long> countChatPhotos(ChatPhotoCriteria criteria) {
        log.debug("REST request to count ChatPhotos by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatPhotoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-photos/:id} : get the "id" chatPhoto.
     *
     * @param id the id of the chatPhotoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatPhotoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-photos/{id}")
    public ResponseEntity<ChatPhotoDTO> getChatPhoto(@PathVariable Long id) {
        log.debug("REST request to get ChatPhoto : {}", id);
        Optional<ChatPhotoDTO> chatPhotoDTO = chatPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatPhotoDTO);
    }

    /**
     * {@code DELETE  /chat-photos/:id} : delete the "id" chatPhoto.
     *
     * @param id the id of the chatPhotoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-photos/{id}")
    public ResponseEntity<Void> deleteChatPhoto(@PathVariable Long id) {
        log.debug("REST request to delete ChatPhoto : {}", id);
        chatPhotoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
