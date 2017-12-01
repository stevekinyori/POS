package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Products;
import com.pos.core.repository.ProductsRepository;
import com.pos.core.service.ProductsService;
import com.pos.core.repository.search.ProductsSearchRepository;
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
import java.util.List;

import static com.pos.core.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductsResource REST controller.
 *
 * @see ProductsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class ProductsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_REORDER_LEVEL = 1;
    private static final Integer UPDATED_REORDER_LEVEL = 2;

    private static final Integer DEFAULT_REORDER_QUANTITY = 1;
    private static final Integer UPDATED_REORDER_QUANTITY = 2;

    private static final Integer DEFAULT_AVERAGE_MONTHLY_USAGE = 1;
    private static final Integer UPDATED_AVERAGE_MONTHLY_USAGE = 2;

    private static final Integer DEFAULT_QUANTITY_SUPPLIED_TO_DATE = 1;
    private static final Integer UPDATED_QUANTITY_SUPPLIED_TO_DATE = 2;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsSearchRepository productsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductsMockMvc;

    private Products products;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductsResource productsResource = new ProductsResource(productsService);
        this.restProductsMockMvc = MockMvcBuilders.standaloneSetup(productsResource)
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
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .shtDescription(DEFAULT_SHT_DESCRIPTION)
            .description(DEFAULT_DESCRIPTION)
            .reorderLevel(DEFAULT_REORDER_LEVEL)
            .reorderQuantity(DEFAULT_REORDER_QUANTITY)
            .averageMonthlyUsage(DEFAULT_AVERAGE_MONTHLY_USAGE)
            .quantitySuppliedToDate(DEFAULT_QUANTITY_SUPPLIED_TO_DATE);
        return products;
    }

    @Before
    public void initTest() {
        productsSearchRepository.deleteAll();
        products = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProducts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducts.getShtDescription()).isEqualTo(DEFAULT_SHT_DESCRIPTION);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getReorderLevel()).isEqualTo(DEFAULT_REORDER_LEVEL);
        assertThat(testProducts.getReorderQuantity()).isEqualTo(DEFAULT_REORDER_QUANTITY);
        assertThat(testProducts.getAverageMonthlyUsage()).isEqualTo(DEFAULT_AVERAGE_MONTHLY_USAGE);
        assertThat(testProducts.getQuantitySuppliedToDate()).isEqualTo(DEFAULT_QUANTITY_SUPPLIED_TO_DATE);

        // Validate the Products in Elasticsearch
        Products productsEs = productsSearchRepository.findOne(testProducts.getId());
        assertThat(productsEs).isEqualToComparingFieldByField(testProducts);
    }

    @Test
    @Transactional
    public void createProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products with an existing ID
        products.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setCode(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setName(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShtDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setShtDescription(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDescription").value(hasItem(DEFAULT_SHT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].reorderQuantity").value(hasItem(DEFAULT_REORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].averageMonthlyUsage").value(hasItem(DEFAULT_AVERAGE_MONTHLY_USAGE)))
            .andExpect(jsonPath("$.[*].quantitySuppliedToDate").value(hasItem(DEFAULT_QUANTITY_SUPPLIED_TO_DATE)));
    }

    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shtDescription").value(DEFAULT_SHT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.reorderLevel").value(DEFAULT_REORDER_LEVEL))
            .andExpect(jsonPath("$.reorderQuantity").value(DEFAULT_REORDER_QUANTITY))
            .andExpect(jsonPath("$.averageMonthlyUsage").value(DEFAULT_AVERAGE_MONTHLY_USAGE))
            .andExpect(jsonPath("$.quantitySuppliedToDate").value(DEFAULT_QUANTITY_SUPPLIED_TO_DATE));
    }

    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findOne(products.getId());
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .shtDescription(UPDATED_SHT_DESCRIPTION)
            .description(UPDATED_DESCRIPTION)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .reorderQuantity(UPDATED_REORDER_QUANTITY)
            .averageMonthlyUsage(UPDATED_AVERAGE_MONTHLY_USAGE)
            .quantitySuppliedToDate(UPDATED_QUANTITY_SUPPLIED_TO_DATE);

        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProducts)))
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProducts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducts.getShtDescription()).isEqualTo(UPDATED_SHT_DESCRIPTION);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getReorderLevel()).isEqualTo(UPDATED_REORDER_LEVEL);
        assertThat(testProducts.getReorderQuantity()).isEqualTo(UPDATED_REORDER_QUANTITY);
        assertThat(testProducts.getAverageMonthlyUsage()).isEqualTo(UPDATED_AVERAGE_MONTHLY_USAGE);
        assertThat(testProducts.getQuantitySuppliedToDate()).isEqualTo(UPDATED_QUANTITY_SUPPLIED_TO_DATE);

        // Validate the Products in Elasticsearch
        Products productsEs = productsSearchRepository.findOne(testProducts.getId());
        assertThat(productsEs).isEqualToComparingFieldByField(testProducts);
    }

    @Test
    @Transactional
    public void updateNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Create the Products

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Get the products
        restProductsMockMvc.perform(delete("/api/products/{id}", products.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean productsExistsInEs = productsSearchRepository.exists(products.getId());
        assertThat(productsExistsInEs).isFalse();

        // Validate the database is empty
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        // Search the products
        restProductsMockMvc.perform(get("/api/_search/products?query=id:" + products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDescription").value(hasItem(DEFAULT_SHT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].reorderQuantity").value(hasItem(DEFAULT_REORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].averageMonthlyUsage").value(hasItem(DEFAULT_AVERAGE_MONTHLY_USAGE)))
            .andExpect(jsonPath("$.[*].quantitySuppliedToDate").value(hasItem(DEFAULT_QUANTITY_SUPPLIED_TO_DATE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Products.class);
        Products products1 = new Products();
        products1.setId(1L);
        Products products2 = new Products();
        products2.setId(products1.getId());
        assertThat(products1).isEqualTo(products2);
        products2.setId(2L);
        assertThat(products1).isNotEqualTo(products2);
        products1.setId(null);
        assertThat(products1).isNotEqualTo(products2);
    }
}
