package com.pos.core.repository;

import com.pos.core.domain.COMPANY_DETAILS;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the COMPANY_DETAILS entity.
 */
@SuppressWarnings("unused")
@Repository
public interface COMPANY_DETAILSRepository extends JpaRepository<COMPANY_DETAILS, Long> {

}
