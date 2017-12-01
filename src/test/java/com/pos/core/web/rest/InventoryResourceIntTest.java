package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Inventory;
import com.pos.core.repository.InventoryRepository;
import com.pos.core.service.InventoryService;
import com.pos.core.repository.search.InventorySearchRepository;
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
 * Test class for the InventoryResource REST controller.
 *
 * @see InventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class InventoryResourceIntTest {

    private static final String DEFAULT_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NO = "BBBBBBBBBB";

    private static final String DEFAULT_BAR_CODE_NO = "AAAAAAAAAA";
    private static final String UPDATED_BAR_CODE_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final Integer DEFAULT_SOLD_TO_DATE = 0;
    private static final Integer UPDATED_SOLD_TO_DATE = 1;

    private static final Integer DEFAULT_AVAILABLE_ITEMS = 0;
    private static final Integer UPDATED_AVAILABLE_ITEMS = 1;

    private static final Integer DEFAULT_UNIT_SELLING_PRICE = 0;
    private static final Integer UPDATED_UNIT_SELLING_PRICE = 1;

    private static final Integer DEFAULT_UNIT_BUYING_PRICE = 0;
    private static final Integer UPDATED_UNIT_BUYING_PRICE = 1;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventorySearchRepository inventorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InventoryResource inventoryResource = new InventoryResource(inventoryService);
        this.restInventoryMockMvc = MockMvcBuilders.standaloneSetup(inventoryResource)
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
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .batchNo(DEFAULT_BATCH_NO)
            .barCodeNo(DEFAULT_BAR_CODE_NO)
            .quantity(DEFAULT_QUANTITY)
            .soldToDate(DEFAULT_SOLD_TO_DATE)
            .availableItems(DEFAULT_AVAILABLE_ITEMS)
            .unitSellingPrice(DEFAULT_UNIT_SELLING_PRICE)
            .unitBuyingPrice(DEFAULT_UNIT_BUYING_PRICE);
        return inventory;
    }

    @Before
    public void initTest() {
        inventorySearchRepository.deleteAll();
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getBatchNo()).isEqualTo(DEFAULT_BATCH_NO);
        assertThat(testInventory.getBarCodeNo()).isEqualTo(DEFAULT_BAR_CODE_NO);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInventory.getSoldToDate()).isEqualTo(DEFAULT_SOLD_TO_DATE);
        assertThat(testInventory.getAvailableItems()).isEqualTo(DEFAULT_AVAILABLE_ITEMS);
        assertThat(testInventory.getUnitSellingPrice()).isEqualTo(DEFAULT_UNIT_SELLING_PRICE);
        assertThat(testInventory.getUnitBuyingPrice()).isEqualTo(DEFAULT_UNIT_BUYING_PRICE);

        // Validate the Inventory in Elasticsearch
        Inventory inventoryEs = inventorySearchRepository.findOne(testInventory.getId());
        assertThat(inventoryEs).isEqualToComparingFieldByField(testInventory);
    }

    @Test
    @Transactional
    public void createInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory with an existing ID
        inventory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBatchNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setBatchNo(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBarCodeNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setBarCodeNo(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setQuantity(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitSellingPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setUnitSellingPrice(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitBuyingPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setUnitBuyingPrice(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].barCodeNo").value(hasItem(DEFAULT_BAR_CODE_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].soldToDate").value(hasItem(DEFAULT_SOLD_TO_DATE)))
            .andExpect(jsonPath("$.[*].availableItems").value(hasItem(DEFAULT_AVAILABLE_ITEMS)))
            .andExpect(jsonPath("$.[*].unitSellingPrice").value(hasItem(DEFAULT_UNIT_SELLING_PRICE)))
            .andExpect(jsonPath("$.[*].unitBuyingPrice").value(hasItem(DEFAULT_UNIT_BUYING_PRICE)));
    }

    @Test
    @Transactional
    public void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.batchNo").value(DEFAULT_BATCH_NO.toString()))
            .andExpect(jsonPath("$.barCodeNo").value(DEFAULT_BAR_CODE_NO.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.soldToDate").value(DEFAULT_SOLD_TO_DATE))
            .andExpect(jsonPath("$.availableItems").value(DEFAULT_AVAILABLE_ITEMS))
            .andExpect(jsonPath("$.unitSellingPrice").value(DEFAULT_UNIT_SELLING_PRICE))
            .andExpect(jsonPath("$.unitBuyingPrice").value(DEFAULT_UNIT_BUYING_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findOne(inventory.getId());
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory
            .batchNo(UPDATED_BATCH_NO)
            .barCodeNo(UPDATED_BAR_CODE_NO)
            .quantity(UPDATED_QUANTITY)
            .soldToDate(UPDATED_SOLD_TO_DATE)
            .availableItems(UPDATED_AVAILABLE_ITEMS)
            .unitSellingPrice(UPDATED_UNIT_SELLING_PRICE)
            .unitBuyingPrice(UPDATED_UNIT_BUYING_PRICE);

        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInventory)))
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getBatchNo()).isEqualTo(UPDATED_BATCH_NO);
        assertThat(testInventory.getBarCodeNo()).isEqualTo(UPDATED_BAR_CODE_NO);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventory.getSoldToDate()).isEqualTo(UPDATED_SOLD_TO_DATE);
        assertThat(testInventory.getAvailableItems()).isEqualTo(UPDATED_AVAILABLE_ITEMS);
        assertThat(testInventory.getUnitSellingPrice()).isEqualTo(UPDATED_UNIT_SELLING_PRICE);
        assertThat(testInventory.getUnitBuyingPrice()).isEqualTo(UPDATED_UNIT_BUYING_PRICE);

        // Validate the Inventory in Elasticsearch
        Inventory inventoryEs = inventorySearchRepository.findOne(testInventory.getId());
        assertThat(inventoryEs).isEqualToComparingFieldByField(testInventory);
    }

    @Test
    @Transactional
    public void updateNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Create the Inventory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Get the inventory
        restInventoryMockMvc.perform(delete("/api/inventories/{id}", inventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean inventoryExistsInEs = inventorySearchRepository.exists(inventory.getId());
        assertThat(inventoryExistsInEs).isFalse();

        // Validate the database is empty
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        // Search the inventory
        restInventoryMockMvc.perform(get("/api/_search/inventories?query=id:" + inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO.toString())))
            .andExpect(jsonPath("$.[*].barCodeNo").value(hasItem(DEFAULT_BAR_CODE_NO.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].soldToDate").value(hasItem(DEFAULT_SOLD_TO_DATE)))
            .andExpect(jsonPath("$.[*].availableItems").value(hasItem(DEFAULT_AVAILABLE_ITEMS)))
            .andExpect(jsonPath("$.[*].unitSellingPrice").value(hasItem(DEFAULT_UNIT_SELLING_PRICE)))
            .andExpect(jsonPath("$.[*].unitBuyingPrice").value(hasItem(DEFAULT_UNIT_BUYING_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        Inventory inventory2 = new Inventory();
        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);
        inventory2.setId(2L);
        assertThat(inventory1).isNotEqualTo(inventory2);
        inventory1.setId(null);
        assertThat(inventory1).isNotEqualTo(inventory2);
    }
}
