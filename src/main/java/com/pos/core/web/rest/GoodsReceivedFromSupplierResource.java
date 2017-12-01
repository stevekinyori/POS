package com.pos.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pos.core.domain.GoodsReceivedFromSupplier;
import com.pos.core.service.GoodsReceivedFromSupplierService;
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
 * REST controller for managing GoodsReceivedFromSupplier.
 */
@RestController
@RequestMapping("/api")
public class GoodsReceivedFromSupplierResource {

    private final Logger log = LoggerFactory.getLogger(GoodsReceivedFromSupplierResource.class);

    private static final String ENTITY_NAME = "goodsReceivedFromSupplier";

    private final GoodsReceivedFromSupplierService goodsReceivedFromSupplierService;

    public GoodsReceivedFromSupplierResource(GoodsReceivedFromSupplierService goodsReceivedFromSupplierService) {
        this.goodsReceivedFromSupplierService = goodsReceivedFromSupplierService;
    }

    /**
     * POST  /goods-received-from-suppliers : Create a new goodsReceivedFromSupplier.
     *
     * @param goodsReceivedFromSupplier the goodsReceivedFromSupplier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goodsReceivedFromSupplier, or with status 400 (Bad Request) if the goodsReceivedFromSupplier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/goods-received-from-suppliers")
    @Timed
    public ResponseEntity<GoodsReceivedFromSupplier> createGoodsReceivedFromSupplier(@RequestBody GoodsReceivedFromSupplier goodsReceivedFromSupplier) throws URISyntaxException {
        log.debug("REST request to save GoodsReceivedFromSupplier : {}", goodsReceivedFromSupplier);
        if (goodsReceivedFromSupplier.getId() != null) {
            throw new BadRequestAlertException("A new goodsReceivedFromSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoodsReceivedFromSupplier result = goodsReceivedFromSupplierService.save(goodsReceivedFromSupplier);
        return ResponseEntity.created(new URI("/api/goods-received-from-suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goods-received-from-suppliers : Updates an existing goodsReceivedFromSupplier.
     *
     * @param goodsReceivedFromSupplier the goodsReceivedFromSupplier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goodsReceivedFromSupplier,
     * or with status 400 (Bad Request) if the goodsReceivedFromSupplier is not valid,
     * or with status 500 (Internal Server Error) if the goodsReceivedFromSupplier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/goods-received-from-suppliers")
    @Timed
    public ResponseEntity<GoodsReceivedFromSupplier> updateGoodsReceivedFromSupplier(@RequestBody GoodsReceivedFromSupplier goodsReceivedFromSupplier) throws URISyntaxException {
        log.debug("REST request to update GoodsReceivedFromSupplier : {}", goodsReceivedFromSupplier);
        if (goodsReceivedFromSupplier.getId() == null) {
            return createGoodsReceivedFromSupplier(goodsReceivedFromSupplier);
        }
        GoodsReceivedFromSupplier result = goodsReceivedFromSupplierService.save(goodsReceivedFromSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, goodsReceivedFromSupplier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goods-received-from-suppliers : get all the goodsReceivedFromSuppliers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of goodsReceivedFromSuppliers in body
     */
    @GetMapping("/goods-received-from-suppliers")
    @Timed
    public ResponseEntity<List<GoodsReceivedFromSupplier>> getAllGoodsReceivedFromSuppliers(Pageable pageable) {
        log.debug("REST request to get a page of GoodsReceivedFromSuppliers");
        Page<GoodsReceivedFromSupplier> page = goodsReceivedFromSupplierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goods-received-from-suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /goods-received-from-suppliers/:id : get the "id" goodsReceivedFromSupplier.
     *
     * @param id the id of the goodsReceivedFromSupplier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goodsReceivedFromSupplier, or with status 404 (Not Found)
     */
    @GetMapping("/goods-received-from-suppliers/{id}")
    @Timed
    public ResponseEntity<GoodsReceivedFromSupplier> getGoodsReceivedFromSupplier(@PathVariable Long id) {
        log.debug("REST request to get GoodsReceivedFromSupplier : {}", id);
        GoodsReceivedFromSupplier goodsReceivedFromSupplier = goodsReceivedFromSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(goodsReceivedFromSupplier));
    }

    /**
     * DELETE  /goods-received-from-suppliers/:id : delete the "id" goodsReceivedFromSupplier.
     *
     * @param id the id of the goodsReceivedFromSupplier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/goods-received-from-suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteGoodsReceivedFromSupplier(@PathVariable Long id) {
        log.debug("REST request to delete GoodsReceivedFromSupplier : {}", id);
        goodsReceivedFromSupplierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/goods-received-from-suppliers?query=:query : search for the goodsReceivedFromSupplier corresponding
     * to the query.
     *
     * @param query the query of the goodsReceivedFromSupplier search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/goods-received-from-suppliers")
    @Timed
    public ResponseEntity<List<GoodsReceivedFromSupplier>> searchGoodsReceivedFromSuppliers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GoodsReceivedFromSuppliers for query {}", query);
        Page<GoodsReceivedFromSupplier> page = goodsReceivedFromSupplierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/goods-received-from-suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
