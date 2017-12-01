package com.pos.core.repository.search;

import com.pos.core.domain.Employees;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Employees entity.
 */
public interface EmployeesSearchRepository extends ElasticsearchRepository<Employees, Long> {
}
