package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.SupplierProducts;
import com.pos.core.repository.SupplierProductsRepository;
import com.pos.core.service.SupplierProductsService;
import com.pos.core.repository.search.SupplierProductsSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.pos.core.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SupplierProductsResource REST controller.
 *
 * @see SupplierProductsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class SupplierProductsResourceIntTest {

    private static final Long DEFAULT_BUYING_PRICE = 1L;
    private static final Long UPDATED_BUYING_PRICE = 2L;

    private static final Long DEFAULT_STANDARD_BUYING_PRICE = 1L;
    private static final Long UPDATED_STANDARD_BUYING_PRICE = 2L;

    private static final Long DEFAULT_RETAIL_PRICE = 1L;
    private static final Long UPDATED_RETAIL_PRICE = 2L;

    private static final Integer DEFAULT_DELIVERY_LEAD_TIME = 1;
    private static final Integer UPDATED_DELIVERY_LEAD_TIME = 2;

    private static final LocalDate DEFAULT_FIRST_ITEM_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_ITEM_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_MINIMUM_REORDER_LEVEL = 1;
    private static final Integer UPDATED_MINIMUM_REORDER_LEVEL = 2;

    private static final Integer DEFAULT_MAX_REORDER_LEVEL = 1;
    private static final Integer UPDATED_MAX_REORDER_LEVEL = 2;

    @Autowired
    private SupplierProductsRepository supplierProductsRepository;

    @Autowired
    private SupplierProductsService supplierProductsService;

    @Autowired
    private SupplierProductsSearchRepository supplierProductsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSupplierProductsMockMvc;

    private SupplierProducts supplierProducts;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SupplierProductsResource supplierProductsResource = new SupplierProductsResource(supplierProductsService);
        this.restSupplierProductsMockMvc = MockMvcBuilders.standaloneSetup(supplierProductsResource)
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
    public static SupplierProducts createEntity(EntityManager em) {
        SupplierProducts supplierProducts = new SupplierProducts()
            .buyingPrice(DEFAULT_BUYING_PRICE)
            .standardBuyingPrice(DEFAULT_STANDARD_BUYING_PRICE)
            .retailPrice(DEFAULT_RETAIL_PRICE)
            .deliveryLeadTime(DEFAULT_DELIVERY_LEAD_TIME)
            .firstItemDeliveryDate(DEFAULT_FIRST_ITEM_DELIVERY_DATE)
            .minimumReorderLevel(DEFAULT_MINIMUM_REORDER_LEVEL)
            .maxReorderLevel(DEFAULT_MAX_REORDER_LEVEL);
        return supplierProducts;
    }

    @Before
    public void initTest() {
        supplierProductsSearchRepository.deleteAll();
        supplierProducts = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplierProducts() throws Exception {
        int databaseSizeBeforeCreate = supplierProductsRepository.findAll().size();

        // Create the SupplierProducts
        restSupplierProductsMockMvc.perform(post("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierProducts)))
            .andExpect(status().isCreated());

        // Validate the SupplierProducts in the database
        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierProducts testSupplierProducts = supplierProductsList.get(supplierProductsList.size() - 1);
        assertThat(testSupplierProducts.getBuyingPrice()).isEqualTo(DEFAULT_BUYING_PRICE);
        assertThat(testSupplierProducts.getStandardBuyingPrice()).isEqualTo(DEFAULT_STANDARD_BUYING_PRICE);
        assertThat(testSupplierProducts.getRetailPrice()).isEqualTo(DEFAULT_RETAIL_PRICE);
        assertThat(testSupplierProducts.getDeliveryLeadTime()).isEqualTo(DEFAULT_DELIVERY_LEAD_TIME);
        assertThat(testSupplierProducts.getFirstItemDeliveryDate()).isEqualTo(DEFAULT_FIRST_ITEM_DELIVERY_DATE);
        assertThat(testSupplierProducts.getMinimumReorderLevel()).isEqualTo(DEFAULT_MINIMUM_REORDER_LEVEL);
        assertThat(testSupplierProducts.getMaxReorderLevel()).isEqualTo(DEFAULT_MAX_REORDER_LEVEL);

        // Validate the SupplierProducts in Elasticsearch
        SupplierProducts supplierProductsEs = supplierProductsSearchRepository.findOne(testSupplierProducts.getId());
        assertThat(supplierProductsEs).isEqualToComparingFieldByField(testSupplierProducts);
    }

    @Test
    @Transactional
    public void createSupplierProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supplierProductsRepository.findAll().size();

        // Create the SupplierProducts with an existing ID
        supplierProducts.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierProductsMockMvc.perform(post("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierProducts)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierProducts in the database
        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBuyingPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierProductsRepository.findAll().size();
        // set the field null
        supplierProducts.setBuyingPrice(null);

        // Create the SupplierProducts, which fails.

        restSupplierProductsMockMvc.perform(post("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierProducts)))
            .andExpect(status().isBadRequest());

        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRetailPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierProductsRepository.findAll().size();
        // set the field null
        supplierProducts.setRetailPrice(null);

        // Create the SupplierProducts, which fails.

        restSupplierProductsMockMvc.perform(post("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierProducts)))
            .andExpect(status().isBadRequest());

        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupplierProducts() throws Exception {
        // Initialize the database
        supplierProductsRepository.saveAndFlush(supplierProducts);

        // Get all the supplierProductsList
        restSupplierProductsMockMvc.perform(get("/api/supplier-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].buyingPrice").value(hasItem(DEFAULT_BUYING_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].standardBuyingPrice").value(hasItem(DEFAULT_STANDARD_BUYING_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].retailPrice").value(hasItem(DEFAULT_RETAIL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].deliveryLeadTime").value(hasItem(DEFAULT_DELIVERY_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].firstItemDeliveryDate").value(hasItem(DEFAULT_FIRST_ITEM_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].minimumReorderLevel").value(hasItem(DEFAULT_MINIMUM_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].maxReorderLevel").value(hasItem(DEFAULT_MAX_REORDER_LEVEL)));
    }

    @Test
    @Transactional
    public void getSupplierProducts() throws Exception {
        // Initialize the database
        supplierProductsRepository.saveAndFlush(supplierProducts);

        // Get the supplierProducts
        restSupplierProductsMockMvc.perform(get("/api/supplier-products/{id}", supplierProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supplierProducts.getId().intValue()))
            .andExpect(jsonPath("$.buyingPrice").value(DEFAULT_BUYING_PRICE.intValue()))
            .andExpect(jsonPath("$.standardBuyingPrice").value(DEFAULT_STANDARD_BUYING_PRICE.intValue()))
            .andExpect(jsonPath("$.retailPrice").value(DEFAULT_RETAIL_PRICE.intValue()))
            .andExpect(jsonPath("$.deliveryLeadTime").value(DEFAULT_DELIVERY_LEAD_TIME))
            .andExpect(jsonPath("$.firstItemDeliveryDate").value(DEFAULT_FIRST_ITEM_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.minimumReorderLevel").value(DEFAULT_MINIMUM_REORDER_LEVEL))
            .andExpect(jsonPath("$.maxReorderLevel").value(DEFAULT_MAX_REORDER_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingSupplierProducts() throws Exception {
        // Get the supplierProducts
        restSupplierProductsMockMvc.perform(get("/api/supplier-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierProducts() throws Exception {
        // Initialize the database
        supplierProductsService.save(supplierProducts);

        int databaseSizeBeforeUpdate = supplierProductsRepository.findAll().size();

        // Update the supplierProducts
        SupplierProducts updatedSupplierProducts = supplierProductsRepository.findOne(supplierProducts.getId());
        // Disconnect from session so that the updates on updatedSupplierProducts are not directly saved in db
        em.detach(updatedSupplierProducts);
        updatedSupplierProducts
            .buyingPrice(UPDATED_BUYING_PRICE)
            .standardBuyingPrice(UPDATED_STANDARD_BUYING_PRICE)
            .retailPrice(UPDATED_RETAIL_PRICE)
            .deliveryLeadTime(UPDATED_DELIVERY_LEAD_TIME)
            .firstItemDeliveryDate(UPDATED_FIRST_ITEM_DELIVERY_DATE)
            .minimumReorderLevel(UPDATED_MINIMUM_REORDER_LEVEL)
            .maxReorderLevel(UPDATED_MAX_REORDER_LEVEL);

        restSupplierProductsMockMvc.perform(put("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSupplierProducts)))
            .andExpect(status().isOk());

        // Validate the SupplierProducts in the database
        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeUpdate);
        SupplierProducts testSupplierProducts = supplierProductsList.get(supplierProductsList.size() - 1);
        assertThat(testSupplierProducts.getBuyingPrice()).isEqualTo(UPDATED_BUYING_PRICE);
        assertThat(testSupplierProducts.getStandardBuyingPrice()).isEqualTo(UPDATED_STANDARD_BUYING_PRICE);
        assertThat(testSupplierProducts.getRetailPrice()).isEqualTo(UPDATED_RETAIL_PRICE);
        assertThat(testSupplierProducts.getDeliveryLeadTime()).isEqualTo(UPDATED_DELIVERY_LEAD_TIME);
        assertThat(testSupplierProducts.getFirstItemDeliveryDate()).isEqualTo(UPDATED_FIRST_ITEM_DELIVERY_DATE);
        assertThat(testSupplierProducts.getMinimumReorderLevel()).isEqualTo(UPDATED_MINIMUM_REORDER_LEVEL);
        assertThat(testSupplierProducts.getMaxReorderLevel()).isEqualTo(UPDATED_MAX_REORDER_LEVEL);

        // Validate the SupplierProducts in Elasticsearch
        SupplierProducts supplierProductsEs = supplierProductsSearchRepository.findOne(testSupplierProducts.getId());
        assertThat(supplierProductsEs).isEqualToComparingFieldByField(testSupplierProducts);
    }

    @Test
    @Transactional
    public void updateNonExistingSupplierProducts() throws Exception {
        int databaseSizeBeforeUpdate = supplierProductsRepository.findAll().size();

        // Create the SupplierProducts

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSupplierProductsMockMvc.perform(put("/api/supplier-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierProducts)))
            .andExpect(status().isCreated());

        // Validate the SupplierProducts in the database
        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSupplierProducts() throws Exception {
        // Initialize the database
        supplierProductsService.save(supplierProducts);

        int databaseSizeBeforeDelete = supplierProductsRepository.findAll().size();

        // Get the supplierProducts
        restSupplierProductsMockMvc.perform(delete("/api/supplier-products/{id}", supplierProducts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean supplierProductsExistsInEs = supplierProductsSearchRepository.exists(supplierProducts.getId());
        assertThat(supplierProductsExistsInEs).isFalse();

        // Validate the database is empty
        List<SupplierProducts> supplierProductsList = supplierProductsRepository.findAll();
        assertThat(supplierProductsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSupplierProducts() throws Exception {
        // Initialize the database
        supplierProductsService.save(supplierProducts);

        // Search the supplierProducts
        restSupplierProductsMockMvc.perform(get("/api/_search/supplier-products?query=id:" + supplierProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].buyingPrice").value(hasItem(DEFAULT_BUYING_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].standardBuyingPrice").value(hasItem(DEFAULT_STANDARD_BUYING_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].retailPrice").value(hasItem(DEFAULT_RETAIL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].deliveryLeadTime").value(hasItem(DEFAULT_DELIVERY_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].firstItemDeliveryDate").value(hasItem(DEFAULT_FIRST_ITEM_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].minimumReorderLevel").value(hasItem(DEFAULT_MINIMUM_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].maxReorderLevel").value(hasItem(DEFAULT_MAX_REORDER_LEVEL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierProducts.class);
        SupplierProducts supplierProducts1 = new SupplierProducts();
        supplierProducts1.setId(1L);
        SupplierProducts supplierProducts2 = new SupplierProducts();
        supplierProducts2.setId(supplierProducts1.getId());
        assertThat(supplierProducts1).isEqualTo(supplierProducts2);
        supplierProducts2.setId(2L);
        assertThat(supplierProducts1).isNotEqualTo(supplierProducts2);
        supplierProducts1.setId(null);
        assertThat(supplierProducts1).isNotEqualTo(supplierProducts2);
    }
}
