package com.pos.core.repository;

import com.pos.core.domain.Brands;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Brands entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrandsRepository extends JpaRepository<Brands, Long> {

}
