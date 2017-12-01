package com.pos.core.service;

import com.pos.core.domain.GoodsReceivedFromSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing GoodsReceivedFromSupplier.
 */
public interface GoodsReceivedFromSupplierService {

    /**
     * Save a goodsReceivedFromSupplier.
     *
     * @param goodsReceivedFromSupplier the entity to save
     * @return the persisted entity
     */
    GoodsReceivedFromSupplier save(GoodsReceivedFromSupplier goodsReceivedFromSupplier);

    /**
     * Get all the goodsReceivedFromSuppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GoodsReceivedFromSupplier> findAll(Pageable pageable);

    /**
     * Get the "id" goodsReceivedFromSupplier.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GoodsReceivedFromSupplier findOne(Long id);

    /**
     * Delete the "id" goodsReceivedFromSupplier.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the goodsReceivedFromSupplier corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GoodsReceivedFromSupplier> search(String query, Pageable pageable);
}
