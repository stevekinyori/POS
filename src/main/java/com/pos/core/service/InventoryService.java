package com.pos.core.service;

import com.pos.core.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Inventory.
 */
public interface InventoryService {

    /**
     * Save a inventory.
     *
     * @param inventory the entity to save
     * @return the persisted entity
     */
    Inventory save(Inventory inventory);

    /**
     * Get all the inventories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Inventory> findAll(Pageable pageable);

    /**
     * Get the "id" inventory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Inventory findOne(Long id);

    /**
     * Delete the "id" inventory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the inventory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Inventory> search(String query, Pageable pageable);
}
