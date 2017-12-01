package com.pos.core.repository.search;

import com.pos.core.domain.SaleTransactions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaleTransactions entity.
 */
public interface SaleTransactionsSearchRepository extends ElasticsearchRepository<SaleTransactions, Long> {
}
