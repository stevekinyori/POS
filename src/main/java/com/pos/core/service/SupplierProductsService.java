package com.pos.core.service;

import com.pos.core.domain.SupplierProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SupplierProducts.
 */
public interface SupplierProductsService {

    /**
     * Save a supplierProducts.
     *
     * @param supplierProducts the entity to save
     * @return the persisted entity
     */
    SupplierProducts save(SupplierProducts supplierProducts);

    /**
     * Get all the supplierProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierProducts> findAll(Pageable pageable);

    /**
     * Get the "id" supplierProducts.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SupplierProducts findOne(Long id);

    /**
     * Delete the "id" supplierProducts.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the supplierProducts corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierProducts> search(String query, Pageable pageable);
}
