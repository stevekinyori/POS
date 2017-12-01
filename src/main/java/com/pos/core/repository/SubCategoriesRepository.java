package com.pos.core.repository;

import com.pos.core.domain.SubCategories;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SubCategories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCategoriesRepository extends JpaRepository<SubCategories, Long> {

}
