package com.pos.core.service.impl;

import com.pos.core.service.GoodsReturnedToSupplierService;
import com.pos.core.domain.GoodsReturnedToSupplier;
import com.pos.core.repository.GoodsReturnedToSupplierRepository;
import com.pos.core.repository.search.GoodsReturnedToSupplierSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GoodsReturnedToSupplier.
 */
@Service
@Transactional
public class GoodsReturnedToSupplierServiceImpl implements GoodsReturnedToSupplierService{

    private final Logger log = LoggerFactory.getLogger(GoodsReturnedToSupplierServiceImpl.class);

    private final GoodsReturnedToSupplierRepository goodsReturnedToSupplierRepository;

    private final GoodsReturnedToSupplierSearchRepository goodsReturnedToSupplierSearchRepository;

    public GoodsReturnedToSupplierServiceImpl(GoodsReturnedToSupplierRepository goodsReturnedToSupplierRepository, GoodsReturnedToSupplierSearchRepository goodsReturnedToSupplierSearchRepository) {
        this.goodsReturnedToSupplierRepository = goodsReturnedToSupplierRepository;
        this.goodsReturnedToSupplierSearchRepository = goodsReturnedToSupplierSearchRepository;
    }

    /**
     * Save a goodsReturnedToSupplier.
     *
     * @param goodsReturnedToSupplier the entity to save
     * @return the persisted entity
     */
    @Override
    public GoodsReturnedToSupplier save(GoodsReturnedToSupplier goodsReturnedToSupplier) {
        log.debug("Request to save GoodsReturnedToSupplier : {}", goodsReturnedToSupplier);
        GoodsReturnedToSupplier result = goodsReturnedToSupplierRepository.save(goodsReturnedToSupplier);
        goodsReturnedToSupplierSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the goodsReturnedToSuppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsReturnedToSupplier> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsReturnedToSuppliers");
        return goodsReturnedToSupplierRepository.findAll(pageable);
    }

    /**
     * Get one goodsReturnedToSupplier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GoodsReturnedToSupplier findOne(Long id) {
        log.debug("Request to get GoodsReturnedToSupplier : {}", id);
        return goodsReturnedToSupplierRepository.findOne(id);
    }

    /**
     * Delete the goodsReturnedToSupplier by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GoodsReturnedToSupplier : {}", id);
        goodsReturnedToSupplierRepository.delete(id);
        goodsReturnedToSupplierSearchRepository.delete(id);
    }

    /**
     * Search for the goodsReturnedToSupplier corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsReturnedToSupplier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GoodsReturnedToSuppliers for query {}", query);
        Page<GoodsReturnedToSupplier> result = goodsReturnedToSupplierSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
