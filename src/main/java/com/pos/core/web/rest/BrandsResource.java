package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.Brands;
import com.pos.core.service.BrandsService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Brands.
 */
@RestController
@RequestMapping("/api")
public class BrandsResource {

    private final Logger log = LoggerFactory.getLogger(BrandsResource.class);

    private static final String ENTITY_NAME = "brands";

    private final BrandsService brandsService;

    public BrandsResource(BrandsService brandsService) {
        this.brandsService = brandsService;
    }

    /**
     * POST  /brands : Create a new brands.
     *
     * @param brands the brands to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brands, or with status 400 (Bad Request) if the brands has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brands")
    @Timed
    public ResponseEntity<Brands> createBrands(@RequestBody Brands brands) throws URISyntaxException {
        log.debug("REST request to save Brands : {}", brands);
        if (brands.getId() != null) {
            throw new BadRequestAlertException("A new brands cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Brands result = brandsService.save(brands);
        return ResponseEntity.created(new URI("/api/brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brands : Updates an existing brands.
     *
     * @param brands the brands to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brands,
     * or with status 400 (Bad Request) if the brands is not valid,
     * or with status 500 (Internal Server Error) if the brands couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brands")
    @Timed
    public ResponseEntity<Brands> updateBrands(@RequestBody Brands brands) throws URISyntaxException {
        log.debug("REST request to update Brands : {}", brands);
        if (brands.getId() == null) {
            return createBrands(brands);
        }
        Brands result = brandsService.save(brands);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brands.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brands : get all the brands.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of brands in body
     */
    @GetMapping("/brands")
    @Timed
    public ResponseEntity<List<Brands>> getAllBrands(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("products-is-null".equals(filter)) {
            log.debug("REST request to get all Brandss where products is null");
            return new ResponseEntity<>(brandsService.findAllWhereProductsIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Brands");
        Page<Brands> page = brandsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /brands/:id : get the "id" brands.
     *
     * @param id the id of the brands to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brands, or with status 404 (Not Found)
     */
    @GetMapping("/brands/{id}")
    @Timed
    public ResponseEntity<Brands> getBrands(@PathVariable Long id) {
        log.debug("REST request to get Brands : {}", id);
        Brands brands = brandsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(brands));
    }

    /**
     * DELETE  /brands/:id : delete the "id" brands.
     *
     * @param id the id of the brands to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brands/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrands(@PathVariable Long id) {
        log.debug("REST request to delete Brands : {}", id);
        brandsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/brands?query=:query : search for the brands corresponding
     * to the query.
     *
     * @param query the query of the brands search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/brands")
    @Timed
    public ResponseEntity<List<Brands>> searchBrands(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Brands for query {}", query);
        Page<Brands> page = brandsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/brands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
