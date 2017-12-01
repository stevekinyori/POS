package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.GoodsReturnedToSupplier;
import com.pos.core.repository.GoodsReturnedToSupplierRepository;
import com.pos.core.service.GoodsReturnedToSupplierService;
import com.pos.core.repository.search.GoodsReturnedToSupplierSearchRepository;
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
 * Test class for the GoodsReturnedToSupplierResource REST controller.
 *
 * @see GoodsReturnedToSupplierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class GoodsReturnedToSupplierResourceIntTest {

    private static final String DEFAULT_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private GoodsReturnedToSupplierRepository goodsReturnedToSupplierRepository;

    @Autowired
    private GoodsReturnedToSupplierService goodsReturnedToSupplierService;

    @Autowired
    private GoodsReturnedToSupplierSearchRepository goodsReturnedToSupplierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGoodsReturnedToSupplierMockMvc;

    private GoodsReturnedToSupplier goodsReturnedToSupplier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsReturnedToSupplierResource goodsReturnedToSupplierResource = new GoodsReturnedToSupplierResource(goodsReturnedToSupplierService);
        this.restGoodsReturnedToSupplierMockMvc = MockMvcBuilders.standaloneSetup(goodsReturnedToSupplierResource)
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
    public static GoodsReturnedToSupplier createEntity(EntityManager em) {
        GoodsReturnedToSupplier goodsReturnedToSupplier = new GoodsReturnedToSupplier()
            .batchNo(DEFAULT_BATCH_NO)
            .quantity(DEFAULT_QUANTITY);
        return goodsReturnedToSupplier;
    }

    @Before
    public void initTest() {
        goodsReturnedToSupplierSearchRepository.deleteAll();
        goodsReturnedToSupplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodsReturnedToSupplier() throws Exception {
        int databaseSizeBeforeCreate = goodsReturnedToSupplierRepository.findAll().size();

        // Create the GoodsReturnedToSupplier
        restGoodsReturnedToSupplierMockMvc.perform(post("/api/goods-returned-to-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReturnedToSupplier)))
            .andExpect(status().isCreated());

        // Validate the GoodsReturnedToSupplier in the database
        List<GoodsReturnedToSupplier> goodsReturnedToSupplierList = goodsReturnedToSupplierRepository.findAll();
        assertThat(goodsReturnedToSupplierList).hasSize(databaseSizeBeforeCreate + 1);
        GoodsReturnedToSupplier testGoodsReturnedToSupplier = goodsReturnedToSupplierList.get(goodsReturnedToSupplierList.size() - 1);
        assertThat(testGoodsReturnedToSupplier.getBatchNo()).isEqualTo(DEFAULT_BATCH_NO);
        assertThat(testGoodsReturnedToSupplier.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the GoodsReturnedToSupplier in Elasticsearch
        GoodsReturnedToSupplier goodsReturnedToSupplierEs = goodsReturnedToSupplierSearchRepository.findOne(testGoodsReturnedToSupplier.getId());
        assertThat(goodsReturnedToSupplierEs).isEqualToComparingFieldByField(testGoodsReturnedToSupplier);
    }

    @Test
    @Transactional
    public void createGoodsReturnedToSupplierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsReturnedToSupplierRepository.findAll().size();

        // Create the GoodsReturnedToSupplier with an existing ID
        goodsReturnedToSupplier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsReturnedToSupplierMockMvc.perform(post("/api/goods-returned-to-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReturnedToSupplier)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReturnedToSupplier in the database
        List<GoodsReturnedToSupplier> goodsReturnedToSupplierList = goodsReturnedToSupplierRepository.findAll();
        assertThat(goodsReturnedToSupplierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGoodsReturnedToSuppliers() throws Exception {
        // Initialize the database
        goodsReturnedToSupplierRepository.saveAndFlush(goodsReturnedToSupplier);

        // Get all the goodsReturnedToSupplierList
        restGoodsReturnedToSupplierMockMvc.perform(get("/api/goods-returned-to-suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReturnedToSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getGoodsReturnedToSupplier() throws Exception {
        // Initialize the database
        goodsReturnedToSupplierRepository.saveAndFlush(goodsReturnedToSupplier);

        // Get the goodsReturnedToSupplier
        restGoodsReturnedToSupplierMockMvc.perform(get("/api/goods-returned-to-suppliers/{id}", goodsReturnedToSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodsReturnedToSupplier.getId().intValue()))
            .andExpect(jsonPath("$.batchNo").value(DEFAULT_BATCH_NO.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingGoodsReturnedToSupplier() throws Exception {
        // Get the goodsReturnedToSupplier
        restGoodsReturnedToSupplierMockMvc.perform(get("/api/goods-returned-to-suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReturnedToSupplier() throws Exception {
        // Initialize the database
        goodsReturnedToSupplierService.save(goodsReturnedToSupplier);

        int databaseSizeBeforeUpdate = goodsReturnedToSupplierRepository.findAll().size();

        // Update the goodsReturnedToSupplier
        GoodsReturnedToSupplier updatedGoodsReturnedToSupplier = goodsReturnedToSupplierRepository.findOne(goodsReturnedToSupplier.getId());
        // Disconnect from session so that the updates on updatedGoodsReturnedToSupplier are not directly saved in db
        em.detach(updatedGoodsReturnedToSupplier);
        updatedGoodsReturnedToSupplier
            .batchNo(UPDATED_BATCH_NO)
            .quantity(UPDATED_QUANTITY);

        restGoodsReturnedToSupplierMockMvc.perform(put("/api/goods-returned-to-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodsReturnedToSupplier)))
            .andExpect(status().isOk());

        // Validate the GoodsReturnedToSupplier in the database
        List<GoodsReturnedToSupplier> goodsReturnedToSupplierList = goodsReturnedToSupplierRepository.findAll();
        assertThat(goodsReturnedToSupplierList).hasSize(databaseSizeBeforeUpdate);
        GoodsReturnedToSupplier testGoodsReturnedToSupplier = goodsReturnedToSupplierList.get(goodsReturnedToSupplierList.size() - 1);
        assertThat(testGoodsReturnedToSupplier.getBatchNo()).isEqualTo(UPDATED_BATCH_NO);
        assertThat(testGoodsReturnedToSupplier.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the GoodsReturnedToSupplier in Elasticsearch
        GoodsReturnedToSupplier goodsReturnedToSupplierEs = goodsReturnedToSupplierSearchRepository.findOne(testGoodsReturnedToSupplier.getId());
        assertThat(goodsReturnedToSupplierEs).isEqualToComparingFieldByField(testGoodsReturnedToSupplier);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodsReturnedToSupplier() throws Exception {
        int databaseSizeBeforeUpdate = goodsReturnedToSupplierRepository.findAll().size();

        // Create the GoodsReturnedToSupplier

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGoodsReturnedToSupplierMockMvc.perform(put("/api/goods-returned-to-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReturnedToSupplier)))
            .andExpect(status().isCreated());

        // Validate the GoodsReturnedToSupplier in the database
        List<GoodsReturnedToSupplier> goodsReturnedToSupplierList = goodsReturnedToSupplierRepository.findAll();
        assertThat(goodsReturnedToSupplierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGoodsReturnedToSupplier() throws Exception {
        // Initialize the database
        goodsReturnedToSupplierService.save(goodsReturnedToSupplier);

        int databaseSizeBeforeDelete = goodsReturnedToSupplierRepository.findAll().size();

        // Get the goodsReturnedToSupplier
        restGoodsReturnedToSupplierMockMvc.perform(delete("/api/goods-returned-to-suppliers/{id}", goodsReturnedToSupplier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean goodsReturnedToSupplierExistsInEs = goodsReturnedToSupplierSearchRepository.exists(goodsReturnedToSupplier.getId());
        assertThat(goodsReturnedToSupplierExistsInEs).isFalse();

        // Validate the database is empty
        List<GoodsReturnedToSupplier> goodsReturnedToSupplierList = goodsReturnedToSupplierRepository.findAll();
        assertThat(goodsReturnedToSupplierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGoodsReturnedToSupplier() throws Exception {
        // Initialize the database
        goodsReturnedToSupplierService.save(goodsReturnedToSupplier);

        // Search the goodsReturnedToSupplier
        restGoodsReturnedToSupplierMockMvc.perform(get("/api/_search/goods-returned-to-suppliers?query=id:" + goodsReturnedToSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReturnedToSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodsReturnedToSupplier.class);
        GoodsReturnedToSupplier goodsReturnedToSupplier1 = new GoodsReturnedToSupplier();
        goodsReturnedToSupplier1.setId(1L);
        GoodsReturnedToSupplier goodsReturnedToSupplier2 = new GoodsReturnedToSupplier();
        goodsReturnedToSupplier2.setId(goodsReturnedToSupplier1.getId());
        assertThat(goodsReturnedToSupplier1).isEqualTo(goodsReturnedToSupplier2);
        goodsReturnedToSupplier2.setId(2L);
        assertThat(goodsReturnedToSupplier1).isNotEqualTo(goodsReturnedToSupplier2);
        goodsReturnedToSupplier1.setId(null);
        assertThat(goodsReturnedToSupplier1).isNotEqualTo(goodsReturnedToSupplier2);
    }
}
