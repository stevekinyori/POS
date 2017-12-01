package com.pos.core.repository.search;

import com.pos.core.domain.Suppliers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Suppliers entity.
 */
public interface SuppliersSearchRepository extends ElasticsearchRepository<Suppliers, Long> {
}
