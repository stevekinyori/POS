package com.pos.core.repository;

import com.pos.core.domain.GoodsReceivedFromSupplier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GoodsReceivedFromSupplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceivedFromSupplierRepository extends JpaRepository<GoodsReceivedFromSupplier, Long> {

}
