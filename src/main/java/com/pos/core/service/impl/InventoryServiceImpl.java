package com.pos.core.service.impl;

import com.pos.core.service.InventoryService;
import com.pos.core.domain.Inventory;
import com.pos.core.repository.InventoryRepository;
import com.pos.core.repository.search.InventorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Inventory.
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService{

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    private final InventorySearchRepository inventorySearchRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventorySearchRepository inventorySearchRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventorySearchRepository = inventorySearchRepository;
    }

    /**
     * Save a inventory.
     *
     * @param inventory the entity to save
     * @return the persisted entity
     */
    @Override
    public Inventory save(Inventory inventory) {
        log.debug("Request to save Inventory : {}", inventory);
        Inventory result = inventoryRepository.save(inventory);
        inventorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the inventories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> findAll(Pageable pageable) {
        log.debug("Request to get all Inventories");
        return inventoryRepository.findAll(pageable);
    }

    /**
     * Get one inventory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Inventory findOne(Long id) {
        log.debug("Request to get Inventory : {}", id);
        return inventoryRepository.findOne(id);
    }

    /**
     * Delete the inventory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inventory : {}", id);
        inventoryRepository.delete(id);
        inventorySearchRepository.delete(id);
    }

    /**
     * Search for the inventory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Inventories for query {}", query);
        Page<Inventory> result = inventorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
