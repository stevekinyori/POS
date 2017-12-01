package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.SupplierProducts;
import com.pos.core.service.SupplierProductsService;
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
 * REST controller for managing SupplierProducts.
 */
@RestController
@RequestMapping("/api")
public class SupplierProductsResource {

    private final Logger log = LoggerFactory.getLogger(SupplierProductsResource.class);

    private static final String ENTITY_NAME = "supplierProducts";

    private final SupplierProductsService supplierProductsService;

    public SupplierProductsResource(SupplierProductsService supplierProductsService) {
        this.supplierProductsService = supplierProductsService;
    }

    /**
     * POST  /supplier-products : Create a new supplierProducts.
     *
     * @param supplierProducts the supplierProducts to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supplierProducts, or with status 400 (Bad Request) if the supplierProducts has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/supplier-products")
    @Timed
    public ResponseEntity<SupplierProducts> createSupplierProducts(@Valid @RequestBody SupplierProducts supplierProducts) throws URISyntaxException {
        log.debug("REST request to save SupplierProducts : {}", supplierProducts);
        if (supplierProducts.getId() != null) {
            throw new BadRequestAlertException("A new supplierProducts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierProducts result = supplierProductsService.save(supplierProducts);
        return ResponseEntity.created(new URI("/api/supplier-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /supplier-products : Updates an existing supplierProducts.
     *
     * @param supplierProducts the supplierProducts to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supplierProducts,
     * or with status 400 (Bad Request) if the supplierProducts is not valid,
     * or with status 500 (Internal Server Error) if the supplierProducts couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/supplier-products")
    @Timed
    public ResponseEntity<SupplierProducts> updateSupplierProducts(@Valid @RequestBody SupplierProducts supplierProducts) throws URISyntaxException {
        log.debug("REST request to update SupplierProducts : {}", supplierProducts);
        if (supplierProducts.getId() == null) {
            return createSupplierProducts(supplierProducts);
        }
        SupplierProducts result = supplierProductsService.save(supplierProducts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, supplierProducts.getId().toString()))
            .body(result);
    }

    /**
     * GET  /supplier-products : get all the supplierProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supplierProducts in body
     */
    @GetMapping("/supplier-products")
    @Timed
    public ResponseEntity<List<SupplierProducts>> getAllSupplierProducts(Pageable pageable) {
        log.debug("REST request to get a page of SupplierProducts");
        Page<SupplierProducts> page = supplierProductsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/supplier-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /supplier-products/:id : get the "id" supplierProducts.
     *
     * @param id the id of the supplierProducts to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supplierProducts, or with status 404 (Not Found)
     */
    @GetMapping("/supplier-products/{id}")
    @Timed
    public ResponseEntity<SupplierProducts> getSupplierProducts(@PathVariable Long id) {
        log.debug("REST request to get SupplierProducts : {}", id);
        SupplierProducts supplierProducts = supplierProductsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supplierProducts));
    }

    /**
     * DELETE  /supplier-products/:id : delete the "id" supplierProducts.
     *
     * @param id the id of the supplierProducts to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/supplier-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupplierProducts(@PathVariable Long id) {
        log.debug("REST request to delete SupplierProducts : {}", id);
        supplierProductsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/supplier-products?query=:query : search for the supplierProducts corresponding
     * to the query.
     *
     * @param query the query of the supplierProducts search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/supplier-products")
    @Timed
    public ResponseEntity<List<SupplierProducts>> searchSupplierProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SupplierProducts for query {}", query);
        Page<SupplierProducts> page = supplierProductsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/supplier-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
