package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.COMPANY_DETAILS;
import com.pos.core.service.COMPANY_DETAILSService;
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
 * REST controller for managing COMPANY_DETAILS.
 */
@RestController
@RequestMapping("/api")
public class COMPANY_DETAILSResource {

    private final Logger log = LoggerFactory.getLogger(COMPANY_DETAILSResource.class);

    private static final String ENTITY_NAME = "cOMPANY_DETAILS";

    private final COMPANY_DETAILSService cOMPANY_DETAILSService;

    public COMPANY_DETAILSResource(COMPANY_DETAILSService cOMPANY_DETAILSService) {
        this.cOMPANY_DETAILSService = cOMPANY_DETAILSService;
    }

    /**
     * POST  /company-details : Create a new cOMPANY_DETAILS.
     *
     * @param cOMPANY_DETAILS the cOMPANY_DETAILS to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cOMPANY_DETAILS, or with status 400 (Bad Request) if the cOMPANY_DETAILS has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-details")
    @Timed
    public ResponseEntity<COMPANY_DETAILS> createCOMPANY_DETAILS(@Valid @RequestBody COMPANY_DETAILS cOMPANY_DETAILS) throws URISyntaxException {
        log.debug("REST request to save COMPANY_DETAILS : {}", cOMPANY_DETAILS);
        if (cOMPANY_DETAILS.getId() != null) {
            throw new BadRequestAlertException("A new cOMPANY_DETAILS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        COMPANY_DETAILS result = cOMPANY_DETAILSService.save(cOMPANY_DETAILS);
        return ResponseEntity.created(new URI("/api/company-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-details : Updates an existing cOMPANY_DETAILS.
     *
     * @param cOMPANY_DETAILS the cOMPANY_DETAILS to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cOMPANY_DETAILS,
     * or with status 400 (Bad Request) if the cOMPANY_DETAILS is not valid,
     * or with status 500 (Internal Server Error) if the cOMPANY_DETAILS couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-details")
    @Timed
    public ResponseEntity<COMPANY_DETAILS> updateCOMPANY_DETAILS(@Valid @RequestBody COMPANY_DETAILS cOMPANY_DETAILS) throws URISyntaxException {
        log.debug("REST request to update COMPANY_DETAILS : {}", cOMPANY_DETAILS);
        if (cOMPANY_DETAILS.getId() == null) {
            return createCOMPANY_DETAILS(cOMPANY_DETAILS);
        }
        COMPANY_DETAILS result = cOMPANY_DETAILSService.save(cOMPANY_DETAILS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cOMPANY_DETAILS.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-details : get all the cOMPANY_DETAILS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cOMPANY_DETAILS in body
     */
    @GetMapping("/company-details")
    @Timed
    public ResponseEntity<List<COMPANY_DETAILS>> getAllCOMPANY_DETAILS(Pageable pageable) {
        log.debug("REST request to get a page of COMPANY_DETAILS");
        Page<COMPANY_DETAILS> page = cOMPANY_DETAILSService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-details/:id : get the "id" cOMPANY_DETAILS.
     *
     * @param id the id of the cOMPANY_DETAILS to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cOMPANY_DETAILS, or with status 404 (Not Found)
     */
    @GetMapping("/company-details/{id}")
    @Timed
    public ResponseEntity<COMPANY_DETAILS> getCOMPANY_DETAILS(@PathVariable Long id) {
        log.debug("REST request to get COMPANY_DETAILS : {}", id);
        COMPANY_DETAILS cOMPANY_DETAILS = cOMPANY_DETAILSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cOMPANY_DETAILS));
    }

    /**
     * DELETE  /company-details/:id : delete the "id" cOMPANY_DETAILS.
     *
     * @param id the id of the cOMPANY_DETAILS to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCOMPANY_DETAILS(@PathVariable Long id) {
        log.debug("REST request to delete COMPANY_DETAILS : {}", id);
        cOMPANY_DETAILSService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-details?query=:query : search for the cOMPANY_DETAILS corresponding
     * to the query.
     *
     * @param query the query of the cOMPANY_DETAILS search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/company-details")
    @Timed
    public ResponseEntity<List<COMPANY_DETAILS>> searchCOMPANY_DETAILS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of COMPANY_DETAILS for query {}", query);
        Page<COMPANY_DETAILS> page = cOMPANY_DETAILSService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
