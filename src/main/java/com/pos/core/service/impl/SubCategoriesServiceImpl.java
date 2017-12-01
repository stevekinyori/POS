package com.pos.core.service.impl;

import com.pos.core.service.SubCategoriesService;
import com.pos.core.domain.SubCategories;
import com.pos.core.repository.SubCategoriesRepository;
import com.pos.core.repository.search.SubCategoriesSearchRepository;
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
 * Service Implementation for managing SubCategories.
 */
@Service
@Transactional
public class SubCategoriesServiceImpl implements SubCategoriesService{

    private final Logger log = LoggerFactory.getLogger(SubCategoriesServiceImpl.class);

    private final SubCategoriesRepository subCategoriesRepository;

    private final SubCategoriesSearchRepository subCategoriesSearchRepository;

    public SubCategoriesServiceImpl(SubCategoriesRepository subCategoriesRepository, SubCategoriesSearchRepository subCategoriesSearchRepository) {
        this.subCategoriesRepository = subCategoriesRepository;
        this.subCategoriesSearchRepository = subCategoriesSearchRepository;
    }

    /**
     * Save a subCategories.
     *
     * @param subCategories the entity to save
     * @return the persisted entity
     */
    @Override
    public SubCategories save(SubCategories subCategories) {
        log.debug("Request to save SubCategories : {}", subCategories);
        SubCategories result = subCategoriesRepository.save(subCategories);
        subCategoriesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the subCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubCategories> findAll(Pageable pageable) {
        log.debug("Request to get all SubCategories");
        return subCategoriesRepository.findAll(pageable);
    }


    /**
     *  get all the subCategories where SubCategoryProducts is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SubCategories> findAllWhereSubCategoryProductsIsNull() {
        log.debug("Request to get all subCategories where SubCategoryProducts is null");
        return StreamSupport
            .stream(subCategoriesRepository.findAll().spliterator(), false)
            .filter(subCategories -> subCategories.getSubCategoryProducts() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one subCategories by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SubCategories findOne(Long id) {
        log.debug("Request to get SubCategories : {}", id);
        return subCategoriesRepository.findOne(id);
    }

    /**
     * Delete the subCategories by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubCategories : {}", id);
        subCategoriesRepository.delete(id);
        subCategoriesSearchRepository.delete(id);
    }

    /**
     * Search for the subCategories corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubCategories> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SubCategories for query {}", query);
        Page<SubCategories> result = subCategoriesSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
