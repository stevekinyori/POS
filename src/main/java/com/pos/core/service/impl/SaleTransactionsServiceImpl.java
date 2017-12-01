package com.pos.core.service.impl;

import com.pos.core.service.SaleTransactionsService;
import com.pos.core.domain.SaleTransactions;
import com.pos.core.repository.SaleTransactionsRepository;
import com.pos.core.repository.search.SaleTransactionsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SaleTransactions.
 */
@Service
@Transactional
public class SaleTransactionsServiceImpl implements SaleTransactionsService{

    private final Logger log = LoggerFactory.getLogger(SaleTransactionsServiceImpl.class);

    private final SaleTransactionsRepository saleTransactionsRepository;

    private final SaleTransactionsSearchRepository saleTransactionsSearchRepository;

    public SaleTransactionsServiceImpl(SaleTransactionsRepository saleTransactionsRepository, SaleTransactionsSearchRepository saleTransactionsSearchRepository) {
        this.saleTransactionsRepository = saleTransactionsRepository;
        this.saleTransactionsSearchRepository = saleTransactionsSearchRepository;
    }

    /**
     * Save a saleTransactions.
     *
     * @param saleTransactions the entity to save
     * @return the persisted entity
     */
    @Override
    public SaleTransactions save(SaleTransactions saleTransactions) {
        log.debug("Request to save SaleTransactions : {}", saleTransactions);
        SaleTransactions result = saleTransactionsRepository.save(saleTransactions);
        saleTransactionsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the saleTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaleTransactions> findAll(Pageable pageable) {
        log.debug("Request to get all SaleTransactions");
        return saleTransactionsRepository.findAll(pageable);
    }

    /**
     * Get one saleTransactions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaleTransactions findOne(Long id) {
        log.debug("Request to get SaleTransactions : {}", id);
        return saleTransactionsRepository.findOne(id);
    }

    /**
     * Delete the saleTransactions by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SaleTransactions : {}", id);
        saleTransactionsRepository.delete(id);
        saleTransactionsSearchRepository.delete(id);
    }

    /**
     * Search for the saleTransactions corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaleTransactions> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SaleTransactions for query {}", query);
        Page<SaleTransactions> result = saleTransactionsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
