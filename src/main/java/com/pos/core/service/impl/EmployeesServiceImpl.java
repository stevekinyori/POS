package com.pos.core.service.impl;

import com.pos.core.service.EmployeesService;
import com.pos.core.domain.Employees;
import com.pos.core.repository.EmployeesRepository;
import com.pos.core.repository.search.EmployeesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Employees.
 */
@Service
@Transactional
public class EmployeesServiceImpl implements EmployeesService{

    private final Logger log = LoggerFactory.getLogger(EmployeesServiceImpl.class);

    private final EmployeesRepository employeesRepository;

    private final EmployeesSearchRepository employeesSearchRepository;

    public EmployeesServiceImpl(EmployeesRepository employeesRepository, EmployeesSearchRepository employeesSearchRepository) {
        this.employeesRepository = employeesRepository;
        this.employeesSearchRepository = employeesSearchRepository;
    }

    /**
     * Save a employees.
     *
     * @param employees the entity to save
     * @return the persisted entity
     */
    @Override
    public Employees save(Employees employees) {
        log.debug("Request to save Employees : {}", employees);
        Employees result = employeesRepository.save(employees);
        employeesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Employees> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeesRepository.findAll(pageable);
    }

    /**
     * Get one employees by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Employees findOne(Long id) {
        log.debug("Request to get Employees : {}", id);
        return employeesRepository.findOne(id);
    }

    /**
     * Delete the employees by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employees : {}", id);
        employeesRepository.delete(id);
        employeesSearchRepository.delete(id);
    }

    /**
     * Search for the employees corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Employees> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Employees for query {}", query);
        Page<Employees> result = employeesSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
