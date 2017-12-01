package com.pos.core.service.impl;

import com.pos.core.service.COMPANY_DETAILSService;
import com.pos.core.domain.COMPANY_DETAILS;
import com.pos.core.repository.COMPANY_DETAILSRepository;
import com.pos.core.repository.search.COMPANY_DETAILSSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing COMPANY_DETAILS.
 */
@Service
@Transactional
public class COMPANY_DETAILSServiceImpl implements COMPANY_DETAILSService{

    private final Logger log = LoggerFactory.getLogger(COMPANY_DETAILSServiceImpl.class);

    private final COMPANY_DETAILSRepository cOMPANY_DETAILSRepository;

    private final COMPANY_DETAILSSearchRepository cOMPANY_DETAILSSearchRepository;

    public COMPANY_DETAILSServiceImpl(COMPANY_DETAILSRepository cOMPANY_DETAILSRepository, COMPANY_DETAILSSearchRepository cOMPANY_DETAILSSearchRepository) {
        this.cOMPANY_DETAILSRepository = cOMPANY_DETAILSRepository;
        this.cOMPANY_DETAILSSearchRepository = cOMPANY_DETAILSSearchRepository;
    }

    /**
     * Save a cOMPANY_DETAILS.
     *
     * @param cOMPANY_DETAILS the entity to save
     * @return the persisted entity
     */
    @Override
    public COMPANY_DETAILS save(COMPANY_DETAILS cOMPANY_DETAILS) {
        log.debug("Request to save COMPANY_DETAILS : {}", cOMPANY_DETAILS);
        COMPANY_DETAILS result = cOMPANY_DETAILSRepository.save(cOMPANY_DETAILS);
        cOMPANY_DETAILSSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cOMPANY_DETAILS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<COMPANY_DETAILS> findAll(Pageable pageable) {
        log.debug("Request to get all COMPANY_DETAILS");
        return cOMPANY_DETAILSRepository.findAll(pageable);
    }

    /**
     * Get one cOMPANY_DETAILS by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public COMPANY_DETAILS findOne(Long id) {
        log.debug("Request to get COMPANY_DETAILS : {}", id);
        return cOMPANY_DETAILSRepository.findOne(id);
    }

    /**
     * Delete the cOMPANY_DETAILS by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete COMPANY_DETAILS : {}", id);
        cOMPANY_DETAILSRepository.delete(id);
        cOMPANY_DETAILSSearchRepository.delete(id);
    }

    /**
     * Search for the cOMPANY_DETAILS corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<COMPANY_DETAILS> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of COMPANY_DETAILS for query {}", query);
        Page<COMPANY_DETAILS> result = cOMPANY_DETAILSSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
