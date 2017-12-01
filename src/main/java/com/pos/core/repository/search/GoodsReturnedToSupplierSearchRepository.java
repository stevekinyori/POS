package com.pos.core.repository.search;

import com.pos.core.domain.GoodsReturnedToSupplier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GoodsReturnedToSupplier entity.
 */
public interface GoodsReturnedToSupplierSearchRepository extends ElasticsearchRepository<GoodsReturnedToSupplier, Long> {
}
