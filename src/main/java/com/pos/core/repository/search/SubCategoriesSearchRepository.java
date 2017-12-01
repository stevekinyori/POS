package com.pos.core.repository.search;

import com.pos.core.domain.SubCategories;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SubCategories entity.
 */
public interface SubCategoriesSearchRepository extends ElasticsearchRepository<SubCategories, Long> {
}
