package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.SubCategories;
import com.pos.core.repository.SubCategoriesRepository;
import com.pos.core.service.SubCategoriesService;
import com.pos.core.repository.search.SubCategoriesSearchRepository;
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
 * Test class for the SubCategoriesResource REST controller.
 *
 * @see SubCategoriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class SubCategoriesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_DESCIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SubCategoriesRepository subCategoriesRepository;

    @Autowired
    private SubCategoriesService subCategoriesService;

    @Autowired
    private SubCategoriesSearchRepository subCategoriesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubCategoriesMockMvc;

    private SubCategories subCategories;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubCategoriesResource subCategoriesResource = new SubCategoriesResource(subCategoriesService);
        this.restSubCategoriesMockMvc = MockMvcBuilders.standaloneSetup(subCategoriesResource)
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
    public static SubCategories createEntity(EntityManager em) {
        SubCategories subCategories = new SubCategories()
            .name(DEFAULT_NAME)
            .shortDesciption(DEFAULT_SHORT_DESCIPTION)
            .description(DEFAULT_DESCRIPTION);
        return subCategories;
    }

    @Before
    public void initTest() {
        subCategoriesSearchRepository.deleteAll();
        subCategories = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubCategories() throws Exception {
        int databaseSizeBeforeCreate = subCategoriesRepository.findAll().size();

        // Create the SubCategories
        restSubCategoriesMockMvc.perform(post("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subCategories)))
            .andExpect(status().isCreated());

        // Validate the SubCategories in the database
        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeCreate + 1);
        SubCategories testSubCategories = subCategoriesList.get(subCategoriesList.size() - 1);
        assertThat(testSubCategories.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubCategories.getShortDesciption()).isEqualTo(DEFAULT_SHORT_DESCIPTION);
        assertThat(testSubCategories.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SubCategories in Elasticsearch
        SubCategories subCategoriesEs = subCategoriesSearchRepository.findOne(testSubCategories.getId());
        assertThat(subCategoriesEs).isEqualToComparingFieldByField(testSubCategories);
    }

    @Test
    @Transactional
    public void createSubCategoriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subCategoriesRepository.findAll().size();

        // Create the SubCategories with an existing ID
        subCategories.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoriesMockMvc.perform(post("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subCategories)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategories in the database
        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoriesRepository.findAll().size();
        // set the field null
        subCategories.setName(null);

        // Create the SubCategories, which fails.

        restSubCategoriesMockMvc.perform(post("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subCategories)))
            .andExpect(status().isBadRequest());

        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortDesciptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoriesRepository.findAll().size();
        // set the field null
        subCategories.setShortDesciption(null);

        // Create the SubCategories, which fails.

        restSubCategoriesMockMvc.perform(post("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subCategories)))
            .andExpect(status().isBadRequest());

        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubCategories() throws Exception {
        // Initialize the database
        subCategoriesRepository.saveAndFlush(subCategories);

        // Get all the subCategoriesList
        restSubCategoriesMockMvc.perform(get("/api/sub-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shortDesciption").value(hasItem(DEFAULT_SHORT_DESCIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSubCategories() throws Exception {
        // Initialize the database
        subCategoriesRepository.saveAndFlush(subCategories);

        // Get the subCategories
        restSubCategoriesMockMvc.perform(get("/api/sub-categories/{id}", subCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subCategories.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortDesciption").value(DEFAULT_SHORT_DESCIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubCategories() throws Exception {
        // Get the subCategories
        restSubCategoriesMockMvc.perform(get("/api/sub-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubCategories() throws Exception {
        // Initialize the database
        subCategoriesService.save(subCategories);

        int databaseSizeBeforeUpdate = subCategoriesRepository.findAll().size();

        // Update the subCategories
        SubCategories updatedSubCategories = subCategoriesRepository.findOne(subCategories.getId());
        // Disconnect from session so that the updates on updatedSubCategories are not directly saved in db
        em.detach(updatedSubCategories);
        updatedSubCategories
            .name(UPDATED_NAME)
            .shortDesciption(UPDATED_SHORT_DESCIPTION)
            .description(UPDATED_DESCRIPTION);

        restSubCategoriesMockMvc.perform(put("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubCategories)))
            .andExpect(status().isOk());

        // Validate the SubCategories in the database
        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeUpdate);
        SubCategories testSubCategories = subCategoriesList.get(subCategoriesList.size() - 1);
        assertThat(testSubCategories.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubCategories.getShortDesciption()).isEqualTo(UPDATED_SHORT_DESCIPTION);
        assertThat(testSubCategories.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SubCategories in Elasticsearch
        SubCategories subCategoriesEs = subCategoriesSearchRepository.findOne(testSubCategories.getId());
        assertThat(subCategoriesEs).isEqualToComparingFieldByField(testSubCategories);
    }

    @Test
    @Transactional
    public void updateNonExistingSubCategories() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriesRepository.findAll().size();

        // Create the SubCategories

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubCategoriesMockMvc.perform(put("/api/sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subCategories)))
            .andExpect(status().isCreated());

        // Validate the SubCategories in the database
        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubCategories() throws Exception {
        // Initialize the database
        subCategoriesService.save(subCategories);

        int databaseSizeBeforeDelete = subCategoriesRepository.findAll().size();

        // Get the subCategories
        restSubCategoriesMockMvc.perform(delete("/api/sub-categories/{id}", subCategories.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean subCategoriesExistsInEs = subCategoriesSearchRepository.exists(subCategories.getId());
        assertThat(subCategoriesExistsInEs).isFalse();

        // Validate the database is empty
        List<SubCategories> subCategoriesList = subCategoriesRepository.findAll();
        assertThat(subCategoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubCategories() throws Exception {
        // Initialize the database
        subCategoriesService.save(subCategories);

        // Search the subCategories
        restSubCategoriesMockMvc.perform(get("/api/_search/sub-categories?query=id:" + subCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shortDesciption").value(hasItem(DEFAULT_SHORT_DESCIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategories.class);
        SubCategories subCategories1 = new SubCategories();
        subCategories1.setId(1L);
        SubCategories subCategories2 = new SubCategories();
        subCategories2.setId(subCategories1.getId());
        assertThat(subCategories1).isEqualTo(subCategories2);
        subCategories2.setId(2L);
        assertThat(subCategories1).isNotEqualTo(subCategories2);
        subCategories1.setId(null);
        assertThat(subCategories1).isNotEqualTo(subCategories2);
    }
}
