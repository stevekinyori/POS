package com.pos.core.repository;

import com.pos.core.domain.SupplierProducts;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SupplierProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierProductsRepository extends JpaRepository<SupplierProducts, Long> {

}
