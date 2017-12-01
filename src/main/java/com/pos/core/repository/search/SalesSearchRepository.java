package com.pos.core.repository.search;

import com.pos.core.domain.Sales;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sales entity.
 */
public interface SalesSearchRepository extends ElasticsearchRepository<Sales, Long> {
}
