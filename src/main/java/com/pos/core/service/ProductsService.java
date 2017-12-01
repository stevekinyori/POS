package com.pos.core.service;

import com.pos.core.domain.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Products.
 */
public interface ProductsService {

    /**
     * Save a products.
     *
     * @param products the entity to save
     * @return the persisted entity
     */
    Products save(Products products);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Products> findAll(Pageable pageable);
    /**
     * Get all the ProductsDTO where Suppliers is null.
     *
     * @return the list of entities
     */
    List<Products> findAllWhereSuppliersIsNull();
    /**
     * Get all the ProductsDTO where ReceivedFromSupplier is null.
     *
     * @return the list of entities
     */
    List<Products> findAllWhereReceivedFromSupplierIsNull();

    /**
     * Get the "id" products.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Products findOne(Long id);

    /**
     * Delete the "id" products.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the products corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Products> search(String query, Pageable pageable);
}
