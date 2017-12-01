package com.pos.core.service.impl;

import com.pos.core.service.PackagingService;
import com.pos.core.domain.Packaging;
import com.pos.core.repository.PackagingRepository;
import com.pos.core.repository.search.PackagingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Packaging.
 */
@Service
@Transactional
public class PackagingServiceImpl implements PackagingService{

    private final Logger log = LoggerFactory.getLogger(PackagingServiceImpl.class);

    private final PackagingRepository packagingRepository;

    private final PackagingSearchRepository packagingSearchRepository;

    public PackagingServiceImpl(PackagingRepository packagingRepository, PackagingSearchRepository packagingSearchRepository) {
        this.packagingRepository = packagingRepository;
        this.packagingSearchRepository = packagingSearchRepository;
    }

    /**
     * Save a packaging.
     *
     * @param packaging the entity to save
     * @return the persisted entity
     */
    @Override
    public Packaging save(Packaging packaging) {
        log.debug("Request to save Packaging : {}", packaging);
        Packaging result = packagingRepository.save(packaging);
        packagingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the packagings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Packaging> findAll(Pageable pageable) {
        log.debug("Request to get all Packagings");
        return packagingRepository.findAll(pageable);
    }

    /**
     * Get one packaging by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Packaging findOne(Long id) {
        log.debug("Request to get Packaging : {}", id);
        return packagingRepository.findOne(id);
    }

    /**
     * Delete the packaging by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Packaging : {}", id);
        packagingRepository.delete(id);
        packagingSearchRepository.delete(id);
    }

    /**
     * Search for the packaging corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Packaging> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Packagings for query {}", query);
        Page<Packaging> result = packagingSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
