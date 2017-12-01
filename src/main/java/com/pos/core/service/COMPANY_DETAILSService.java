package com.pos.core.service;

import com.pos.core.domain.COMPANY_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing COMPANY_DETAILS.
 */
public interface COMPANY_DETAILSService {

    /**
     * Save a cOMPANY_DETAILS.
     *
     * @param cOMPANY_DETAILS the entity to save
     * @return the persisted entity
     */
    COMPANY_DETAILS save(COMPANY_DETAILS cOMPANY_DETAILS);

    /**
     * Get all the cOMPANY_DETAILS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<COMPANY_DETAILS> findAll(Pageable pageable);

    /**
     * Get the "id" cOMPANY_DETAILS.
     *
     * @param id the id of the entity
     * @return the entity
     */
    COMPANY_DETAILS findOne(Long id);

    /**
     * Delete the "id" cOMPANY_DETAILS.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cOMPANY_DETAILS corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<COMPANY_DETAILS> search(String query, Pageable pageable);
}
