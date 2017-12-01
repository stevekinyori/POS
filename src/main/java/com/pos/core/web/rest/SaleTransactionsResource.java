package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.SaleTransactions;
import com.pos.core.service.SaleTransactionsService;
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
 * REST controller for managing SaleTransactions.
 */
@RestController
@RequestMapping("/api")
public class SaleTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(SaleTransactionsResource.class);

    private static final String ENTITY_NAME = "saleTransactions";

    private final SaleTransactionsService saleTransactionsService;

    public SaleTransactionsResource(SaleTransactionsService saleTransactionsService) {
        this.saleTransactionsService = saleTransactionsService;
    }

    /**
     * POST  /sale-transactions : Create a new saleTransactions.
     *
     * @param saleTransactions the saleTransactions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleTransactions, or with status 400 (Bad Request) if the saleTransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-transactions")
    @Timed
    public ResponseEntity<SaleTransactions> createSaleTransactions(@Valid @RequestBody SaleTransactions saleTransactions) throws URISyntaxException {
        log.debug("REST request to save SaleTransactions : {}", saleTransactions);
        if (saleTransactions.getId() != null) {
            throw new BadRequestAlertException("A new saleTransactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleTransactions result = saleTransactionsService.save(saleTransactions);
        return ResponseEntity.created(new URI("/api/sale-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-transactions : Updates an existing saleTransactions.
     *
     * @param saleTransactions the saleTransactions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleTransactions,
     * or with status 400 (Bad Request) if the saleTransactions is not valid,
     * or with status 500 (Internal Server Error) if the saleTransactions couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-transactions")
    @Timed
    public ResponseEntity<SaleTransactions> updateSaleTransactions(@Valid @RequestBody SaleTransactions saleTransactions) throws URISyntaxException {
        log.debug("REST request to update SaleTransactions : {}", saleTransactions);
        if (saleTransactions.getId() == null) {
            return createSaleTransactions(saleTransactions);
        }
        SaleTransactions result = saleTransactionsService.save(saleTransactions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleTransactions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-transactions : get all the saleTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saleTransactions in body
     */
    @GetMapping("/sale-transactions")
    @Timed
    public ResponseEntity<List<SaleTransactions>> getAllSaleTransactions(Pageable pageable) {
        log.debug("REST request to get a page of SaleTransactions");
        Page<SaleTransactions> page = saleTransactionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sale-transactions/:id : get the "id" saleTransactions.
     *
     * @param id the id of the saleTransactions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleTransactions, or with status 404 (Not Found)
     */
    @GetMapping("/sale-transactions/{id}")
    @Timed
    public ResponseEntity<SaleTransactions> getSaleTransactions(@PathVariable Long id) {
        log.debug("REST request to get SaleTransactions : {}", id);
        SaleTransactions saleTransactions = saleTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saleTransactions));
    }

    /**
     * DELETE  /sale-transactions/:id : delete the "id" saleTransactions.
     *
     * @param id the id of the saleTransactions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleTransactions(@PathVariable Long id) {
        log.debug("REST request to delete SaleTransactions : {}", id);
        saleTransactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-transactions?query=:query : search for the saleTransactions corresponding
     * to the query.
     *
     * @param query the query of the saleTransactions search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sale-transactions")
    @Timed
    public ResponseEntity<List<SaleTransactions>> searchSaleTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaleTransactions for query {}", query);
        Page<SaleTransactions> page = saleTransactionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sale-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
