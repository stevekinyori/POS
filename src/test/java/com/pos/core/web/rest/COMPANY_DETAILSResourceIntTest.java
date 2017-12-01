package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.COMPANY_DETAILS;
import com.pos.core.repository.COMPANY_DETAILSRepository;
import com.pos.core.service.COMPANY_DETAILSService;
import com.pos.core.repository.search.COMPANY_DETAILSSearchRepository;
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
 * Test class for the COMPANY_DETAILSResource REST controller.
 *
 * @see COMPANY_DETAILSResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class COMPANY_DETAILSResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OPENED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OPENED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LICENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE_NO = "BBBBBBBBBB";

    @Autowired
    private COMPANY_DETAILSRepository cOMPANY_DETAILSRepository;

    @Autowired
    private COMPANY_DETAILSService cOMPANY_DETAILSService;

    @Autowired
    private COMPANY_DETAILSSearchRepository cOMPANY_DETAILSSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCOMPANY_DETAILSMockMvc;

    private COMPANY_DETAILS cOMPANY_DETAILS;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final COMPANY_DETAILSResource cOMPANY_DETAILSResource = new COMPANY_DETAILSResource(cOMPANY_DETAILSService);
        this.restCOMPANY_DETAILSMockMvc = MockMvcBuilders.standaloneSetup(cOMPANY_DETAILSResource)
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
    public static COMPANY_DETAILS createEntity(EntityManager em) {
        COMPANY_DETAILS cOMPANY_DETAILS = new COMPANY_DETAILS()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .dateOpened(DEFAULT_DATE_OPENED)
            .licenceNo(DEFAULT_LICENCE_NO);
        return cOMPANY_DETAILS;
    }

    @Before
    public void initTest() {
        cOMPANY_DETAILSSearchRepository.deleteAll();
        cOMPANY_DETAILS = createEntity(em);
    }

    @Test
    @Transactional
    public void createCOMPANY_DETAILS() throws Exception {
        int databaseSizeBeforeCreate = cOMPANY_DETAILSRepository.findAll().size();

        // Create the COMPANY_DETAILS
        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isCreated());

        // Validate the COMPANY_DETAILS in the database
        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeCreate + 1);
        COMPANY_DETAILS testCOMPANY_DETAILS = cOMPANY_DETAILSList.get(cOMPANY_DETAILSList.size() - 1);
        assertThat(testCOMPANY_DETAILS.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCOMPANY_DETAILS.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCOMPANY_DETAILS.getDateOpened()).isEqualTo(DEFAULT_DATE_OPENED);
        assertThat(testCOMPANY_DETAILS.getLicenceNo()).isEqualTo(DEFAULT_LICENCE_NO);

        // Validate the COMPANY_DETAILS in Elasticsearch
        COMPANY_DETAILS cOMPANY_DETAILSEs = cOMPANY_DETAILSSearchRepository.findOne(testCOMPANY_DETAILS.getId());
        assertThat(cOMPANY_DETAILSEs).isEqualToComparingFieldByField(testCOMPANY_DETAILS);
    }

    @Test
    @Transactional
    public void createCOMPANY_DETAILSWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cOMPANY_DETAILSRepository.findAll().size();

        // Create the COMPANY_DETAILS with an existing ID
        cOMPANY_DETAILS.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isBadRequest());

        // Validate the COMPANY_DETAILS in the database
        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cOMPANY_DETAILSRepository.findAll().size();
        // set the field null
        cOMPANY_DETAILS.setName(null);

        // Create the COMPANY_DETAILS, which fails.

        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isBadRequest());

        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = cOMPANY_DETAILSRepository.findAll().size();
        // set the field null
        cOMPANY_DETAILS.setLocation(null);

        // Create the COMPANY_DETAILS, which fails.

        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isBadRequest());

        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOpenedIsRequired() throws Exception {
        int databaseSizeBeforeTest = cOMPANY_DETAILSRepository.findAll().size();
        // set the field null
        cOMPANY_DETAILS.setDateOpened(null);

        // Create the COMPANY_DETAILS, which fails.

        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isBadRequest());

        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cOMPANY_DETAILSRepository.findAll().size();
        // set the field null
        cOMPANY_DETAILS.setLicenceNo(null);

        // Create the COMPANY_DETAILS, which fails.

        restCOMPANY_DETAILSMockMvc.perform(post("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isBadRequest());

        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCOMPANY_DETAILS() throws Exception {
        // Initialize the database
        cOMPANY_DETAILSRepository.saveAndFlush(cOMPANY_DETAILS);

        // Get all the cOMPANY_DETAILSList
        restCOMPANY_DETAILSMockMvc.perform(get("/api/company-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cOMPANY_DETAILS.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].dateOpened").value(hasItem(DEFAULT_DATE_OPENED.toString())))
            .andExpect(jsonPath("$.[*].licenceNo").value(hasItem(DEFAULT_LICENCE_NO.toString())));
    }

    @Test
    @Transactional
    public void getCOMPANY_DETAILS() throws Exception {
        // Initialize the database
        cOMPANY_DETAILSRepository.saveAndFlush(cOMPANY_DETAILS);

        // Get the cOMPANY_DETAILS
        restCOMPANY_DETAILSMockMvc.perform(get("/api/company-details/{id}", cOMPANY_DETAILS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cOMPANY_DETAILS.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.dateOpened").value(DEFAULT_DATE_OPENED.toString()))
            .andExpect(jsonPath("$.licenceNo").value(DEFAULT_LICENCE_NO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCOMPANY_DETAILS() throws Exception {
        // Get the cOMPANY_DETAILS
        restCOMPANY_DETAILSMockMvc.perform(get("/api/company-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCOMPANY_DETAILS() throws Exception {
        // Initialize the database
        cOMPANY_DETAILSService.save(cOMPANY_DETAILS);

        int databaseSizeBeforeUpdate = cOMPANY_DETAILSRepository.findAll().size();

        // Update the cOMPANY_DETAILS
        COMPANY_DETAILS updatedCOMPANY_DETAILS = cOMPANY_DETAILSRepository.findOne(cOMPANY_DETAILS.getId());
        // Disconnect from session so that the updates on updatedCOMPANY_DETAILS are not directly saved in db
        em.detach(updatedCOMPANY_DETAILS);
        updatedCOMPANY_DETAILS
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .dateOpened(UPDATED_DATE_OPENED)
            .licenceNo(UPDATED_LICENCE_NO);

        restCOMPANY_DETAILSMockMvc.perform(put("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCOMPANY_DETAILS)))
            .andExpect(status().isOk());

        // Validate the COMPANY_DETAILS in the database
        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeUpdate);
        COMPANY_DETAILS testCOMPANY_DETAILS = cOMPANY_DETAILSList.get(cOMPANY_DETAILSList.size() - 1);
        assertThat(testCOMPANY_DETAILS.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCOMPANY_DETAILS.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCOMPANY_DETAILS.getDateOpened()).isEqualTo(UPDATED_DATE_OPENED);
        assertThat(testCOMPANY_DETAILS.getLicenceNo()).isEqualTo(UPDATED_LICENCE_NO);

        // Validate the COMPANY_DETAILS in Elasticsearch
        COMPANY_DETAILS cOMPANY_DETAILSEs = cOMPANY_DETAILSSearchRepository.findOne(testCOMPANY_DETAILS.getId());
        assertThat(cOMPANY_DETAILSEs).isEqualToComparingFieldByField(testCOMPANY_DETAILS);
    }

    @Test
    @Transactional
    public void updateNonExistingCOMPANY_DETAILS() throws Exception {
        int databaseSizeBeforeUpdate = cOMPANY_DETAILSRepository.findAll().size();

        // Create the COMPANY_DETAILS

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCOMPANY_DETAILSMockMvc.perform(put("/api/company-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cOMPANY_DETAILS)))
            .andExpect(status().isCreated());

        // Validate the COMPANY_DETAILS in the database
        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCOMPANY_DETAILS() throws Exception {
        // Initialize the database
        cOMPANY_DETAILSService.save(cOMPANY_DETAILS);

        int databaseSizeBeforeDelete = cOMPANY_DETAILSRepository.findAll().size();

        // Get the cOMPANY_DETAILS
        restCOMPANY_DETAILSMockMvc.perform(delete("/api/company-details/{id}", cOMPANY_DETAILS.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cOMPANY_DETAILSExistsInEs = cOMPANY_DETAILSSearchRepository.exists(cOMPANY_DETAILS.getId());
        assertThat(cOMPANY_DETAILSExistsInEs).isFalse();

        // Validate the database is empty
        List<COMPANY_DETAILS> cOMPANY_DETAILSList = cOMPANY_DETAILSRepository.findAll();
        assertThat(cOMPANY_DETAILSList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCOMPANY_DETAILS() throws Exception {
        // Initialize the database
        cOMPANY_DETAILSService.save(cOMPANY_DETAILS);

        // Search the cOMPANY_DETAILS
        restCOMPANY_DETAILSMockMvc.perform(get("/api/_search/company-details?query=id:" + cOMPANY_DETAILS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cOMPANY_DETAILS.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].dateOpened").value(hasItem(DEFAULT_DATE_OPENED.toString())))
            .andExpect(jsonPath("$.[*].licenceNo").value(hasItem(DEFAULT_LICENCE_NO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(COMPANY_DETAILS.class);
        COMPANY_DETAILS cOMPANY_DETAILS1 = new COMPANY_DETAILS();
        cOMPANY_DETAILS1.setId(1L);
        COMPANY_DETAILS cOMPANY_DETAILS2 = new COMPANY_DETAILS();
        cOMPANY_DETAILS2.setId(cOMPANY_DETAILS1.getId());
        assertThat(cOMPANY_DETAILS1).isEqualTo(cOMPANY_DETAILS2);
        cOMPANY_DETAILS2.setId(2L);
        assertThat(cOMPANY_DETAILS1).isNotEqualTo(cOMPANY_DETAILS2);
        cOMPANY_DETAILS1.setId(null);
        assertThat(cOMPANY_DETAILS1).isNotEqualTo(cOMPANY_DETAILS2);
    }
}
