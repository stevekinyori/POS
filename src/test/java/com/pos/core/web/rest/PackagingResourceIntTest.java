package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Packaging;
import com.pos.core.repository.PackagingRepository;
import com.pos.core.service.PackagingService;
import com.pos.core.repository.search.PackagingSearchRepository;
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

import com.pos.core.domain.enumeration.Measurement;
/**
 * Test class for the PackagingResource REST controller.
 *
 * @see PackagingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class PackagingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final Measurement DEFAULT_UNIT = Measurement.LITRES;
    private static final Measurement UPDATED_UNIT = Measurement.MILLILITRES;

    @Autowired
    private PackagingRepository packagingRepository;

    @Autowired
    private PackagingService packagingService;

    @Autowired
    private PackagingSearchRepository packagingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPackagingMockMvc;

    private Packaging packaging;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackagingResource packagingResource = new PackagingResource(packagingService);
        this.restPackagingMockMvc = MockMvcBuilders.standaloneSetup(packagingResource)
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
    public static Packaging createEntity(EntityManager em) {
        Packaging packaging = new Packaging()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .capacity(DEFAULT_CAPACITY)
            .unit(DEFAULT_UNIT);
        return packaging;
    }

    @Before
    public void initTest() {
        packagingSearchRepository.deleteAll();
        packaging = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackaging() throws Exception {
        int databaseSizeBeforeCreate = packagingRepository.findAll().size();

        // Create the Packaging
        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isCreated());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeCreate + 1);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPackaging.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testPackaging.getUnit()).isEqualTo(DEFAULT_UNIT);

        // Validate the Packaging in Elasticsearch
        Packaging packagingEs = packagingSearchRepository.findOne(testPackaging.getId());
        assertThat(packagingEs).isEqualToComparingFieldByField(testPackaging);
    }

    @Test
    @Transactional
    public void createPackagingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packagingRepository.findAll().size();

        // Create the Packaging with an existing ID
        packaging.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setName(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setQuantity(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setCapacity(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setUnit(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc.perform(post("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPackagings() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        // Get all the packagingList
        restPackagingMockMvc.perform(get("/api/packagings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packaging.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getPackaging() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        // Get the packaging
        restPackagingMockMvc.perform(get("/api/packagings/{id}", packaging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packaging.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPackaging() throws Exception {
        // Get the packaging
        restPackagingMockMvc.perform(get("/api/packagings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackaging() throws Exception {
        // Initialize the database
        packagingService.save(packaging);

        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();

        // Update the packaging
        Packaging updatedPackaging = packagingRepository.findOne(packaging.getId());
        // Disconnect from session so that the updates on updatedPackaging are not directly saved in db
        em.detach(updatedPackaging);
        updatedPackaging
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .capacity(UPDATED_CAPACITY)
            .unit(UPDATED_UNIT);

        restPackagingMockMvc.perform(put("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackaging)))
            .andExpect(status().isOk());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPackaging.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testPackaging.getUnit()).isEqualTo(UPDATED_UNIT);

        // Validate the Packaging in Elasticsearch
        Packaging packagingEs = packagingSearchRepository.findOne(testPackaging.getId());
        assertThat(packagingEs).isEqualToComparingFieldByField(testPackaging);
    }

    @Test
    @Transactional
    public void updateNonExistingPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();

        // Create the Packaging

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPackagingMockMvc.perform(put("/api/packagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packaging)))
            .andExpect(status().isCreated());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePackaging() throws Exception {
        // Initialize the database
        packagingService.save(packaging);

        int databaseSizeBeforeDelete = packagingRepository.findAll().size();

        // Get the packaging
        restPackagingMockMvc.perform(delete("/api/packagings/{id}", packaging.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean packagingExistsInEs = packagingSearchRepository.exists(packaging.getId());
        assertThat(packagingExistsInEs).isFalse();

        // Validate the database is empty
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackaging() throws Exception {
        // Initialize the database
        packagingService.save(packaging);

        // Search the packaging
        restPackagingMockMvc.perform(get("/api/_search/packagings?query=id:" + packaging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packaging.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packaging.class);
        Packaging packaging1 = new Packaging();
        packaging1.setId(1L);
        Packaging packaging2 = new Packaging();
        packaging2.setId(packaging1.getId());
        assertThat(packaging1).isEqualTo(packaging2);
        packaging2.setId(2L);
        assertThat(packaging1).isNotEqualTo(packaging2);
        packaging1.setId(null);
        assertThat(packaging1).isNotEqualTo(packaging2);
    }
}
