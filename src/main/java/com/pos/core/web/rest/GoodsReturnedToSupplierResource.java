package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.GoodsReturnedToSupplier;
import com.pos.core.service.GoodsReturnedToSupplierService;
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
 * REST controller for managing GoodsReturnedToSupplier.
 */
@RestController
@RequestMapping("/api")
public class GoodsReturnedToSupplierResource {

    private final Logger log = LoggerFactory.getLogger(GoodsReturnedToSupplierResource.class);

    private static final String ENTITY_NAME = "goodsReturnedToSupplier";

    private final GoodsReturnedToSupplierService goodsReturnedToSupplierService;

    public GoodsReturnedToSupplierResource(GoodsReturnedToSupplierService goodsReturnedToSupplierService) {
        this.goodsReturnedToSupplierService = goodsReturnedToSupplierService;
    }

    /**
     * POST  /goods-returned-to-suppliers : Create a new goodsReturnedToSupplier.
     *
     * @param goodsReturnedToSupplier the goodsReturnedToSupplier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goodsReturnedToSupplier, or with status 400 (Bad Request) if the goodsReturnedToSupplier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/goods-returned-to-suppliers")
    @Timed
    public ResponseEntity<GoodsReturnedToSupplier> createGoodsReturnedToSupplier(@RequestBody GoodsReturnedToSupplier goodsReturnedToSupplier) throws URISyntaxException {
        log.debug("REST request to save GoodsReturnedToSupplier : {}", goodsReturnedToSupplier);
        if (goodsReturnedToSupplier.getId() != null) {
            throw new BadRequestAlertException("A new goodsReturnedToSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoodsReturnedToSupplier result = goodsReturnedToSupplierService.save(goodsReturnedToSupplier);
        return ResponseEntity.created(new URI("/api/goods-returned-to-suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goods-returned-to-suppliers : Updates an existing goodsReturnedToSupplier.
     *
     * @param goodsReturnedToSupplier the goodsReturnedToSupplier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goodsReturnedToSupplier,
     * or with status 400 (Bad Request) if the goodsReturnedToSupplier is not valid,
     * or with status 500 (Internal Server Error) if the goodsReturnedToSupplier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/goods-returned-to-suppliers")
    @Timed
    public ResponseEntity<GoodsReturnedToSupplier> updateGoodsReturnedToSupplier(@RequestBody GoodsReturnedToSupplier goodsReturnedToSupplier) throws URISyntaxException {
        log.debug("REST request to update GoodsReturnedToSupplier : {}", goodsReturnedToSupplier);
        if (goodsReturnedToSupplier.getId() == null) {
            return createGoodsReturnedToSupplier(goodsReturnedToSupplier);
        }
        GoodsReturnedToSupplier result = goodsReturnedToSupplierService.save(goodsReturnedToSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, goodsReturnedToSupplier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goods-returned-to-suppliers : get all the goodsReturnedToSuppliers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of goodsReturnedToSuppliers in body
     */
    @GetMapping("/goods-returned-to-suppliers")
    @Timed
    public ResponseEntity<List<GoodsReturnedToSupplier>> getAllGoodsReturnedToSuppliers(Pageable pageable) {
        log.debug("REST request to get a page of GoodsReturnedToSuppliers");
        Page<GoodsReturnedToSupplier> page = goodsReturnedToSupplierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goods-returned-to-suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /goods-returned-to-suppliers/:id : get the "id" goodsReturnedToSupplier.
     *
     * @param id the id of the goodsReturnedToSupplier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goodsReturnedToSupplier, or with status 404 (Not Found)
     */
    @GetMapping("/goods-returned-to-suppliers/{id}")
    @Timed
    public ResponseEntity<GoodsReturnedToSupplier> getGoodsReturnedToSupplier(@PathVariable Long id) {
        log.debug("REST request to get GoodsReturnedToSupplier : {}", id);
        GoodsReturnedToSupplier goodsReturnedToSupplier = goodsReturnedToSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(goodsReturnedToSupplier));
    }

    /**
     * DELETE  /goods-returned-to-suppliers/:id : delete the "id" goodsReturnedToSupplier.
     *
     * @param id the id of the goodsReturnedToSupplier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/goods-returned-to-suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteGoodsReturnedToSupplier(@PathVariable Long id) {
        log.debug("REST request to delete GoodsReturnedToSupplier : {}", id);
        goodsReturnedToSupplierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/goods-returned-to-suppliers?query=:query : search for the goodsReturnedToSupplier corresponding
     * to the query.
     *
     * @param query the query of the goodsReturnedToSupplier search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/goods-returned-to-suppliers")
    @Timed
    public ResponseEntity<List<GoodsReturnedToSupplier>> searchGoodsReturnedToSuppliers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GoodsReturnedToSuppliers for query {}", query);
        Page<GoodsReturnedToSupplier> page = goodsReturnedToSupplierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/goods-returned-to-suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
