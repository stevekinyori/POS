package com.pos.core.repository.search;

import com.pos.core.domain.COMPANY_DETAILS;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the COMPANY_DETAILS entity.
 */
public interface COMPANY_DETAILSSearchRepository extends ElasticsearchRepository<COMPANY_DETAILS, Long> {
}
