package com.pos.core.service;

import com.pos.core.domain.Brands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Brands.
 */
public interface BrandsService {

    /**
     * Save a brands.
     *
     * @param brands the entity to save
     * @return the persisted entity
     */
    Brands save(Brands brands);

    /**
     * Get all the brands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Brands> findAll(Pageable pageable);
    /**
     * Get all the BrandsDTO where Products is null.
     *
     * @return the list of entities
     */
    List<Brands> findAllWhereProductsIsNull();

    /**
     * Get the "id" brands.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Brands findOne(Long id);

    /**
     * Delete the "id" brands.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the brands corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Brands> search(String query, Pageable pageable);
}
