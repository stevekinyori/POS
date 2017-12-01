package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.ProductCategories;
import com.pos.core.repository.ProductCategoriesRepository;
import com.pos.core.service.ProductCategoriesService;
import com.pos.core.repository.search.ProductCategoriesSearchRepository;
import com.pos.core.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.pos.core.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductCategoriesResource REST controller.
 *
 * @see ProductCategoriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class ProductCategoriesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    @Autowired
    private ProductCategoriesService productCategoriesService;

    @Autowired
    private ProductCategoriesSearchRepository productCategoriesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductCategoriesMockMvc;

    private ProductCategories productCategories;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductCategoriesResource productCategoriesResource = new ProductCategoriesResource(productCategoriesService);
        this.restProductCategoriesMockMvc = MockMvcBuilders.standaloneSetup(productCategoriesResource)
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
    public static ProductCategories createEntity(EntityManager em) {
        ProductCategories productCategories = new ProductCategories()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shtDescription(DEFAULT_SHT_DESCRIPTION)
            .description(DEFAULT_DESCRIPTION)
            .dateCreated(DEFAULT_DATE_CREATED);
        return productCategories;
    }

    @Before
    public void initTest() {
        productCategoriesSearchRepository.deleteAll();
        productCategories = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductCategories() throws Exception {
        int databaseSizeBeforeCreate = productCategoriesRepository.findAll().size();

        // Create the ProductCategories
        restProductCategoriesMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isCreated());

        // Validate the ProductCategories in the database
        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategories testProductCategories = productCategoriesList.get(productCategoriesList.size() - 1);
        assertThat(testProductCategories.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProductCategories.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductCategories.getShtDescription()).isEqualTo(DEFAULT_SHT_DESCRIPTION);
        assertThat(testProductCategories.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductCategories.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);

        // Validate the ProductCategories in Elasticsearch
        ProductCategories productCategoriesEs = productCategoriesSearchRepository.findOne(testProductCategories.getId());
        assertThat(productCategoriesEs).isEqualToComparingFieldByField(testProductCategories);
    }

    @Test
    @Transactional
    public void createProductCategoriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productCategoriesRepository.findAll().size();

        // Create the ProductCategories with an existing ID
        productCategories.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoriesMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategories in the database
        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoriesRepository.findAll().size();
        // set the field null
        productCategories.setCode(null);

        // Create the ProductCategories, which fails.

        restProductCategoriesMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isBadRequest());

        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoriesRepository.findAll().size();
        // set the field null
        productCategories.setName(null);

        // Create the ProductCategories, which fails.

        restProductCategoriesMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isBadRequest());

        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShtDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoriesRepository.findAll().size();
        // set the field null
        productCategories.setShtDescription(null);

        // Create the ProductCategories, which fails.

        restProductCategoriesMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isBadRequest());

        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductCategories() throws Exception {
        // Initialize the database
        productCategoriesRepository.saveAndFlush(productCategories);

        // Get all the productCategoriesList
        restProductCategoriesMockMvc.perform(get("/api/product-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDescription").value(hasItem(DEFAULT_SHT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getProductCategories() throws Exception {
        // Initialize the database
        productCategoriesRepository.saveAndFlush(productCategories);

        // Get the productCategories
        restProductCategoriesMockMvc.perform(get("/api/product-categories/{id}", productCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productCategories.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shtDescription").value(DEFAULT_SHT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductCategories() throws Exception {
        // Get the productCategories
        restProductCategoriesMockMvc.perform(get("/api/product-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductCategories() throws Exception {
        // Initialize the database
        productCategoriesService.save(productCategories);

        int databaseSizeBeforeUpdate = productCategoriesRepository.findAll().size();

        // Update the productCategories
        ProductCategories updatedProductCategories = productCategoriesRepository.findOne(productCategories.getId());
        // Disconnect from session so that the updates on updatedProductCategories are not directly saved in db
        em.detach(updatedProductCategories);
        updatedProductCategories
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shtDescription(UPDATED_SHT_DESCRIPTION)
            .description(UPDATED_DESCRIPTION)
            .dateCreated(UPDATED_DATE_CREATED);

        restProductCategoriesMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductCategories)))
            .andExpect(status().isOk());

        // Validate the ProductCategories in the database
        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeUpdate);
        ProductCategories testProductCategories = productCategoriesList.get(productCategoriesList.size() - 1);
        assertThat(testProductCategories.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductCategories.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductCategories.getShtDescription()).isEqualTo(UPDATED_SHT_DESCRIPTION);
        assertThat(testProductCategories.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductCategories.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);

        // Validate the ProductCategories in Elasticsearch
        ProductCategories productCategoriesEs = productCategoriesSearchRepository.findOne(testProductCategories.getId());
        assertThat(productCategoriesEs).isEqualToComparingFieldByField(testProductCategories);
    }

    @Test
    @Transactional
    public void updateNonExistingProductCategories() throws Exception {
        int databaseSizeBeforeUpdate = productCategoriesRepository.findAll().size();

        // Create the ProductCategories

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductCategoriesMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategories)))
            .andExpect(status().isCreated());

        // Validate the ProductCategories in the database
        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProductCategories() throws Exception {
        // Initialize the database
        productCategoriesService.save(productCategories);

        int databaseSizeBeforeDelete = productCategoriesRepository.findAll().size();

        // Get the productCategories
        restProductCategoriesMockMvc.perform(delete("/api/product-categories/{id}", productCategories.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean productCategoriesExistsInEs = productCategoriesSearchRepository.exists(productCategories.getId());
        assertThat(productCategoriesExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductCategories> productCategoriesList = productCategoriesRepository.findAll();
        assertThat(productCategoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductCategories() throws Exception {
        // Initialize the database
        productCategoriesService.save(productCategories);

        // Search the productCategories
        restProductCategoriesMockMvc.perform(get("/api/_search/product-categories?query=id:" + productCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDescription").value(hasItem(DEFAULT_SHT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategories.class);
        ProductCategories productCategories1 = new ProductCategories();
        productCategories1.setId(1L);
        ProductCategories productCategories2 = new ProductCategories();
        productCategories2.setId(productCategories1.getId());
        assertThat(productCategories1).isEqualTo(productCategories2);
        productCategories2.setId(2L);
        assertThat(productCategories1).isNotEqualTo(productCategories2);
        productCategories1.setId(null);
        assertThat(productCategories1).isNotEqualTo(productCategories2);
    }
}
