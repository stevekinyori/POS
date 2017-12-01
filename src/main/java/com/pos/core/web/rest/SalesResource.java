package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.Sales;
import com.pos.core.service.SalesService;
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
 * REST controller for managing Sales.
 */
@RestController
@RequestMapping("/api")
public class SalesResource {

    private final Logger log = LoggerFactory.getLogger(SalesResource.class);

    private static final String ENTITY_NAME = "sales";

    private final SalesService salesService;

    public SalesResource(SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * POST  /sales : Create a new sales.
     *
     * @param sales the sales to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sales, or with status 400 (Bad Request) if the sales has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales")
    @Timed
    public ResponseEntity<Sales> createSales(@Valid @RequestBody Sales sales) throws URISyntaxException {
        log.debug("REST request to save Sales : {}", sales);
        if (sales.getId() != null) {
            throw new BadRequestAlertException("A new sales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sales result = salesService.save(sales);
        return ResponseEntity.created(new URI("/api/sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales : Updates an existing sales.
     *
     * @param sales the sales to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sales,
     * or with status 400 (Bad Request) if the sales is not valid,
     * or with status 500 (Internal Server Error) if the sales couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales")
    @Timed
    public ResponseEntity<Sales> updateSales(@Valid @RequestBody Sales sales) throws URISyntaxException {
        log.debug("REST request to update Sales : {}", sales);
        if (sales.getId() == null) {
            return createSales(sales);
        }
        Sales result = salesService.save(sales);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sales.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales : get all the sales.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sales in body
     */
    @GetMapping("/sales")
    @Timed
    public ResponseEntity<List<Sales>> getAllSales(Pageable pageable) {
        log.debug("REST request to get a page of Sales");
        Page<Sales> page = salesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sales/:id : get the "id" sales.
     *
     * @param id the id of the sales to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sales, or with status 404 (Not Found)
     */
    @GetMapping("/sales/{id}")
    @Timed
    public ResponseEntity<Sales> getSales(@PathVariable Long id) {
        log.debug("REST request to get Sales : {}", id);
        Sales sales = salesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sales));
    }

    /**
     * DELETE  /sales/:id : delete the "id" sales.
     *
     * @param id the id of the sales to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales/{id}")
    @Timed
    public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
        log.debug("REST request to delete Sales : {}", id);
        salesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sales?query=:query : search for the sales corresponding
     * to the query.
     *
     * @param query the query of the sales search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sales")
    @Timed
    public ResponseEntity<List<Sales>> searchSales(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sales for query {}", query);
        Page<Sales> page = salesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
