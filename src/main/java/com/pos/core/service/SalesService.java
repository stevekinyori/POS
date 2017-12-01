package com.pos.core.service;

import com.pos.core.domain.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sales.
 */
public interface SalesService {

    /**
     * Save a sales.
     *
     * @param sales the entity to save
     * @return the persisted entity
     */
    Sales save(Sales sales);

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sales> findAll(Pageable pageable);

    /**
     * Get the "id" sales.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Sales findOne(Long id);

    /**
     * Delete the "id" sales.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sales corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sales> search(String query, Pageable pageable);
}
