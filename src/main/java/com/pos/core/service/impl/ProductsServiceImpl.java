package com.pos.core.service.impl;

import com.pos.core.service.ProductsService;
import com.pos.core.domain.Products;
import com.pos.core.repository.ProductsRepository;
import com.pos.core.repository.search.ProductsSearchRepository;
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
 * Service Implementation for managing Products.
 */
@Service
@Transactional
public class ProductsServiceImpl implements ProductsService{

    private final Logger log = LoggerFactory.getLogger(ProductsServiceImpl.class);

    private final ProductsRepository productsRepository;

    private final ProductsSearchRepository productsSearchRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository, ProductsSearchRepository productsSearchRepository) {
        this.productsRepository = productsRepository;
        this.productsSearchRepository = productsSearchRepository;
    }

    /**
     * Save a products.
     *
     * @param products the entity to save
     * @return the persisted entity
     */
    @Override
    public Products save(Products products) {
        log.debug("Request to save Products : {}", products);
        Products result = productsRepository.save(products);
        productsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Products> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productsRepository.findAll(pageable);
    }


    /**
     *  get all the products where Suppliers is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Products> findAllWhereSuppliersIsNull() {
        log.debug("Request to get all products where Suppliers is null");
        return StreamSupport
            .stream(productsRepository.findAll().spliterator(), false)
            .filter(products -> products.getSuppliers() == null)
            .collect(Collectors.toList());
    }


    /**
     *  get all the products where ReceivedFromSupplier is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Products> findAllWhereReceivedFromSupplierIsNull() {
        log.debug("Request to get all products where ReceivedFromSupplier is null");
        return StreamSupport
            .stream(productsRepository.findAll().spliterator(), false)
            .filter(products -> products.getReceivedFromSupplier() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one products by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Products findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        return productsRepository.findOne(id);
    }

    /**
     * Delete the products by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.delete(id);
        productsSearchRepository.delete(id);
    }

    /**
     * Search for the products corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Products> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        Page<Products> result = productsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
