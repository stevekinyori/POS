package com.pos.core.repository;

import com.pos.core.domain.ProductCategories;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProductCategories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, Long> {

}
