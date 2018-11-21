package com.pricestool.pricestool.repository.search;

import com.pricestool.pricestool.domain.Vgoitem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vgoitem entity.
 */
public interface VgoitemSearchRepository extends ElasticsearchRepository<Vgoitem, Long> {
}
