package com.pos.core.repository.search;

import com.pos.core.domain.ProductCategories;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductCategories entity.
 */
public interface ProductCategoriesSearchRepository extends ElasticsearchRepository<ProductCategories, Long> {
}
