package com.pricestool.pricestool.web.rest;

import com.pricestool.pricestool.PricestoolApp;

import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.repository.VgoitemRepository;
import com.pricestool.pricestool.repository.search.VgoitemSearchRepository;
import com.pricestool.pricestool.service.VgoitemService;
import com.pricestool.pricestool.web.rest.errors.ExceptionTranslator;
import com.pricestool.pricestool.service.dto.VgoitemCriteria;
import com.pricestool.pricestool.service.VgoitemQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.pricestool.pricestool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VgoitemResource REST controller.
 *
 * @see VgoitemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PricestoolApp.class)
public class VgoitemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_OP_7_DAY = 1;
    private static final Integer UPDATED_OP_7_DAY = 2;

    private static final Integer DEFAULT_OP_30_DAY = 1;
    private static final Integer UPDATED_OP_30_DAY = 2;

    private static final Integer DEFAULT_SALES = 1;
    private static final Integer UPDATED_SALES = 2;

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;

    private static final Integer DEFAULT_MIN_PRICE = 1;
    private static final Integer UPDATED_MIN_PRICE = 2;

    @Autowired
    private VgoitemRepository vgoitemRepository;

    @Autowired
    private VgoitemService vgoitemService;

    /**
     * This repository is mocked in the com.pricestool.pricestool.repository.search test package.
     *
     * @see com.pricestool.pricestool.repository.search.VgoitemSearchRepositoryMockConfiguration
     */
    @Autowired
    private VgoitemSearchRepository mockVgoitemSearchRepository;

    @Autowired
    private VgoitemQueryService vgoitemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVgoitemMockMvc;

    private Vgoitem vgoitem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VgoitemResource vgoitemResource = new VgoitemResource(vgoitemService, vgoitemQueryService);
        this.restVgoitemMockMvc = MockMvcBuilders.standaloneSetup(vgoitemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vgoitem createEntity(EntityManager em) {
        Vgoitem vgoitem = new Vgoitem()
            .name(DEFAULT_NAME)
            .op7day(DEFAULT_OP_7_DAY)
            .op30day(DEFAULT_OP_30_DAY)
            .sales(DEFAULT_SALES)
            .qty(DEFAULT_QTY)
            .minPrice(DEFAULT_MIN_PRICE);
        return vgoitem;
    }

    @Before
    public void initTest() {
        vgoitem = createEntity(em);
    }

    @Test
    @Transactional
    public void createVgoitem() throws Exception {
        int databaseSizeBeforeCreate = vgoitemRepository.findAll().size();

        // Create the Vgoitem
        restVgoitemMockMvc.perform(post("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vgoitem)))
            .andExpect(status().isCreated());

        // Validate the Vgoitem in the database
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeCreate + 1);
        Vgoitem testVgoitem = vgoitemList.get(vgoitemList.size() - 1);
        assertThat(testVgoitem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVgoitem.getOp7day()).isEqualTo(DEFAULT_OP_7_DAY);
        assertThat(testVgoitem.getOp30day()).isEqualTo(DEFAULT_OP_30_DAY);
        assertThat(testVgoitem.getSales()).isEqualTo(DEFAULT_SALES);
        assertThat(testVgoitem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testVgoitem.getMinPrice()).isEqualTo(DEFAULT_MIN_PRICE);

        // Validate the Vgoitem in Elasticsearch
        verify(mockVgoitemSearchRepository, times(1)).save(testVgoitem);
    }

    @Test
    @Transactional
    public void createVgoitemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vgoitemRepository.findAll().size();

        // Create the Vgoitem with an existing ID
        vgoitem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVgoitemMockMvc.perform(post("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vgoitem)))
            .andExpect(status().isBadRequest());

        // Validate the Vgoitem in the database
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vgoitem in Elasticsearch
        verify(mockVgoitemSearchRepository, times(0)).save(vgoitem);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vgoitemRepository.findAll().size();
        // set the field null
        vgoitem.setName(null);

        // Create the Vgoitem, which fails.

        restVgoitemMockMvc.perform(post("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vgoitem)))
            .andExpect(status().isBadRequest());

        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVgoitems() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList
        restVgoitemMockMvc.perform(get("/api/vgoitems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vgoitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].op7day").value(hasItem(DEFAULT_OP_7_DAY)))
            .andExpect(jsonPath("$.[*].op30day").value(hasItem(DEFAULT_OP_30_DAY)))
            .andExpect(jsonPath("$.[*].sales").value(hasItem(DEFAULT_SALES)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].minPrice").value(hasItem(DEFAULT_MIN_PRICE)));
    }
    
    @Test
    @Transactional
    public void getVgoitem() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get the vgoitem
        restVgoitemMockMvc.perform(get("/api/vgoitems/{id}", vgoitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vgoitem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.op7day").value(DEFAULT_OP_7_DAY))
            .andExpect(jsonPath("$.op30day").value(DEFAULT_OP_30_DAY))
            .andExpect(jsonPath("$.sales").value(DEFAULT_SALES))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.minPrice").value(DEFAULT_MIN_PRICE));
    }

    @Test
    @Transactional
    public void getAllVgoitemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where name equals to DEFAULT_NAME
        defaultVgoitemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vgoitemList where name equals to UPDATED_NAME
        defaultVgoitemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVgoitemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vgoitemList where name equals to UPDATED_NAME
        defaultVgoitemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where name is not null
        defaultVgoitemShouldBeFound("name.specified=true");

        // Get all the vgoitemList where name is null
        defaultVgoitemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp7dayIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op7day equals to DEFAULT_OP_7_DAY
        defaultVgoitemShouldBeFound("op7day.equals=" + DEFAULT_OP_7_DAY);

        // Get all the vgoitemList where op7day equals to UPDATED_OP_7_DAY
        defaultVgoitemShouldNotBeFound("op7day.equals=" + UPDATED_OP_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp7dayIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op7day in DEFAULT_OP_7_DAY or UPDATED_OP_7_DAY
        defaultVgoitemShouldBeFound("op7day.in=" + DEFAULT_OP_7_DAY + "," + UPDATED_OP_7_DAY);

        // Get all the vgoitemList where op7day equals to UPDATED_OP_7_DAY
        defaultVgoitemShouldNotBeFound("op7day.in=" + UPDATED_OP_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp7dayIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op7day is not null
        defaultVgoitemShouldBeFound("op7day.specified=true");

        // Get all the vgoitemList where op7day is null
        defaultVgoitemShouldNotBeFound("op7day.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp7dayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op7day greater than or equals to DEFAULT_OP_7_DAY
        defaultVgoitemShouldBeFound("op7day.greaterOrEqualThan=" + DEFAULT_OP_7_DAY);

        // Get all the vgoitemList where op7day greater than or equals to UPDATED_OP_7_DAY
        defaultVgoitemShouldNotBeFound("op7day.greaterOrEqualThan=" + UPDATED_OP_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp7dayIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op7day less than or equals to DEFAULT_OP_7_DAY
        defaultVgoitemShouldNotBeFound("op7day.lessThan=" + DEFAULT_OP_7_DAY);

        // Get all the vgoitemList where op7day less than or equals to UPDATED_OP_7_DAY
        defaultVgoitemShouldBeFound("op7day.lessThan=" + UPDATED_OP_7_DAY);
    }


    @Test
    @Transactional
    public void getAllVgoitemsByOp30dayIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op30day equals to DEFAULT_OP_30_DAY
        defaultVgoitemShouldBeFound("op30day.equals=" + DEFAULT_OP_30_DAY);

        // Get all the vgoitemList where op30day equals to UPDATED_OP_30_DAY
        defaultVgoitemShouldNotBeFound("op30day.equals=" + UPDATED_OP_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp30dayIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op30day in DEFAULT_OP_30_DAY or UPDATED_OP_30_DAY
        defaultVgoitemShouldBeFound("op30day.in=" + DEFAULT_OP_30_DAY + "," + UPDATED_OP_30_DAY);

        // Get all the vgoitemList where op30day equals to UPDATED_OP_30_DAY
        defaultVgoitemShouldNotBeFound("op30day.in=" + UPDATED_OP_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp30dayIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op30day is not null
        defaultVgoitemShouldBeFound("op30day.specified=true");

        // Get all the vgoitemList where op30day is null
        defaultVgoitemShouldNotBeFound("op30day.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp30dayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op30day greater than or equals to DEFAULT_OP_30_DAY
        defaultVgoitemShouldBeFound("op30day.greaterOrEqualThan=" + DEFAULT_OP_30_DAY);

        // Get all the vgoitemList where op30day greater than or equals to UPDATED_OP_30_DAY
        defaultVgoitemShouldNotBeFound("op30day.greaterOrEqualThan=" + UPDATED_OP_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByOp30dayIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where op30day less than or equals to DEFAULT_OP_30_DAY
        defaultVgoitemShouldNotBeFound("op30day.lessThan=" + DEFAULT_OP_30_DAY);

        // Get all the vgoitemList where op30day less than or equals to UPDATED_OP_30_DAY
        defaultVgoitemShouldBeFound("op30day.lessThan=" + UPDATED_OP_30_DAY);
    }


    @Test
    @Transactional
    public void getAllVgoitemsBySalesIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where sales equals to DEFAULT_SALES
        defaultVgoitemShouldBeFound("sales.equals=" + DEFAULT_SALES);

        // Get all the vgoitemList where sales equals to UPDATED_SALES
        defaultVgoitemShouldNotBeFound("sales.equals=" + UPDATED_SALES);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySalesIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where sales in DEFAULT_SALES or UPDATED_SALES
        defaultVgoitemShouldBeFound("sales.in=" + DEFAULT_SALES + "," + UPDATED_SALES);

        // Get all the vgoitemList where sales equals to UPDATED_SALES
        defaultVgoitemShouldNotBeFound("sales.in=" + UPDATED_SALES);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySalesIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where sales is not null
        defaultVgoitemShouldBeFound("sales.specified=true");

        // Get all the vgoitemList where sales is null
        defaultVgoitemShouldNotBeFound("sales.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySalesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where sales greater than or equals to DEFAULT_SALES
        defaultVgoitemShouldBeFound("sales.greaterOrEqualThan=" + DEFAULT_SALES);

        // Get all the vgoitemList where sales greater than or equals to UPDATED_SALES
        defaultVgoitemShouldNotBeFound("sales.greaterOrEqualThan=" + UPDATED_SALES);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySalesIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where sales less than or equals to DEFAULT_SALES
        defaultVgoitemShouldNotBeFound("sales.lessThan=" + DEFAULT_SALES);

        // Get all the vgoitemList where sales less than or equals to UPDATED_SALES
        defaultVgoitemShouldBeFound("sales.lessThan=" + UPDATED_SALES);
    }


    @Test
    @Transactional
    public void getAllVgoitemsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where qty equals to DEFAULT_QTY
        defaultVgoitemShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the vgoitemList where qty equals to UPDATED_QTY
        defaultVgoitemShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultVgoitemShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the vgoitemList where qty equals to UPDATED_QTY
        defaultVgoitemShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where qty is not null
        defaultVgoitemShouldBeFound("qty.specified=true");

        // Get all the vgoitemList where qty is null
        defaultVgoitemShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where qty greater than or equals to DEFAULT_QTY
        defaultVgoitemShouldBeFound("qty.greaterOrEqualThan=" + DEFAULT_QTY);

        // Get all the vgoitemList where qty greater than or equals to UPDATED_QTY
        defaultVgoitemShouldNotBeFound("qty.greaterOrEqualThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where qty less than or equals to DEFAULT_QTY
        defaultVgoitemShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the vgoitemList where qty less than or equals to UPDATED_QTY
        defaultVgoitemShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }


    @Test
    @Transactional
    public void getAllVgoitemsByMinPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where minPrice equals to DEFAULT_MIN_PRICE
        defaultVgoitemShouldBeFound("minPrice.equals=" + DEFAULT_MIN_PRICE);

        // Get all the vgoitemList where minPrice equals to UPDATED_MIN_PRICE
        defaultVgoitemShouldNotBeFound("minPrice.equals=" + UPDATED_MIN_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByMinPriceIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where minPrice in DEFAULT_MIN_PRICE or UPDATED_MIN_PRICE
        defaultVgoitemShouldBeFound("minPrice.in=" + DEFAULT_MIN_PRICE + "," + UPDATED_MIN_PRICE);

        // Get all the vgoitemList where minPrice equals to UPDATED_MIN_PRICE
        defaultVgoitemShouldNotBeFound("minPrice.in=" + UPDATED_MIN_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByMinPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where minPrice is not null
        defaultVgoitemShouldBeFound("minPrice.specified=true");

        // Get all the vgoitemList where minPrice is null
        defaultVgoitemShouldNotBeFound("minPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByMinPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where minPrice greater than or equals to DEFAULT_MIN_PRICE
        defaultVgoitemShouldBeFound("minPrice.greaterOrEqualThan=" + DEFAULT_MIN_PRICE);

        // Get all the vgoitemList where minPrice greater than or equals to UPDATED_MIN_PRICE
        defaultVgoitemShouldNotBeFound("minPrice.greaterOrEqualThan=" + UPDATED_MIN_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByMinPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where minPrice less than or equals to DEFAULT_MIN_PRICE
        defaultVgoitemShouldNotBeFound("minPrice.lessThan=" + DEFAULT_MIN_PRICE);

        // Get all the vgoitemList where minPrice less than or equals to UPDATED_MIN_PRICE
        defaultVgoitemShouldBeFound("minPrice.lessThan=" + UPDATED_MIN_PRICE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVgoitemShouldBeFound(String filter) throws Exception {
        restVgoitemMockMvc.perform(get("/api/vgoitems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vgoitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].op7day").value(hasItem(DEFAULT_OP_7_DAY)))
            .andExpect(jsonPath("$.[*].op30day").value(hasItem(DEFAULT_OP_30_DAY)))
            .andExpect(jsonPath("$.[*].sales").value(hasItem(DEFAULT_SALES)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].minPrice").value(hasItem(DEFAULT_MIN_PRICE)));

        // Check, that the count call also returns 1
        restVgoitemMockMvc.perform(get("/api/vgoitems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVgoitemShouldNotBeFound(String filter) throws Exception {
        restVgoitemMockMvc.perform(get("/api/vgoitems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVgoitemMockMvc.perform(get("/api/vgoitems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVgoitem() throws Exception {
        // Get the vgoitem
        restVgoitemMockMvc.perform(get("/api/vgoitems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVgoitem() throws Exception {
        // Initialize the database
        vgoitemService.save(vgoitem);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockVgoitemSearchRepository);

        int databaseSizeBeforeUpdate = vgoitemRepository.findAll().size();

        // Update the vgoitem
        Vgoitem updatedVgoitem = vgoitemRepository.findById(vgoitem.getId()).get();
        // Disconnect from session so that the updates on updatedVgoitem are not directly saved in db
        em.detach(updatedVgoitem);
        updatedVgoitem
            .name(UPDATED_NAME)
            .op7day(UPDATED_OP_7_DAY)
            .op30day(UPDATED_OP_30_DAY)
            .sales(UPDATED_SALES)
            .qty(UPDATED_QTY)
            .minPrice(UPDATED_MIN_PRICE);

        restVgoitemMockMvc.perform(put("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVgoitem)))
            .andExpect(status().isOk());

        // Validate the Vgoitem in the database
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeUpdate);
        Vgoitem testVgoitem = vgoitemList.get(vgoitemList.size() - 1);
        assertThat(testVgoitem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVgoitem.getOp7day()).isEqualTo(UPDATED_OP_7_DAY);
        assertThat(testVgoitem.getOp30day()).isEqualTo(UPDATED_OP_30_DAY);
        assertThat(testVgoitem.getSales()).isEqualTo(UPDATED_SALES);
        assertThat(testVgoitem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testVgoitem.getMinPrice()).isEqualTo(UPDATED_MIN_PRICE);

        // Validate the Vgoitem in Elasticsearch
        verify(mockVgoitemSearchRepository, times(1)).save(testVgoitem);
    }

    @Test
    @Transactional
    public void updateNonExistingVgoitem() throws Exception {
        int databaseSizeBeforeUpdate = vgoitemRepository.findAll().size();

        // Create the Vgoitem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVgoitemMockMvc.perform(put("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vgoitem)))
            .andExpect(status().isBadRequest());

        // Validate the Vgoitem in the database
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vgoitem in Elasticsearch
        verify(mockVgoitemSearchRepository, times(0)).save(vgoitem);
    }

    @Test
    @Transactional
    public void deleteVgoitem() throws Exception {
        // Initialize the database
        vgoitemService.save(vgoitem);

        int databaseSizeBeforeDelete = vgoitemRepository.findAll().size();

        // Get the vgoitem
        restVgoitemMockMvc.perform(delete("/api/vgoitems/{id}", vgoitem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vgoitem in Elasticsearch
        verify(mockVgoitemSearchRepository, times(1)).deleteById(vgoitem.getId());
    }

    @Test
    @Transactional
    public void searchVgoitem() throws Exception {
        // Initialize the database
        vgoitemService.save(vgoitem);
        when(mockVgoitemSearchRepository.search(queryStringQuery("id:" + vgoitem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vgoitem), PageRequest.of(0, 1), 1));
        // Search the vgoitem
        restVgoitemMockMvc.perform(get("/api/_search/vgoitems?query=id:" + vgoitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vgoitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].op7day").value(hasItem(DEFAULT_OP_7_DAY)))
            .andExpect(jsonPath("$.[*].op30day").value(hasItem(DEFAULT_OP_30_DAY)))
            .andExpect(jsonPath("$.[*].sales").value(hasItem(DEFAULT_SALES)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].minPrice").value(hasItem(DEFAULT_MIN_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vgoitem.class);
        Vgoitem vgoitem1 = new Vgoitem();
        vgoitem1.setId(1L);
        Vgoitem vgoitem2 = new Vgoitem();
        vgoitem2.setId(vgoitem1.getId());
        assertThat(vgoitem1).isEqualTo(vgoitem2);
        vgoitem2.setId(2L);
        assertThat(vgoitem1).isNotEqualTo(vgoitem2);
        vgoitem1.setId(null);
        assertThat(vgoitem1).isNotEqualTo(vgoitem2);
    }
}
