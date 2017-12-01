package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.SaleTransactions;
import com.pos.core.repository.SaleTransactionsRepository;
import com.pos.core.service.SaleTransactionsService;
import com.pos.core.repository.search.SaleTransactionsSearchRepository;
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
 * Test class for the SaleTransactionsResource REST controller.
 *
 * @see SaleTransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class SaleTransactionsResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_UNIT_PRICE = 0;
    private static final Integer UPDATED_UNIT_PRICE = 1;

    @Autowired
    private SaleTransactionsRepository saleTransactionsRepository;

    @Autowired
    private SaleTransactionsService saleTransactionsService;

    @Autowired
    private SaleTransactionsSearchRepository saleTransactionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaleTransactionsMockMvc;

    private SaleTransactions saleTransactions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleTransactionsResource saleTransactionsResource = new SaleTransactionsResource(saleTransactionsService);
        this.restSaleTransactionsMockMvc = MockMvcBuilders.standaloneSetup(saleTransactionsResource)
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
    public static SaleTransactions createEntity(EntityManager em) {
        SaleTransactions saleTransactions = new SaleTransactions()
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE);
        return saleTransactions;
    }

    @Before
    public void initTest() {
        saleTransactionsSearchRepository.deleteAll();
        saleTransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleTransactions() throws Exception {
        int databaseSizeBeforeCreate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions
        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactions)))
            .andExpect(status().isCreated());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        SaleTransactions testSaleTransactions = saleTransactionsList.get(saleTransactionsList.size() - 1);
        assertThat(testSaleTransactions.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSaleTransactions.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);

        // Validate the SaleTransactions in Elasticsearch
        SaleTransactions saleTransactionsEs = saleTransactionsSearchRepository.findOne(testSaleTransactions.getId());
        assertThat(saleTransactionsEs).isEqualToComparingFieldByField(testSaleTransactions);
    }

    @Test
    @Transactional
    public void createSaleTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions with an existing ID
        saleTransactions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactions)))
            .andExpect(status().isBadRequest());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)));
    }

    @Test
    @Transactional
    public void getSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/{id}", saleTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleTransactions.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingSaleTransactions() throws Exception {
        // Get the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsService.save(saleTransactions);

        int databaseSizeBeforeUpdate = saleTransactionsRepository.findAll().size();

        // Update the saleTransactions
        SaleTransactions updatedSaleTransactions = saleTransactionsRepository.findOne(saleTransactions.getId());
        // Disconnect from session so that the updates on updatedSaleTransactions are not directly saved in db
        em.detach(updatedSaleTransactions);
        updatedSaleTransactions
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restSaleTransactionsMockMvc.perform(put("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaleTransactions)))
            .andExpect(status().isOk());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeUpdate);
        SaleTransactions testSaleTransactions = saleTransactionsList.get(saleTransactionsList.size() - 1);
        assertThat(testSaleTransactions.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSaleTransactions.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);

        // Validate the SaleTransactions in Elasticsearch
        SaleTransactions saleTransactionsEs = saleTransactionsSearchRepository.findOne(testSaleTransactions.getId());
        assertThat(saleTransactionsEs).isEqualToComparingFieldByField(testSaleTransactions);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleTransactions() throws Exception {
        int databaseSizeBeforeUpdate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaleTransactionsMockMvc.perform(put("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactions)))
            .andExpect(status().isCreated());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsService.save(saleTransactions);

        int databaseSizeBeforeDelete = saleTransactionsRepository.findAll().size();

        // Get the saleTransactions
        restSaleTransactionsMockMvc.perform(delete("/api/sale-transactions/{id}", saleTransactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean saleTransactionsExistsInEs = saleTransactionsSearchRepository.exists(saleTransactions.getId());
        assertThat(saleTransactionsExistsInEs).isFalse();

        // Validate the database is empty
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsService.save(saleTransactions);

        // Search the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/_search/sale-transactions?query=id:" + saleTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleTransactions.class);
        SaleTransactions saleTransactions1 = new SaleTransactions();
        saleTransactions1.setId(1L);
        SaleTransactions saleTransactions2 = new SaleTransactions();
        saleTransactions2.setId(saleTransactions1.getId());
        assertThat(saleTransactions1).isEqualTo(saleTransactions2);
        saleTransactions2.setId(2L);
        assertThat(saleTransactions1).isNotEqualTo(saleTransactions2);
        saleTransactions1.setId(null);
        assertThat(saleTransactions1).isNotEqualTo(saleTransactions2);
    }
}
