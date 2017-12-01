package com.pos.core.service.impl;

import com.pos.core.service.SuppliersService;
import com.pos.core.domain.Suppliers;
import com.pos.core.repository.SuppliersRepository;
import com.pos.core.repository.search.SuppliersSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Suppliers.
 */
@Service
@Transactional
public class SuppliersServiceImpl implements SuppliersService{

    private final Logger log = LoggerFactory.getLogger(SuppliersServiceImpl.class);

    private final SuppliersRepository suppliersRepository;

    private final SuppliersSearchRepository suppliersSearchRepository;

    public SuppliersServiceImpl(SuppliersRepository suppliersRepository, SuppliersSearchRepository suppliersSearchRepository) {
        this.suppliersRepository = suppliersRepository;
        this.suppliersSearchRepository = suppliersSearchRepository;
    }

    /**
     * Save a suppliers.
     *
     * @param suppliers the entity to save
     * @return the persisted entity
     */
    @Override
    public Suppliers save(Suppliers suppliers) {
        log.debug("Request to save Suppliers : {}", suppliers);
        Suppliers result = suppliersRepository.save(suppliers);
        suppliersSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Suppliers> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return suppliersRepository.findAll(pageable);
    }


    /**
     *  get all the suppliers where Products is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Suppliers> findAllWhereProductsIsNull() {
        log.debug("Request to get all suppliers where Products is null");
        return StreamSupport
            .stream(suppliersRepository.findAll().spliterator(), false)
            .filter(suppliers -> suppliers.getProducts() == null)
            .collect(Collectors.toList());
    }


    /**
     *  get all the suppliers where GoodsReturned is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Suppliers> findAllWhereGoodsReturnedIsNull() {
        log.debug("Request to get all suppliers where GoodsReturned is null");
        return StreamSupport
            .stream(suppliersRepository.findAll().spliterator(), false)
            .filter(suppliers -> suppliers.getGoodsReturned() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one suppliers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Suppliers findOne(Long id) {
        log.debug("Request to get Suppliers : {}", id);
        return suppliersRepository.findOne(id);
    }

    /**
     * Delete the suppliers by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Suppliers : {}", id);
        suppliersRepository.delete(id);
        suppliersSearchRepository.delete(id);
    }

    /**
     * Search for the suppliers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Suppliers> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Suppliers for query {}", query);
        Page<Suppliers> result = suppliersSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
