package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.SubCategories;
import com.pos.core.service.SubCategoriesService;
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
 * REST controller for managing SubCategories.
 */
@RestController
@RequestMapping("/api")
public class SubCategoriesResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoriesResource.class);

    private static final String ENTITY_NAME = "subCategories";

    private final SubCategoriesService subCategoriesService;

    public SubCategoriesResource(SubCategoriesService subCategoriesService) {
        this.subCategoriesService = subCategoriesService;
    }

    /**
     * POST  /sub-categories : Create a new subCategories.
     *
     * @param subCategories the subCategories to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subCategories, or with status 400 (Bad Request) if the subCategories has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-categories")
    @Timed
    public ResponseEntity<SubCategories> createSubCategories(@Valid @RequestBody SubCategories subCategories) throws URISyntaxException {
        log.debug("REST request to save SubCategories : {}", subCategories);
        if (subCategories.getId() != null) {
            throw new BadRequestAlertException("A new subCategories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCategories result = subCategoriesService.save(subCategories);
        return ResponseEntity.created(new URI("/api/sub-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-categories : Updates an existing subCategories.
     *
     * @param subCategories the subCategories to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subCategories,
     * or with status 400 (Bad Request) if the subCategories is not valid,
     * or with status 500 (Internal Server Error) if the subCategories couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-categories")
    @Timed
    public ResponseEntity<SubCategories> updateSubCategories(@Valid @RequestBody SubCategories subCategories) throws URISyntaxException {
        log.debug("REST request to update SubCategories : {}", subCategories);
        if (subCategories.getId() == null) {
            return createSubCategories(subCategories);
        }
        SubCategories result = subCategoriesService.save(subCategories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subCategories.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-categories : get all the subCategories.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of subCategories in body
     */
    @GetMapping("/sub-categories")
    @Timed
    public ResponseEntity<List<SubCategories>> getAllSubCategories(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("subcategoryproducts-is-null".equals(filter)) {
            log.debug("REST request to get all SubCategoriess where subCategoryProducts is null");
            return new ResponseEntity<>(subCategoriesService.findAllWhereSubCategoryProductsIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of SubCategories");
        Page<SubCategories> page = subCategoriesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sub-categories/:id : get the "id" subCategories.
     *
     * @param id the id of the subCategories to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subCategories, or with status 404 (Not Found)
     */
    @GetMapping("/sub-categories/{id}")
    @Timed
    public ResponseEntity<SubCategories> getSubCategories(@PathVariable Long id) {
        log.debug("REST request to get SubCategories : {}", id);
        SubCategories subCategories = subCategoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subCategories));
    }

    /**
     * DELETE  /sub-categories/:id : delete the "id" subCategories.
     *
     * @param id the id of the subCategories to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubCategories(@PathVariable Long id) {
        log.debug("REST request to delete SubCategories : {}", id);
        subCategoriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sub-categories?query=:query : search for the subCategories corresponding
     * to the query.
     *
     * @param query the query of the subCategories search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sub-categories")
    @Timed
    public ResponseEntity<List<SubCategories>> searchSubCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SubCategories for query {}", query);
        Page<SubCategories> page = subCategoriesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sub-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
