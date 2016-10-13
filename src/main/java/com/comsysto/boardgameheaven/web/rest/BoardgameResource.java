package com.comsysto.boardgameheaven.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comsysto.boardgameheaven.domain.Boardgame;

import com.comsysto.boardgameheaven.repository.BoardgameRepository;
import com.comsysto.boardgameheaven.web.rest.util.HeaderUtil;
import com.comsysto.boardgameheaven.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Boardgame.
 */
@RestController
@RequestMapping("/api")
public class BoardgameResource {

    private final Logger log = LoggerFactory.getLogger(BoardgameResource.class);
        
    @Inject
    private BoardgameRepository boardgameRepository;

    /**
     * POST  /boardgames : Create a new boardgame.
     *
     * @param boardgame the boardgame to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boardgame, or with status 400 (Bad Request) if the boardgame has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boardgames",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boardgame> createBoardgame(@Valid @RequestBody Boardgame boardgame) throws URISyntaxException {
        log.debug("REST request to save Boardgame : {}", boardgame);
        if (boardgame.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("boardgame", "idexists", "A new boardgame cannot already have an ID")).body(null);
        }
        Boardgame result = boardgameRepository.save(boardgame);
        return ResponseEntity.created(new URI("/api/boardgames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("boardgame", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boardgames : Updates an existing boardgame.
     *
     * @param boardgame the boardgame to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boardgame,
     * or with status 400 (Bad Request) if the boardgame is not valid,
     * or with status 500 (Internal Server Error) if the boardgame couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boardgames",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boardgame> updateBoardgame(@Valid @RequestBody Boardgame boardgame) throws URISyntaxException {
        log.debug("REST request to update Boardgame : {}", boardgame);
        if (boardgame.getId() == null) {
            return createBoardgame(boardgame);
        }
        Boardgame result = boardgameRepository.save(boardgame);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("boardgame", boardgame.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boardgames : get all the boardgames.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of boardgames in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/boardgames",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Boardgame>> getAllBoardgames(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Boardgames");
        Page<Boardgame> page = boardgameRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/boardgames");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /boardgames/:id : get the "id" boardgame.
     *
     * @param id the id of the boardgame to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boardgame, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/boardgames/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boardgame> getBoardgame(@PathVariable Long id) {
        log.debug("REST request to get Boardgame : {}", id);
        Boardgame boardgame = boardgameRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(boardgame)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /boardgames/:id : delete the "id" boardgame.
     *
     * @param id the id of the boardgame to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/boardgames/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        log.debug("REST request to delete Boardgame : {}", id);
        boardgameRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("boardgame", id.toString())).build();
    }

}
