package com.pos.core.service.impl;

import com.pos.core.service.SupplierProductsService;
import com.pos.core.domain.SupplierProducts;
import com.pos.core.repository.SupplierProductsRepository;
import com.pos.core.repository.search.SupplierProductsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SupplierProducts.
 */
@Service
@Transactional
public class SupplierProductsServiceImpl implements SupplierProductsService{

    private final Logger log = LoggerFactory.getLogger(SupplierProductsServiceImpl.class);

    private final SupplierProductsRepository supplierProductsRepository;

    private final SupplierProductsSearchRepository supplierProductsSearchRepository;

    public SupplierProductsServiceImpl(SupplierProductsRepository supplierProductsRepository, SupplierProductsSearchRepository supplierProductsSearchRepository) {
        this.supplierProductsRepository = supplierProductsRepository;
        this.supplierProductsSearchRepository = supplierProductsSearchRepository;
    }

    /**
     * Save a supplierProducts.
     *
     * @param supplierProducts the entity to save
     * @return the persisted entity
     */
    @Override
    public SupplierProducts save(SupplierProducts supplierProducts) {
        log.debug("Request to save SupplierProducts : {}", supplierProducts);
        SupplierProducts result = supplierProductsRepository.save(supplierProducts);
        supplierProductsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the supplierProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierProducts> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierProducts");
        return supplierProductsRepository.findAll(pageable);
    }

    /**
     * Get one supplierProducts by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SupplierProducts findOne(Long id) {
        log.debug("Request to get SupplierProducts : {}", id);
        return supplierProductsRepository.findOne(id);
    }

    /**
     * Delete the supplierProducts by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SupplierProducts : {}", id);
        supplierProductsRepository.delete(id);
        supplierProductsSearchRepository.delete(id);
    }

    /**
     * Search for the supplierProducts corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierProducts> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SupplierProducts for query {}", query);
        Page<SupplierProducts> result = supplierProductsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
