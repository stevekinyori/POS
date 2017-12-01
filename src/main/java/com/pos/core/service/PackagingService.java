package com.pos.core.service;

import com.pos.core.domain.Packaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Packaging.
 */
public interface PackagingService {

    /**
     * Save a packaging.
     *
     * @param packaging the entity to save
     * @return the persisted entity
     */
    Packaging save(Packaging packaging);

    /**
     * Get all the packagings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Packaging> findAll(Pageable pageable);

    /**
     * Get the "id" packaging.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Packaging findOne(Long id);

    /**
     * Delete the "id" packaging.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the packaging corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Packaging> search(String query, Pageable pageable);
}
