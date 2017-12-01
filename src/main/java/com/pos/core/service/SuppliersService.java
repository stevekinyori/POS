package com.pos.core.service;

import com.pos.core.domain.Suppliers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Suppliers.
 */
public interface SuppliersService {

    /**
     * Save a suppliers.
     *
     * @param suppliers the entity to save
     * @return the persisted entity
     */
    Suppliers save(Suppliers suppliers);

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Suppliers> findAll(Pageable pageable);
    /**
     * Get all the SuppliersDTO where Products is null.
     *
     * @return the list of entities
     */
    List<Suppliers> findAllWhereProductsIsNull();
    /**
     * Get all the SuppliersDTO where GoodsReturned is null.
     *
     * @return the list of entities
     */
    List<Suppliers> findAllWhereGoodsReturnedIsNull();

    /**
     * Get the "id" suppliers.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Suppliers findOne(Long id);

    /**
     * Delete the "id" suppliers.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the suppliers corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Suppliers> search(String query, Pageable pageable);
}
