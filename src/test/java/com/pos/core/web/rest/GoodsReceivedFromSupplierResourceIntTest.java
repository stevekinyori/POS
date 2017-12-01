package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.GoodsReceivedFromSupplier;
import com.pos.core.repository.GoodsReceivedFromSupplierRepository;
import com.pos.core.service.GoodsReceivedFromSupplierService;
import com.pos.core.repository.search.GoodsReceivedFromSupplierSearchRepository;
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
 * Test class for the GoodsReceivedFromSupplierResource REST controller.
 *
 * @see GoodsReceivedFromSupplierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class GoodsReceivedFromSupplierResourceIntTest {

    private static final String DEFAULT_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_UNIT_PRICE = 1;
    private static final Integer UPDATED_UNIT_PRICE = 2;

    @Autowired
    private GoodsReceivedFromSupplierRepository goodsReceivedFromSupplierRepository;

    @Autowired
    private GoodsReceivedFromSupplierService goodsReceivedFromSupplierService;

    @Autowired
    private GoodsReceivedFromSupplierSearchRepository goodsReceivedFromSupplierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGoodsReceivedFromSupplierMockMvc;

    private GoodsReceivedFromSupplier goodsReceivedFromSupplier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsReceivedFromSupplierResource goodsReceivedFromSupplierResource = new GoodsReceivedFromSupplierResource(goodsReceivedFromSupplierService);
        this.restGoodsReceivedFromSupplierMockMvc = MockMvcBuilders.standaloneSetup(goodsReceivedFromSupplierResource)
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
    public static GoodsReceivedFromSupplier createEntity(EntityManager em) {
        GoodsReceivedFromSupplier goodsReceivedFromSupplier = new GoodsReceivedFromSupplier()
            .batchNo(DEFAULT_BATCH_NO)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE);
        return goodsReceivedFromSupplier;
    }

    @Before
    public void initTest() {
        goodsReceivedFromSupplierSearchRepository.deleteAll();
        goodsReceivedFromSupplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodsReceivedFromSupplier() throws Exception {
        int databaseSizeBeforeCreate = goodsReceivedFromSupplierRepository.findAll().size();

        // Create the GoodsReceivedFromSupplier
        restGoodsReceivedFromSupplierMockMvc.perform(post("/api/goods-received-from-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceivedFromSupplier)))
            .andExpect(status().isCreated());

        // Validate the GoodsReceivedFromSupplier in the database
        List<GoodsReceivedFromSupplier> goodsReceivedFromSupplierList = goodsReceivedFromSupplierRepository.findAll();
        assertThat(goodsReceivedFromSupplierList).hasSize(databaseSizeBeforeCreate + 1);
        GoodsReceivedFromSupplier testGoodsReceivedFromSupplier = goodsReceivedFromSupplierList.get(goodsReceivedFromSupplierList.size() - 1);
        assertThat(testGoodsReceivedFromSupplier.getBatchNo()).isEqualTo(DEFAULT_BATCH_NO);
        assertThat(testGoodsReceivedFromSupplier.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testGoodsReceivedFromSupplier.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);

        // Validate the GoodsReceivedFromSupplier in Elasticsearch
        GoodsReceivedFromSupplier goodsReceivedFromSupplierEs = goodsReceivedFromSupplierSearchRepository.findOne(testGoodsReceivedFromSupplier.getId());
        assertThat(goodsReceivedFromSupplierEs).isEqualToComparingFieldByField(testGoodsReceivedFromSupplier);
    }

    @Test
    @Transactional
    public void createGoodsReceivedFromSupplierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsReceivedFromSupplierRepository.findAll().size();

        // Create the GoodsReceivedFromSupplier with an existing ID
        goodsReceivedFromSupplier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsReceivedFromSupplierMockMvc.perform(post("/api/goods-received-from-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceivedFromSupplier)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceivedFromSupplier in the database
        List<GoodsReceivedFromSupplier> goodsReceivedFromSupplierList = goodsReceivedFromSupplierRepository.findAll();
        assertThat(goodsReceivedFromSupplierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGoodsReceivedFromSuppliers() throws Exception {
        // Initialize the database
        goodsReceivedFromSupplierRepository.saveAndFlush(goodsReceivedFromSupplier);

        // Get all the goodsReceivedFromSupplierList
        restGoodsReceivedFromSupplierMockMvc.perform(get("/api/goods-received-from-suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceivedFromSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)));
    }

    @Test
    @Transactional
    public void getGoodsReceivedFromSupplier() throws Exception {
        // Initialize the database
        goodsReceivedFromSupplierRepository.saveAndFlush(goodsReceivedFromSupplier);

        // Get the goodsReceivedFromSupplier
        restGoodsReceivedFromSupplierMockMvc.perform(get("/api/goods-received-from-suppliers/{id}", goodsReceivedFromSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodsReceivedFromSupplier.getId().intValue()))
            .andExpect(jsonPath("$.batchNo").value(DEFAULT_BATCH_NO.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingGoodsReceivedFromSupplier() throws Exception {
        // Get the goodsReceivedFromSupplier
        restGoodsReceivedFromSupplierMockMvc.perform(get("/api/goods-received-from-suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReceivedFromSupplier() throws Exception {
        // Initialize the database
        goodsReceivedFromSupplierService.save(goodsReceivedFromSupplier);

        int databaseSizeBeforeUpdate = goodsReceivedFromSupplierRepository.findAll().size();

        // Update the goodsReceivedFromSupplier
        GoodsReceivedFromSupplier updatedGoodsReceivedFromSupplier = goodsReceivedFromSupplierRepository.findOne(goodsReceivedFromSupplier.getId());
        // Disconnect from session so that the updates on updatedGoodsReceivedFromSupplier are not directly saved in db
        em.detach(updatedGoodsReceivedFromSupplier);
        updatedGoodsReceivedFromSupplier
            .batchNo(UPDATED_BATCH_NO)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restGoodsReceivedFromSupplierMockMvc.perform(put("/api/goods-received-from-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodsReceivedFromSupplier)))
            .andExpect(status().isOk());

        // Validate the GoodsReceivedFromSupplier in the database
        List<GoodsReceivedFromSupplier> goodsReceivedFromSupplierList = goodsReceivedFromSupplierRepository.findAll();
        assertThat(goodsReceivedFromSupplierList).hasSize(databaseSizeBeforeUpdate);
        GoodsReceivedFromSupplier testGoodsReceivedFromSupplier = goodsReceivedFromSupplierList.get(goodsReceivedFromSupplierList.size() - 1);
        assertThat(testGoodsReceivedFromSupplier.getBatchNo()).isEqualTo(UPDATED_BATCH_NO);
        assertThat(testGoodsReceivedFromSupplier.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testGoodsReceivedFromSupplier.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);

        // Validate the GoodsReceivedFromSupplier in Elasticsearch
        GoodsReceivedFromSupplier goodsReceivedFromSupplierEs = goodsReceivedFromSupplierSearchRepository.findOne(testGoodsReceivedFromSupplier.getId());
        assertThat(goodsReceivedFromSupplierEs).isEqualToComparingFieldByField(testGoodsReceivedFromSupplier);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodsReceivedFromSupplier() throws Exception {
        int databaseSizeBeforeUpdate = goodsReceivedFromSupplierRepository.findAll().size();

        // Create the GoodsReceivedFromSupplier

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGoodsReceivedFromSupplierMockMvc.perform(put("/api/goods-received-from-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceivedFromSupplier)))
            .andExpect(status().isCreated());

        // Validate the GoodsReceivedFromSupplier in the database
        List<GoodsReceivedFromSupplier> goodsReceivedFromSupplierList = goodsReceivedFromSupplierRepository.findAll();
        assertThat(goodsReceivedFromSupplierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGoodsReceivedFromSupplier() throws Exception {
        // Initialize the database
        goodsReceivedFromSupplierService.save(goodsReceivedFromSupplier);

        int databaseSizeBeforeDelete = goodsReceivedFromSupplierRepository.findAll().size();

        // Get the goodsReceivedFromSupplier
        restGoodsReceivedFromSupplierMockMvc.perform(delete("/api/goods-received-from-suppliers/{id}", goodsReceivedFromSupplier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean goodsReceivedFromSupplierExistsInEs = goodsReceivedFromSupplierSearchRepository.exists(goodsReceivedFromSupplier.getId());
        assertThat(goodsReceivedFromSupplierExistsInEs).isFalse();

        // Validate the database is empty
        List<GoodsReceivedFromSupplier> goodsReceivedFromSupplierList = goodsReceivedFromSupplierRepository.findAll();
        assertThat(goodsReceivedFromSupplierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGoodsReceivedFromSupplier() throws Exception {
        // Initialize the database
        goodsReceivedFromSupplierService.save(goodsReceivedFromSupplier);

        // Search the goodsReceivedFromSupplier
        restGoodsReceivedFromSupplierMockMvc.perform(get("/api/_search/goods-received-from-suppliers?query=id:" + goodsReceivedFromSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceivedFromSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodsReceivedFromSupplier.class);
        GoodsReceivedFromSupplier goodsReceivedFromSupplier1 = new GoodsReceivedFromSupplier();
        goodsReceivedFromSupplier1.setId(1L);
        GoodsReceivedFromSupplier goodsReceivedFromSupplier2 = new GoodsReceivedFromSupplier();
        goodsReceivedFromSupplier2.setId(goodsReceivedFromSupplier1.getId());
        assertThat(goodsReceivedFromSupplier1).isEqualTo(goodsReceivedFromSupplier2);
        goodsReceivedFromSupplier2.setId(2L);
        assertThat(goodsReceivedFromSupplier1).isNotEqualTo(goodsReceivedFromSupplier2);
        goodsReceivedFromSupplier1.setId(null);
        assertThat(goodsReceivedFromSupplier1).isNotEqualTo(goodsReceivedFromSupplier2);
    }
}
