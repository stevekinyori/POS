package com.pos.core.service.impl;

import com.pos.core.service.SalesService;
import com.pos.core.domain.Sales;
import com.pos.core.repository.SalesRepository;
import com.pos.core.repository.search.SalesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Sales.
 */
@Service
@Transactional
public class SalesServiceImpl implements SalesService{

    private final Logger log = LoggerFactory.getLogger(SalesServiceImpl.class);

    private final SalesRepository salesRepository;

    private final SalesSearchRepository salesSearchRepository;

    public SalesServiceImpl(SalesRepository salesRepository, SalesSearchRepository salesSearchRepository) {
        this.salesRepository = salesRepository;
        this.salesSearchRepository = salesSearchRepository;
    }

    /**
     * Save a sales.
     *
     * @param sales the entity to save
     * @return the persisted entity
     */
    @Override
    public Sales save(Sales sales) {
        log.debug("Request to save Sales : {}", sales);
        Sales result = salesRepository.save(sales);
        salesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sales> findAll(Pageable pageable) {
        log.debug("Request to get all Sales");
        return salesRepository.findAll(pageable);
    }

    /**
     * Get one sales by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Sales findOne(Long id) {
        log.debug("Request to get Sales : {}", id);
        return salesRepository.findOne(id);
    }

    /**
     * Delete the sales by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sales : {}", id);
        salesRepository.delete(id);
        salesSearchRepository.delete(id);
    }

    /**
     * Search for the sales corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sales> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sales for query {}", query);
        Page<Sales> result = salesSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
