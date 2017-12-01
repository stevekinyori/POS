package com.pos.core.repository;

import com.pos.core.domain.Packaging;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Packaging entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagingRepository extends JpaRepository<Packaging, Long> {

}
