package com.pricestool.pricestool.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of VgoitemSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class VgoitemSearchRepositoryMockConfiguration {

    @MockBean
    private VgoitemSearchRepository mockVgoitemSearchRepository;

}
