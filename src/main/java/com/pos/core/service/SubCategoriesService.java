package com.pos.core.service;

import com.pos.core.domain.SubCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SubCategories.
 */
public interface SubCategoriesService {

    /**
     * Save a subCategories.
     *
     * @param subCategories the entity to save
     * @return the persisted entity
     */
    SubCategories save(SubCategories subCategories);

    /**
     * Get all the subCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SubCategories> findAll(Pageable pageable);
    /**
     * Get all the SubCategoriesDTO where SubCategoryProducts is null.
     *
     * @return the list of entities
     */
    List<SubCategories> findAllWhereSubCategoryProductsIsNull();

    /**
     * Get the "id" subCategories.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SubCategories findOne(Long id);

    /**
     * Delete the "id" subCategories.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the subCategories corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SubCategories> search(String query, Pageable pageable);
}
