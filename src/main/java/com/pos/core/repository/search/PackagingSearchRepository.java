package com.pos.core.repository.search;

import com.pos.core.domain.Packaging;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Packaging entity.
 */
public interface PackagingSearchRepository extends ElasticsearchRepository<Packaging, Long> {
}
