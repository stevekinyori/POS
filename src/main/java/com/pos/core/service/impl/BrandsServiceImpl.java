package com.pos.core.service.impl;

import com.pos.core.service.BrandsService;
import com.pos.core.domain.Brands;
import com.pos.core.repository.BrandsRepository;
import com.pos.core.repository.search.BrandsSearchRepository;
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
 * Service Implementation for managing Brands.
 */
@Service
@Transactional
public class BrandsServiceImpl implements BrandsService{

    private final Logger log = LoggerFactory.getLogger(BrandsServiceImpl.class);

    private final BrandsRepository brandsRepository;

    private final BrandsSearchRepository brandsSearchRepository;

    public BrandsServiceImpl(BrandsRepository brandsRepository, BrandsSearchRepository brandsSearchRepository) {
        this.brandsRepository = brandsRepository;
        this.brandsSearchRepository = brandsSearchRepository;
    }

    /**
     * Save a brands.
     *
     * @param brands the entity to save
     * @return the persisted entity
     */
    @Override
    public Brands save(Brands brands) {
        log.debug("Request to save Brands : {}", brands);
        Brands result = brandsRepository.save(brands);
        brandsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the brands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Brands> findAll(Pageable pageable) {
        log.debug("Request to get all Brands");
        return brandsRepository.findAll(pageable);
    }


    /**
     *  get all the brands where Products is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Brands> findAllWhereProductsIsNull() {
        log.debug("Request to get all brands where Products is null");
        return StreamSupport
            .stream(brandsRepository.findAll().spliterator(), false)
            .filter(brands -> brands.getProducts() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one brands by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Brands findOne(Long id) {
        log.debug("Request to get Brands : {}", id);
        return brandsRepository.findOne(id);
    }

    /**
     * Delete the brands by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Brands : {}", id);
        brandsRepository.delete(id);
        brandsSearchRepository.delete(id);
    }

    /**
     * Search for the brands corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Brands> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Brands for query {}", query);
        Page<Brands> result = brandsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
