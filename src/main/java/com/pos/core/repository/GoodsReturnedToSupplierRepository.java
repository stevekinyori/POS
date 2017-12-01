package com.pos.core.repository;

import com.pos.core.domain.GoodsReturnedToSupplier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GoodsReturnedToSupplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReturnedToSupplierRepository extends JpaRepository<GoodsReturnedToSupplier, Long> {

}
