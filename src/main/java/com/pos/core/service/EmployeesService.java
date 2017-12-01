package com.pos.core.service;

import com.pos.core.domain.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Employees.
 */
public interface EmployeesService {

    /**
     * Save a employees.
     *
     * @param employees the entity to save
     * @return the persisted entity
     */
    Employees save(Employees employees);

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Employees> findAll(Pageable pageable);

    /**
     * Get the "id" employees.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Employees findOne(Long id);

    /**
     * Delete the "id" employees.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the employees corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Employees> search(String query, Pageable pageable);
}
