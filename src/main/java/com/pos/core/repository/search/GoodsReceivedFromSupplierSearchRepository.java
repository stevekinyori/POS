package com.pos.core.repository.search;

import com.pos.core.domain.GoodsReceivedFromSupplier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GoodsReceivedFromSupplier entity.
 */
public interface GoodsReceivedFromSupplierSearchRepository extends ElasticsearchRepository<GoodsReceivedFromSupplier, Long> {
}
