package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.ProductCategories;
import com.pos.core.service.ProductCategoriesService;
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
 * REST controller for managing ProductCategories.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoriesResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoriesResource.class);

    private static final String ENTITY_NAME = "productCategories";

    private final ProductCategoriesService productCategoriesService;

    public ProductCategoriesResource(ProductCategoriesService productCategoriesService) {
        this.productCategoriesService = productCategoriesService;
    }

    /**
     * POST  /product-categories : Create a new productCategories.
     *
     * @param productCategories the productCategories to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productCategories, or with status 400 (Bad Request) if the productCategories has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategories> createProductCategories(@Valid @RequestBody ProductCategories productCategories) throws URISyntaxException {
        log.debug("REST request to save ProductCategories : {}", productCategories);
        if (productCategories.getId() != null) {
            throw new BadRequestAlertException("A new productCategories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCategories result = productCategoriesService.save(productCategories);
        return ResponseEntity.created(new URI("/api/product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-categories : Updates an existing productCategories.
     *
     * @param productCategories the productCategories to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productCategories,
     * or with status 400 (Bad Request) if the productCategories is not valid,
     * or with status 500 (Internal Server Error) if the productCategories couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategories> updateProductCategories(@Valid @RequestBody ProductCategories productCategories) throws URISyntaxException {
        log.debug("REST request to update ProductCategories : {}", productCategories);
        if (productCategories.getId() == null) {
            return createProductCategories(productCategories);
        }
        ProductCategories result = productCategoriesService.save(productCategories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productCategories.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-categories : get all the productCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productCategories in body
     */
    @GetMapping("/product-categories")
    @Timed
    public ResponseEntity<List<ProductCategories>> getAllProductCategories(Pageable pageable) {
        log.debug("REST request to get a page of ProductCategories");
        Page<ProductCategories> page = productCategoriesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /product-categories/:id : get the "id" productCategories.
     *
     * @param id the id of the productCategories to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productCategories, or with status 404 (Not Found)
     */
    @GetMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<ProductCategories> getProductCategories(@PathVariable Long id) {
        log.debug("REST request to get ProductCategories : {}", id);
        ProductCategories productCategories = productCategoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productCategories));
    }

    /**
     * DELETE  /product-categories/:id : delete the "id" productCategories.
     *
     * @param id the id of the productCategories to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductCategories(@PathVariable Long id) {
        log.debug("REST request to delete ProductCategories : {}", id);
        productCategoriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-categories?query=:query : search for the productCategories corresponding
     * to the query.
     *
     * @param query the query of the productCategories search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-categories")
    @Timed
    public ResponseEntity<List<ProductCategories>> searchProductCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductCategories for query {}", query);
        Page<ProductCategories> page = productCategoriesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
