package com.pos.core.service.impl;

import com.pos.core.service.ProductCategoriesService;
import com.pos.core.domain.ProductCategories;
import com.pos.core.repository.ProductCategoriesRepository;
import com.pos.core.repository.search.ProductCategoriesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductCategories.
 */
@Service
@Transactional
public class ProductCategoriesServiceImpl implements ProductCategoriesService{

    private final Logger log = LoggerFactory.getLogger(ProductCategoriesServiceImpl.class);

    private final ProductCategoriesRepository productCategoriesRepository;

    private final ProductCategoriesSearchRepository productCategoriesSearchRepository;

    public ProductCategoriesServiceImpl(ProductCategoriesRepository productCategoriesRepository, ProductCategoriesSearchRepository productCategoriesSearchRepository) {
        this.productCategoriesRepository = productCategoriesRepository;
        this.productCategoriesSearchRepository = productCategoriesSearchRepository;
    }

    /**
     * Save a productCategories.
     *
     * @param productCategories the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductCategories save(ProductCategories productCategories) {
        log.debug("Request to save ProductCategories : {}", productCategories);
        ProductCategories result = productCategoriesRepository.save(productCategories);
        productCategoriesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategories> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoriesRepository.findAll(pageable);
    }

    /**
     * Get one productCategories by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProductCategories findOne(Long id) {
        log.debug("Request to get ProductCategories : {}", id);
        return productCategoriesRepository.findOne(id);
    }

    /**
     * Delete the productCategories by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductCategories : {}", id);
        productCategoriesRepository.delete(id);
        productCategoriesSearchRepository.delete(id);
    }

    /**
     * Search for the productCategories corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategories> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductCategories for query {}", query);
        Page<ProductCategories> result = productCategoriesSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
