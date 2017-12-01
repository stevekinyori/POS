package com.pos.core.service;

import com.pos.core.domain.ProductCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProductCategories.
 */
public interface ProductCategoriesService {

    /**
     * Save a productCategories.
     *
     * @param productCategories the entity to save
     * @return the persisted entity
     */
    ProductCategories save(ProductCategories productCategories);

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProductCategories> findAll(Pageable pageable);

    /**
     * Get the "id" productCategories.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProductCategories findOne(Long id);

    /**
     * Delete the "id" productCategories.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the productCategories corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProductCategories> search(String query, Pageable pageable);
}
