package com.pos.core.repository.search;

import com.pos.core.domain.Brands;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Brands entity.
 */
public interface BrandsSearchRepository extends ElasticsearchRepository<Brands, Long> {
}
