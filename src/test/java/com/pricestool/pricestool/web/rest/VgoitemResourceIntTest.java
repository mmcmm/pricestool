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

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_RARITY = "AAAAAAAAAA";
    private static final String UPDATED_RARITY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_300_PX = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_300_PX = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_600_PX = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_600_PX = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUGGESTED_PRICE = 1;
    private static final Integer UPDATED_SUGGESTED_PRICE = 2;

    private static final Integer DEFAULT_SUGGESTED_PRICE_7_DAY = 1;
    private static final Integer UPDATED_SUGGESTED_PRICE_7_DAY = 2;

    private static final Integer DEFAULT_SUGGESTED_PRICE_30_DAY = 1;
    private static final Integer UPDATED_SUGGESTED_PRICE_30_DAY = 2;

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
            .setMessageConverters(jacksonMessageConverter).build();
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
            .category(DEFAULT_CATEGORY)
            .rarity(DEFAULT_RARITY)
            .type(DEFAULT_TYPE)
            .color(DEFAULT_COLOR)
            .image300px(DEFAULT_IMAGE_300_PX)
            .image600px(DEFAULT_IMAGE_600_PX)
            .suggestedPrice(DEFAULT_SUGGESTED_PRICE)
            .suggestedPrice7day(DEFAULT_SUGGESTED_PRICE_7_DAY)
            .suggestedPrice30day(DEFAULT_SUGGESTED_PRICE_30_DAY);
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
        assertThat(testVgoitem.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testVgoitem.getRarity()).isEqualTo(DEFAULT_RARITY);
        assertThat(testVgoitem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVgoitem.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testVgoitem.getImage300px()).isEqualTo(DEFAULT_IMAGE_300_PX);
        assertThat(testVgoitem.getImage600px()).isEqualTo(DEFAULT_IMAGE_600_PX);
        assertThat(testVgoitem.getSuggestedPrice()).isEqualTo(DEFAULT_SUGGESTED_PRICE);
        assertThat(testVgoitem.getSuggestedPrice7day()).isEqualTo(DEFAULT_SUGGESTED_PRICE_7_DAY);
        assertThat(testVgoitem.getSuggestedPrice30day()).isEqualTo(DEFAULT_SUGGESTED_PRICE_30_DAY);

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
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = vgoitemRepository.findAll().size();
        // set the field null
        vgoitem.setCategory(null);

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
    public void checkRarityIsRequired() throws Exception {
        int databaseSizeBeforeTest = vgoitemRepository.findAll().size();
        // set the field null
        vgoitem.setRarity(null);

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
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vgoitemRepository.findAll().size();
        // set the field null
        vgoitem.setType(null);

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
    public void checkSuggestedPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = vgoitemRepository.findAll().size();
        // set the field null
        vgoitem.setSuggestedPrice(null);

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
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].image300px").value(hasItem(DEFAULT_IMAGE_300_PX.toString())))
            .andExpect(jsonPath("$.[*].image600px").value(hasItem(DEFAULT_IMAGE_600_PX.toString())))
            .andExpect(jsonPath("$.[*].suggestedPrice").value(hasItem(DEFAULT_SUGGESTED_PRICE)))
            .andExpect(jsonPath("$.[*].suggestedPrice7day").value(hasItem(DEFAULT_SUGGESTED_PRICE_7_DAY)))
            .andExpect(jsonPath("$.[*].suggestedPrice30day").value(hasItem(DEFAULT_SUGGESTED_PRICE_30_DAY)));
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
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.rarity").value(DEFAULT_RARITY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.image300px").value(DEFAULT_IMAGE_300_PX.toString()))
            .andExpect(jsonPath("$.image600px").value(DEFAULT_IMAGE_600_PX.toString()))
            .andExpect(jsonPath("$.suggestedPrice").value(DEFAULT_SUGGESTED_PRICE))
            .andExpect(jsonPath("$.suggestedPrice7day").value(DEFAULT_SUGGESTED_PRICE_7_DAY))
            .andExpect(jsonPath("$.suggestedPrice30day").value(DEFAULT_SUGGESTED_PRICE_30_DAY));
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
    public void getAllVgoitemsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where category equals to DEFAULT_CATEGORY
        defaultVgoitemShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the vgoitemList where category equals to UPDATED_CATEGORY
        defaultVgoitemShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultVgoitemShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the vgoitemList where category equals to UPDATED_CATEGORY
        defaultVgoitemShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where category is not null
        defaultVgoitemShouldBeFound("category.specified=true");

        // Get all the vgoitemList where category is null
        defaultVgoitemShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByRarityIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where rarity equals to DEFAULT_RARITY
        defaultVgoitemShouldBeFound("rarity.equals=" + DEFAULT_RARITY);

        // Get all the vgoitemList where rarity equals to UPDATED_RARITY
        defaultVgoitemShouldNotBeFound("rarity.equals=" + UPDATED_RARITY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByRarityIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where rarity in DEFAULT_RARITY or UPDATED_RARITY
        defaultVgoitemShouldBeFound("rarity.in=" + DEFAULT_RARITY + "," + UPDATED_RARITY);

        // Get all the vgoitemList where rarity equals to UPDATED_RARITY
        defaultVgoitemShouldNotBeFound("rarity.in=" + UPDATED_RARITY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByRarityIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where rarity is not null
        defaultVgoitemShouldBeFound("rarity.specified=true");

        // Get all the vgoitemList where rarity is null
        defaultVgoitemShouldNotBeFound("rarity.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where type equals to DEFAULT_TYPE
        defaultVgoitemShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the vgoitemList where type equals to UPDATED_TYPE
        defaultVgoitemShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultVgoitemShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the vgoitemList where type equals to UPDATED_TYPE
        defaultVgoitemShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where type is not null
        defaultVgoitemShouldBeFound("type.specified=true");

        // Get all the vgoitemList where type is null
        defaultVgoitemShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where color equals to DEFAULT_COLOR
        defaultVgoitemShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the vgoitemList where color equals to UPDATED_COLOR
        defaultVgoitemShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultVgoitemShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the vgoitemList where color equals to UPDATED_COLOR
        defaultVgoitemShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where color is not null
        defaultVgoitemShouldBeFound("color.specified=true");

        // Get all the vgoitemList where color is null
        defaultVgoitemShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage300pxIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image300px equals to DEFAULT_IMAGE_300_PX
        defaultVgoitemShouldBeFound("image300px.equals=" + DEFAULT_IMAGE_300_PX);

        // Get all the vgoitemList where image300px equals to UPDATED_IMAGE_300_PX
        defaultVgoitemShouldNotBeFound("image300px.equals=" + UPDATED_IMAGE_300_PX);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage300pxIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image300px in DEFAULT_IMAGE_300_PX or UPDATED_IMAGE_300_PX
        defaultVgoitemShouldBeFound("image300px.in=" + DEFAULT_IMAGE_300_PX + "," + UPDATED_IMAGE_300_PX);

        // Get all the vgoitemList where image300px equals to UPDATED_IMAGE_300_PX
        defaultVgoitemShouldNotBeFound("image300px.in=" + UPDATED_IMAGE_300_PX);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage300pxIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image300px is not null
        defaultVgoitemShouldBeFound("image300px.specified=true");

        // Get all the vgoitemList where image300px is null
        defaultVgoitemShouldNotBeFound("image300px.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage600pxIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image600px equals to DEFAULT_IMAGE_600_PX
        defaultVgoitemShouldBeFound("image600px.equals=" + DEFAULT_IMAGE_600_PX);

        // Get all the vgoitemList where image600px equals to UPDATED_IMAGE_600_PX
        defaultVgoitemShouldNotBeFound("image600px.equals=" + UPDATED_IMAGE_600_PX);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage600pxIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image600px in DEFAULT_IMAGE_600_PX or UPDATED_IMAGE_600_PX
        defaultVgoitemShouldBeFound("image600px.in=" + DEFAULT_IMAGE_600_PX + "," + UPDATED_IMAGE_600_PX);

        // Get all the vgoitemList where image600px equals to UPDATED_IMAGE_600_PX
        defaultVgoitemShouldNotBeFound("image600px.in=" + UPDATED_IMAGE_600_PX);
    }

    @Test
    @Transactional
    public void getAllVgoitemsByImage600pxIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where image600px is not null
        defaultVgoitemShouldBeFound("image600px.specified=true");

        // Get all the vgoitemList where image600px is null
        defaultVgoitemShouldNotBeFound("image600px.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice equals to DEFAULT_SUGGESTED_PRICE
        defaultVgoitemShouldBeFound("suggestedPrice.equals=" + DEFAULT_SUGGESTED_PRICE);

        // Get all the vgoitemList where suggestedPrice equals to UPDATED_SUGGESTED_PRICE
        defaultVgoitemShouldNotBeFound("suggestedPrice.equals=" + UPDATED_SUGGESTED_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPriceIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice in DEFAULT_SUGGESTED_PRICE or UPDATED_SUGGESTED_PRICE
        defaultVgoitemShouldBeFound("suggestedPrice.in=" + DEFAULT_SUGGESTED_PRICE + "," + UPDATED_SUGGESTED_PRICE);

        // Get all the vgoitemList where suggestedPrice equals to UPDATED_SUGGESTED_PRICE
        defaultVgoitemShouldNotBeFound("suggestedPrice.in=" + UPDATED_SUGGESTED_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice is not null
        defaultVgoitemShouldBeFound("suggestedPrice.specified=true");

        // Get all the vgoitemList where suggestedPrice is null
        defaultVgoitemShouldNotBeFound("suggestedPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice greater than or equals to DEFAULT_SUGGESTED_PRICE
        defaultVgoitemShouldBeFound("suggestedPrice.greaterOrEqualThan=" + DEFAULT_SUGGESTED_PRICE);

        // Get all the vgoitemList where suggestedPrice greater than or equals to UPDATED_SUGGESTED_PRICE
        defaultVgoitemShouldNotBeFound("suggestedPrice.greaterOrEqualThan=" + UPDATED_SUGGESTED_PRICE);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice less than or equals to DEFAULT_SUGGESTED_PRICE
        defaultVgoitemShouldNotBeFound("suggestedPrice.lessThan=" + DEFAULT_SUGGESTED_PRICE);

        // Get all the vgoitemList where suggestedPrice less than or equals to UPDATED_SUGGESTED_PRICE
        defaultVgoitemShouldBeFound("suggestedPrice.lessThan=" + UPDATED_SUGGESTED_PRICE);
    }


    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice7dayIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice7day equals to DEFAULT_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldBeFound("suggestedPrice7day.equals=" + DEFAULT_SUGGESTED_PRICE_7_DAY);

        // Get all the vgoitemList where suggestedPrice7day equals to UPDATED_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice7day.equals=" + UPDATED_SUGGESTED_PRICE_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice7dayIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice7day in DEFAULT_SUGGESTED_PRICE_7_DAY or UPDATED_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldBeFound("suggestedPrice7day.in=" + DEFAULT_SUGGESTED_PRICE_7_DAY + "," + UPDATED_SUGGESTED_PRICE_7_DAY);

        // Get all the vgoitemList where suggestedPrice7day equals to UPDATED_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice7day.in=" + UPDATED_SUGGESTED_PRICE_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice7dayIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice7day is not null
        defaultVgoitemShouldBeFound("suggestedPrice7day.specified=true");

        // Get all the vgoitemList where suggestedPrice7day is null
        defaultVgoitemShouldNotBeFound("suggestedPrice7day.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice7dayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice7day greater than or equals to DEFAULT_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldBeFound("suggestedPrice7day.greaterOrEqualThan=" + DEFAULT_SUGGESTED_PRICE_7_DAY);

        // Get all the vgoitemList where suggestedPrice7day greater than or equals to UPDATED_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice7day.greaterOrEqualThan=" + UPDATED_SUGGESTED_PRICE_7_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice7dayIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice7day less than or equals to DEFAULT_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice7day.lessThan=" + DEFAULT_SUGGESTED_PRICE_7_DAY);

        // Get all the vgoitemList where suggestedPrice7day less than or equals to UPDATED_SUGGESTED_PRICE_7_DAY
        defaultVgoitemShouldBeFound("suggestedPrice7day.lessThan=" + UPDATED_SUGGESTED_PRICE_7_DAY);
    }


    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice30dayIsEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice30day equals to DEFAULT_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldBeFound("suggestedPrice30day.equals=" + DEFAULT_SUGGESTED_PRICE_30_DAY);

        // Get all the vgoitemList where suggestedPrice30day equals to UPDATED_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice30day.equals=" + UPDATED_SUGGESTED_PRICE_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice30dayIsInShouldWork() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice30day in DEFAULT_SUGGESTED_PRICE_30_DAY or UPDATED_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldBeFound("suggestedPrice30day.in=" + DEFAULT_SUGGESTED_PRICE_30_DAY + "," + UPDATED_SUGGESTED_PRICE_30_DAY);

        // Get all the vgoitemList where suggestedPrice30day equals to UPDATED_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice30day.in=" + UPDATED_SUGGESTED_PRICE_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice30dayIsNullOrNotNull() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice30day is not null
        defaultVgoitemShouldBeFound("suggestedPrice30day.specified=true");

        // Get all the vgoitemList where suggestedPrice30day is null
        defaultVgoitemShouldNotBeFound("suggestedPrice30day.specified=false");
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice30dayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice30day greater than or equals to DEFAULT_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldBeFound("suggestedPrice30day.greaterOrEqualThan=" + DEFAULT_SUGGESTED_PRICE_30_DAY);

        // Get all the vgoitemList where suggestedPrice30day greater than or equals to UPDATED_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice30day.greaterOrEqualThan=" + UPDATED_SUGGESTED_PRICE_30_DAY);
    }

    @Test
    @Transactional
    public void getAllVgoitemsBySuggestedPrice30dayIsLessThanSomething() throws Exception {
        // Initialize the database
        vgoitemRepository.saveAndFlush(vgoitem);

        // Get all the vgoitemList where suggestedPrice30day less than or equals to DEFAULT_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldNotBeFound("suggestedPrice30day.lessThan=" + DEFAULT_SUGGESTED_PRICE_30_DAY);

        // Get all the vgoitemList where suggestedPrice30day less than or equals to UPDATED_SUGGESTED_PRICE_30_DAY
        defaultVgoitemShouldBeFound("suggestedPrice30day.lessThan=" + UPDATED_SUGGESTED_PRICE_30_DAY);
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
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].image300px").value(hasItem(DEFAULT_IMAGE_300_PX.toString())))
            .andExpect(jsonPath("$.[*].image600px").value(hasItem(DEFAULT_IMAGE_600_PX.toString())))
            .andExpect(jsonPath("$.[*].suggestedPrice").value(hasItem(DEFAULT_SUGGESTED_PRICE)))
            .andExpect(jsonPath("$.[*].suggestedPrice7day").value(hasItem(DEFAULT_SUGGESTED_PRICE_7_DAY)))
            .andExpect(jsonPath("$.[*].suggestedPrice30day").value(hasItem(DEFAULT_SUGGESTED_PRICE_30_DAY)));

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
            .category(UPDATED_CATEGORY)
            .rarity(UPDATED_RARITY)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .image300px(UPDATED_IMAGE_300_PX)
            .image600px(UPDATED_IMAGE_600_PX)
            .suggestedPrice(UPDATED_SUGGESTED_PRICE)
            .suggestedPrice7day(UPDATED_SUGGESTED_PRICE_7_DAY)
            .suggestedPrice30day(UPDATED_SUGGESTED_PRICE_30_DAY);

        restVgoitemMockMvc.perform(put("/api/vgoitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVgoitem)))
            .andExpect(status().isOk());

        // Validate the Vgoitem in the database
        List<Vgoitem> vgoitemList = vgoitemRepository.findAll();
        assertThat(vgoitemList).hasSize(databaseSizeBeforeUpdate);
        Vgoitem testVgoitem = vgoitemList.get(vgoitemList.size() - 1);
        assertThat(testVgoitem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVgoitem.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testVgoitem.getRarity()).isEqualTo(UPDATED_RARITY);
        assertThat(testVgoitem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVgoitem.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testVgoitem.getImage300px()).isEqualTo(UPDATED_IMAGE_300_PX);
        assertThat(testVgoitem.getImage600px()).isEqualTo(UPDATED_IMAGE_600_PX);
        assertThat(testVgoitem.getSuggestedPrice()).isEqualTo(UPDATED_SUGGESTED_PRICE);
        assertThat(testVgoitem.getSuggestedPrice7day()).isEqualTo(UPDATED_SUGGESTED_PRICE_7_DAY);
        assertThat(testVgoitem.getSuggestedPrice30day()).isEqualTo(UPDATED_SUGGESTED_PRICE_30_DAY);

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
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].image300px").value(hasItem(DEFAULT_IMAGE_300_PX)))
            .andExpect(jsonPath("$.[*].image600px").value(hasItem(DEFAULT_IMAGE_600_PX)))
            .andExpect(jsonPath("$.[*].suggestedPrice").value(hasItem(DEFAULT_SUGGESTED_PRICE)))
            .andExpect(jsonPath("$.[*].suggestedPrice7day").value(hasItem(DEFAULT_SUGGESTED_PRICE_7_DAY)))
            .andExpect(jsonPath("$.[*].suggestedPrice30day").value(hasItem(DEFAULT_SUGGESTED_PRICE_30_DAY)));
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
