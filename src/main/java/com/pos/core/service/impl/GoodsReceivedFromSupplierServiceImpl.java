package com.pos.core.service.impl;

import com.pos.core.service.GoodsReceivedFromSupplierService;
import com.pos.core.domain.GoodsReceivedFromSupplier;
import com.pos.core.repository.GoodsReceivedFromSupplierRepository;
import com.pos.core.repository.search.GoodsReceivedFromSupplierSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GoodsReceivedFromSupplier.
 */
@Service
@Transactional
public class GoodsReceivedFromSupplierServiceImpl implements GoodsReceivedFromSupplierService{

    private final Logger log = LoggerFactory.getLogger(GoodsReceivedFromSupplierServiceImpl.class);

    private final GoodsReceivedFromSupplierRepository goodsReceivedFromSupplierRepository;

    private final GoodsReceivedFromSupplierSearchRepository goodsReceivedFromSupplierSearchRepository;

    public GoodsReceivedFromSupplierServiceImpl(GoodsReceivedFromSupplierRepository goodsReceivedFromSupplierRepository, GoodsReceivedFromSupplierSearchRepository goodsReceivedFromSupplierSearchRepository) {
        this.goodsReceivedFromSupplierRepository = goodsReceivedFromSupplierRepository;
        this.goodsReceivedFromSupplierSearchRepository = goodsReceivedFromSupplierSearchRepository;
    }

    /**
     * Save a goodsReceivedFromSupplier.
     *
     * @param goodsReceivedFromSupplier the entity to save
     * @return the persisted entity
     */
    @Override
    public GoodsReceivedFromSupplier save(GoodsReceivedFromSupplier goodsReceivedFromSupplier) {
        log.debug("Request to save GoodsReceivedFromSupplier : {}", goodsReceivedFromSupplier);
        GoodsReceivedFromSupplier result = goodsReceivedFromSupplierRepository.save(goodsReceivedFromSupplier);
        goodsReceivedFromSupplierSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the goodsReceivedFromSuppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsReceivedFromSupplier> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsReceivedFromSuppliers");
        return goodsReceivedFromSupplierRepository.findAll(pageable);
    }

    /**
     * Get one goodsReceivedFromSupplier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedFromSupplier findOne(Long id) {
        log.debug("Request to get GoodsReceivedFromSupplier : {}", id);
        return goodsReceivedFromSupplierRepository.findOne(id);
    }

    /**
     * Delete the goodsReceivedFromSupplier by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceivedFromSupplier : {}", id);
        goodsReceivedFromSupplierRepository.delete(id);
        goodsReceivedFromSupplierSearchRepository.delete(id);
    }

    /**
     * Search for the goodsReceivedFromSupplier corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsReceivedFromSupplier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GoodsReceivedFromSuppliers for query {}", query);
        Page<GoodsReceivedFromSupplier> result = goodsReceivedFromSupplierSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
