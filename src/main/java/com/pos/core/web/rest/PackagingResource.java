package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.Packaging;
import com.pos.core.service.PackagingService;
import com.pos.core.web.rest.errors.BadRequestAlertException;
import com.pos.core.web.rest.util.HeaderUtil;
import com.pos.core.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Packaging.
 */
@RestController
@RequestMapping("/api")
public class PackagingResource {

    private final Logger log = LoggerFactory.getLogger(PackagingResource.class);

    private static final String ENTITY_NAME = "packaging";

    private final PackagingService packagingService;

    public PackagingResource(PackagingService packagingService) {
        this.packagingService = packagingService;
    }

    /**
     * POST  /packagings : Create a new packaging.
     *
     * @param packaging the packaging to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packaging, or with status 400 (Bad Request) if the packaging has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/packagings")
    @Timed
    public ResponseEntity<Packaging> createPackaging(@Valid @RequestBody Packaging packaging) throws URISyntaxException {
        log.debug("REST request to save Packaging : {}", packaging);
        if (packaging.getId() != null) {
            throw new BadRequestAlertException("A new packaging cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Packaging result = packagingService.save(packaging);
        return ResponseEntity.created(new URI("/api/packagings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /packagings : Updates an existing packaging.
     *
     * @param packaging the packaging to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packaging,
     * or with status 400 (Bad Request) if the packaging is not valid,
     * or with status 500 (Internal Server Error) if the packaging couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/packagings")
    @Timed
    public ResponseEntity<Packaging> updatePackaging(@Valid @RequestBody Packaging packaging) throws URISyntaxException {
        log.debug("REST request to update Packaging : {}", packaging);
        if (packaging.getId() == null) {
            return createPackaging(packaging);
        }
        Packaging result = packagingService.save(packaging);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, packaging.getId().toString()))
            .body(result);
    }

    /**
     * GET  /packagings : get all the packagings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of packagings in body
     */
    @GetMapping("/packagings")
    @Timed
    public ResponseEntity<List<Packaging>> getAllPackagings(Pageable pageable) {
        log.debug("REST request to get a page of Packagings");
        Page<Packaging> page = packagingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/packagings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /packagings/:id : get the "id" packaging.
     *
     * @param id the id of the packaging to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packaging, or with status 404 (Not Found)
     */
    @GetMapping("/packagings/{id}")
    @Timed
    public ResponseEntity<Packaging> getPackaging(@PathVariable Long id) {
        log.debug("REST request to get Packaging : {}", id);
        Packaging packaging = packagingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(packaging));
    }

    /**
     * DELETE  /packagings/:id : delete the "id" packaging.
     *
     * @param id the id of the packaging to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/packagings/{id}")
    @Timed
    public ResponseEntity<Void> deletePackaging(@PathVariable Long id) {
        log.debug("REST request to delete Packaging : {}", id);
        packagingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/packagings?query=:query : search for the packaging corresponding
     * to the query.
     *
     * @param query the query of the packaging search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/packagings")
    @Timed
    public ResponseEntity<List<Packaging>> searchPackagings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Packagings for query {}", query);
        Page<Packaging> page = packagingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/packagings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
