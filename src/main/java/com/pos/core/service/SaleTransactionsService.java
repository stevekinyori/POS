package com.pos.core.service;

import com.pos.core.domain.SaleTransactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SaleTransactions.
 */
public interface SaleTransactionsService {

    /**
     * Save a saleTransactions.
     *
     * @param saleTransactions the entity to save
     * @return the persisted entity
     */
    SaleTransactions save(SaleTransactions saleTransactions);

    /**
     * Get all the saleTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleTransactions> findAll(Pageable pageable);

    /**
     * Get the "id" saleTransactions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaleTransactions findOne(Long id);

    /**
     * Delete the "id" saleTransactions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the saleTransactions corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleTransactions> search(String query, Pageable pageable);
}
