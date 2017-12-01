package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.Suppliers;
import com.pos.core.service.SuppliersService;
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
 * REST controller for managing Suppliers.
 */
@RestController
@RequestMapping("/api")
public class SuppliersResource {

    private final Logger log = LoggerFactory.getLogger(SuppliersResource.class);

    private static final String ENTITY_NAME = "suppliers";

    private final SuppliersService suppliersService;

    public SuppliersResource(SuppliersService suppliersService) {
        this.suppliersService = suppliersService;
    }

    /**
     * POST  /suppliers : Create a new suppliers.
     *
     * @param suppliers the suppliers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suppliers, or with status 400 (Bad Request) if the suppliers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suppliers")
    @Timed
    public ResponseEntity<Suppliers> createSuppliers(@Valid @RequestBody Suppliers suppliers) throws URISyntaxException {
        log.debug("REST request to save Suppliers : {}", suppliers);
        if (suppliers.getId() != null) {
            throw new BadRequestAlertException("A new suppliers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Suppliers result = suppliersService.save(suppliers);
        return ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suppliers : Updates an existing suppliers.
     *
     * @param suppliers the suppliers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suppliers,
     * or with status 400 (Bad Request) if the suppliers is not valid,
     * or with status 500 (Internal Server Error) if the suppliers couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suppliers")
    @Timed
    public ResponseEntity<Suppliers> updateSuppliers(@Valid @RequestBody Suppliers suppliers) throws URISyntaxException {
        log.debug("REST request to update Suppliers : {}", suppliers);
        if (suppliers.getId() == null) {
            return createSuppliers(suppliers);
        }
        Suppliers result = suppliersService.save(suppliers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suppliers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suppliers : get all the suppliers.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of suppliers in body
     */
    @GetMapping("/suppliers")
    @Timed
    public ResponseEntity<List<Suppliers>> getAllSuppliers(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("products-is-null".equals(filter)) {
            log.debug("REST request to get all Supplierss where products is null");
            return new ResponseEntity<>(suppliersService.findAllWhereProductsIsNull(),
                    HttpStatus.OK);
        }
        if ("goodsreturned-is-null".equals(filter)) {
            log.debug("REST request to get all Supplierss where goodsReturned is null");
            return new ResponseEntity<>(suppliersService.findAllWhereGoodsReturnedIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Suppliers");
        Page<Suppliers> page = suppliersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /suppliers/:id : get the "id" suppliers.
     *
     * @param id the id of the suppliers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suppliers, or with status 404 (Not Found)
     */
    @GetMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<Suppliers> getSuppliers(@PathVariable Long id) {
        log.debug("REST request to get Suppliers : {}", id);
        Suppliers suppliers = suppliersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(suppliers));
    }

    /**
     * DELETE  /suppliers/:id : delete the "id" suppliers.
     *
     * @param id the id of the suppliers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuppliers(@PathVariable Long id) {
        log.debug("REST request to delete Suppliers : {}", id);
        suppliersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/suppliers?query=:query : search for the suppliers corresponding
     * to the query.
     *
     * @param query the query of the suppliers search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/suppliers")
    @Timed
    public ResponseEntity<List<Suppliers>> searchSuppliers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Suppliers for query {}", query);
        Page<Suppliers> page = suppliersService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
