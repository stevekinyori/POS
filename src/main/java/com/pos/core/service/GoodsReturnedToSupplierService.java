package com.pos.core.service;

import com.pos.core.domain.GoodsReturnedToSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing GoodsReturnedToSupplier.
 */
public interface GoodsReturnedToSupplierService {

    /**
     * Save a goodsReturnedToSupplier.
     *
     * @param goodsReturnedToSupplier the entity to save
     * @return the persisted entity
     */
    GoodsReturnedToSupplier save(GoodsReturnedToSupplier goodsReturnedToSupplier);

    /**
     * Get all the goodsReturnedToSuppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GoodsReturnedToSupplier> findAll(Pageable pageable);

    /**
     * Get the "id" goodsReturnedToSupplier.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GoodsReturnedToSupplier findOne(Long id);

    /**
     * Delete the "id" goodsReturnedToSupplier.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the goodsReturnedToSupplier corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GoodsReturnedToSupplier> search(String query, Pageable pageable);
}
