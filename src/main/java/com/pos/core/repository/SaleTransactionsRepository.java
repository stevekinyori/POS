package com.pos.core.repository;

import com.pos.core.domain.SaleTransactions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SaleTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleTransactionsRepository extends JpaRepository<SaleTransactions, Long> {

}
