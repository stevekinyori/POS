package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Sales;
import com.pos.core.repository.SalesRepository;
import com.pos.core.service.SalesService;
import com.pos.core.repository.search.SalesSearchRepository;
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
 * Test class for the SalesResource REST controller.
 *
 * @see SalesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class SalesResourceIntTest {

    private static final Instant DEFAULT_DATE_INITIATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INITIATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final Integer DEFAULT_TOTAL_SALE_AMOUNT = 1;
    private static final Integer UPDATED_TOTAL_SALE_AMOUNT = 2;

    private static final Integer DEFAULT_TOTAL_PAID_AMOUNT = 1;
    private static final Integer UPDATED_TOTAL_PAID_AMOUNT = 2;

    private static final Instant DEFAULT_DATE_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesSearchRepository salesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesMockMvc;

    private Sales sales;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesResource salesResource = new SalesResource(salesService);
        this.restSalesMockMvc = MockMvcBuilders.standaloneSetup(salesResource)
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
    public static Sales createEntity(EntityManager em) {
        Sales sales = new Sales()
            .dateInitiated(DEFAULT_DATE_INITIATED)
            .userId(DEFAULT_USER_ID)
            .totalSaleAmount(DEFAULT_TOTAL_SALE_AMOUNT)
            .totalPaidAmount(DEFAULT_TOTAL_PAID_AMOUNT)
            .dateClosed(DEFAULT_DATE_CLOSED);
        return sales;
    }

    @Before
    public void initTest() {
        salesSearchRepository.deleteAll();
        sales = createEntity(em);
    }

    @Test
    @Transactional
    public void createSales() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isCreated());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate + 1);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getDateInitiated()).isEqualTo(DEFAULT_DATE_INITIATED);
        assertThat(testSales.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSales.getTotalSaleAmount()).isEqualTo(DEFAULT_TOTAL_SALE_AMOUNT);
        assertThat(testSales.getTotalPaidAmount()).isEqualTo(DEFAULT_TOTAL_PAID_AMOUNT);
        assertThat(testSales.getDateClosed()).isEqualTo(DEFAULT_DATE_CLOSED);

        // Validate the Sales in Elasticsearch
        Sales salesEs = salesSearchRepository.findOne(testSales.getId());
        assertThat(salesEs).isEqualToComparingFieldByField(testSales);
    }

    @Test
    @Transactional
    public void createSalesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales with an existing ID
        sales.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateInitiatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesRepository.findAll().size();
        // set the field null
        sales.setDateInitiated(null);

        // Create the Sales, which fails.

        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList
        restSalesMockMvc.perform(get("/api/sales?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sales.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInitiated").value(hasItem(DEFAULT_DATE_INITIATED.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].totalSaleAmount").value(hasItem(DEFAULT_TOTAL_SALE_AMOUNT)))
            .andExpect(jsonPath("$.[*].totalPaidAmount").value(hasItem(DEFAULT_TOTAL_PAID_AMOUNT)))
            .andExpect(jsonPath("$.[*].dateClosed").value(hasItem(DEFAULT_DATE_CLOSED.toString())));
    }

    @Test
    @Transactional
    public void getSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", sales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sales.getId().intValue()))
            .andExpect(jsonPath("$.dateInitiated").value(DEFAULT_DATE_INITIATED.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.totalSaleAmount").value(DEFAULT_TOTAL_SALE_AMOUNT))
            .andExpect(jsonPath("$.totalPaidAmount").value(DEFAULT_TOTAL_PAID_AMOUNT))
            .andExpect(jsonPath("$.dateClosed").value(DEFAULT_DATE_CLOSED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSales() throws Exception {
        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSales() throws Exception {
        // Initialize the database
        salesService.save(sales);

        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Update the sales
        Sales updatedSales = salesRepository.findOne(sales.getId());
        // Disconnect from session so that the updates on updatedSales are not directly saved in db
        em.detach(updatedSales);
        updatedSales
            .dateInitiated(UPDATED_DATE_INITIATED)
            .userId(UPDATED_USER_ID)
            .totalSaleAmount(UPDATED_TOTAL_SALE_AMOUNT)
            .totalPaidAmount(UPDATED_TOTAL_PAID_AMOUNT)
            .dateClosed(UPDATED_DATE_CLOSED);

        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSales)))
            .andExpect(status().isOk());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getDateInitiated()).isEqualTo(UPDATED_DATE_INITIATED);
        assertThat(testSales.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSales.getTotalSaleAmount()).isEqualTo(UPDATED_TOTAL_SALE_AMOUNT);
        assertThat(testSales.getTotalPaidAmount()).isEqualTo(UPDATED_TOTAL_PAID_AMOUNT);
        assertThat(testSales.getDateClosed()).isEqualTo(UPDATED_DATE_CLOSED);

        // Validate the Sales in Elasticsearch
        Sales salesEs = salesSearchRepository.findOne(testSales.getId());
        assertThat(salesEs).isEqualToComparingFieldByField(testSales);
    }

    @Test
    @Transactional
    public void updateNonExistingSales() throws Exception {
        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Create the Sales

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isCreated());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSales() throws Exception {
        // Initialize the database
        salesService.save(sales);

        int databaseSizeBeforeDelete = salesRepository.findAll().size();

        // Get the sales
        restSalesMockMvc.perform(delete("/api/sales/{id}", sales.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean salesExistsInEs = salesSearchRepository.exists(sales.getId());
        assertThat(salesExistsInEs).isFalse();

        // Validate the database is empty
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSales() throws Exception {
        // Initialize the database
        salesService.save(sales);

        // Search the sales
        restSalesMockMvc.perform(get("/api/_search/sales?query=id:" + sales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sales.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInitiated").value(hasItem(DEFAULT_DATE_INITIATED.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].totalSaleAmount").value(hasItem(DEFAULT_TOTAL_SALE_AMOUNT)))
            .andExpect(jsonPath("$.[*].totalPaidAmount").value(hasItem(DEFAULT_TOTAL_PAID_AMOUNT)))
            .andExpect(jsonPath("$.[*].dateClosed").value(hasItem(DEFAULT_DATE_CLOSED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sales.class);
        Sales sales1 = new Sales();
        sales1.setId(1L);
        Sales sales2 = new Sales();
        sales2.setId(sales1.getId());
        assertThat(sales1).isEqualTo(sales2);
        sales2.setId(2L);
        assertThat(sales1).isNotEqualTo(sales2);
        sales1.setId(null);
        assertThat(sales1).isNotEqualTo(sales2);
    }
}
