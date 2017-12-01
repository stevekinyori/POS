package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Brands;
import com.pos.core.repository.BrandsRepository;
import com.pos.core.service.BrandsService;
import com.pos.core.repository.search.BrandsSearchRepository;
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
 * Test class for the BrandsResource REST controller.
 *
 * @see BrandsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class BrandsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_SHT_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BrandsRepository brandsRepository;

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private BrandsSearchRepository brandsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrandsMockMvc;

    private Brands brands;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrandsResource brandsResource = new BrandsResource(brandsService);
        this.restBrandsMockMvc = MockMvcBuilders.standaloneSetup(brandsResource)
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
    public static Brands createEntity(EntityManager em) {
        Brands brands = new Brands()
            .name(DEFAULT_NAME)
            .shtDesc(DEFAULT_SHT_DESC)
            .description(DEFAULT_DESCRIPTION);
        return brands;
    }

    @Before
    public void initTest() {
        brandsSearchRepository.deleteAll();
        brands = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrands() throws Exception {
        int databaseSizeBeforeCreate = brandsRepository.findAll().size();

        // Create the Brands
        restBrandsMockMvc.perform(post("/api/brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brands)))
            .andExpect(status().isCreated());

        // Validate the Brands in the database
        List<Brands> brandsList = brandsRepository.findAll();
        assertThat(brandsList).hasSize(databaseSizeBeforeCreate + 1);
        Brands testBrands = brandsList.get(brandsList.size() - 1);
        assertThat(testBrands.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrands.getShtDesc()).isEqualTo(DEFAULT_SHT_DESC);
        assertThat(testBrands.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Brands in Elasticsearch
        Brands brandsEs = brandsSearchRepository.findOne(testBrands.getId());
        assertThat(brandsEs).isEqualToComparingFieldByField(testBrands);
    }

    @Test
    @Transactional
    public void createBrandsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brandsRepository.findAll().size();

        // Create the Brands with an existing ID
        brands.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrandsMockMvc.perform(post("/api/brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brands)))
            .andExpect(status().isBadRequest());

        // Validate the Brands in the database
        List<Brands> brandsList = brandsRepository.findAll();
        assertThat(brandsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBrands() throws Exception {
        // Initialize the database
        brandsRepository.saveAndFlush(brands);

        // Get all the brandsList
        restBrandsMockMvc.perform(get("/api/brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brands.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDesc").value(hasItem(DEFAULT_SHT_DESC.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBrands() throws Exception {
        // Initialize the database
        brandsRepository.saveAndFlush(brands);

        // Get the brands
        restBrandsMockMvc.perform(get("/api/brands/{id}", brands.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brands.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shtDesc").value(DEFAULT_SHT_DESC.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrands() throws Exception {
        // Get the brands
        restBrandsMockMvc.perform(get("/api/brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrands() throws Exception {
        // Initialize the database
        brandsService.save(brands);

        int databaseSizeBeforeUpdate = brandsRepository.findAll().size();

        // Update the brands
        Brands updatedBrands = brandsRepository.findOne(brands.getId());
        // Disconnect from session so that the updates on updatedBrands are not directly saved in db
        em.detach(updatedBrands);
        updatedBrands
            .name(UPDATED_NAME)
            .shtDesc(UPDATED_SHT_DESC)
            .description(UPDATED_DESCRIPTION);

        restBrandsMockMvc.perform(put("/api/brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBrands)))
            .andExpect(status().isOk());

        // Validate the Brands in the database
        List<Brands> brandsList = brandsRepository.findAll();
        assertThat(brandsList).hasSize(databaseSizeBeforeUpdate);
        Brands testBrands = brandsList.get(brandsList.size() - 1);
        assertThat(testBrands.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrands.getShtDesc()).isEqualTo(UPDATED_SHT_DESC);
        assertThat(testBrands.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Brands in Elasticsearch
        Brands brandsEs = brandsSearchRepository.findOne(testBrands.getId());
        assertThat(brandsEs).isEqualToComparingFieldByField(testBrands);
    }

    @Test
    @Transactional
    public void updateNonExistingBrands() throws Exception {
        int databaseSizeBeforeUpdate = brandsRepository.findAll().size();

        // Create the Brands

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBrandsMockMvc.perform(put("/api/brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brands)))
            .andExpect(status().isCreated());

        // Validate the Brands in the database
        List<Brands> brandsList = brandsRepository.findAll();
        assertThat(brandsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBrands() throws Exception {
        // Initialize the database
        brandsService.save(brands);

        int databaseSizeBeforeDelete = brandsRepository.findAll().size();

        // Get the brands
        restBrandsMockMvc.perform(delete("/api/brands/{id}", brands.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean brandsExistsInEs = brandsSearchRepository.exists(brands.getId());
        assertThat(brandsExistsInEs).isFalse();

        // Validate the database is empty
        List<Brands> brandsList = brandsRepository.findAll();
        assertThat(brandsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBrands() throws Exception {
        // Initialize the database
        brandsService.save(brands);

        // Search the brands
        restBrandsMockMvc.perform(get("/api/_search/brands?query=id:" + brands.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brands.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shtDesc").value(hasItem(DEFAULT_SHT_DESC.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Brands.class);
        Brands brands1 = new Brands();
        brands1.setId(1L);
        Brands brands2 = new Brands();
        brands2.setId(brands1.getId());
        assertThat(brands1).isEqualTo(brands2);
        brands2.setId(2L);
        assertThat(brands1).isNotEqualTo(brands2);
        brands1.setId(null);
        assertThat(brands1).isNotEqualTo(brands2);
    }
}
