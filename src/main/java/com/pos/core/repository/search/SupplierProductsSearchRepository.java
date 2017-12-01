package com.pos.core.repository.search;

import com.pos.core.domain.SupplierProducts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SupplierProducts entity.
 */
public interface SupplierProductsSearchRepository extends ElasticsearchRepository<SupplierProducts, Long> {
}
