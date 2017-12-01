package com.pos.core.web.rest;

import com.pos.core.PosApp;

import com.pos.core.domain.Employees;
import com.pos.core.repository.EmployeesRepository;
import com.pos.core.service.EmployeesService;
import com.pos.core.repository.search.EmployeesSearchRepository;
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

import com.pos.core.domain.enumeration.GENDER;
import com.pos.core.domain.enumeration.Banks;
import com.pos.core.domain.enumeration.PAYMENT_TYPES;
/**
 * Test class for the EmployeesResource REST controller.
 *
 * @see EmployeesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PosApp.class)
public class EmployeesResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_NO = 1;
    private static final Integer UPDATED_ID_NO = 2;

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final GENDER DEFAULT_GENDER = GENDER.MALE;
    private static final GENDER UPDATED_GENDER = GENDER.FEMALE;

    private static final String DEFAULT_ESTATE = "AAAAAAAAAA";
    private static final String UPDATED_ESTATE = "BBBBBBBBBB";

    private static final String DEFAULT_APARTMENT_NO = "AAAAAAAAAA";
    private static final String UPDATED_APARTMENT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Banks DEFAULT_BANK_NAME = Banks.KCB;
    private static final Banks UPDATED_BANK_NAME = Banks.EQUITY;

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final PAYMENT_TYPES DEFAULT_PAYMENT_MODE = PAYMENT_TYPES.BANK;
    private static final PAYMENT_TYPES UPDATED_PAYMENT_MODE = PAYMENT_TYPES.MPESA;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private EmployeesSearchRepository employeesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeesMockMvc;

    private Employees employees;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeesResource employeesResource = new EmployeesResource(employeesService);
        this.restEmployeesMockMvc = MockMvcBuilders.standaloneSetup(employeesResource)
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
    public static Employees createEntity(EntityManager em) {
        Employees employees = new Employees()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .idNo(DEFAULT_ID_NO)
            .dob(DEFAULT_DOB)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .estate(DEFAULT_ESTATE)
            .apartmentNo(DEFAULT_APARTMENT_NO)
            .email(DEFAULT_EMAIL)
            .bankName(DEFAULT_BANK_NAME)
            .accountNo(DEFAULT_ACCOUNT_NO)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .paymentMode(DEFAULT_PAYMENT_MODE);
        return employees;
    }

    @Before
    public void initTest() {
        employeesSearchRepository.deleteAll();
        employees = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployees() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // Create the Employees
        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate + 1);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployees.getIdNo()).isEqualTo(DEFAULT_ID_NO);
        assertThat(testEmployees.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testEmployees.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testEmployees.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployees.getEstate()).isEqualTo(DEFAULT_ESTATE);
        assertThat(testEmployees.getApartmentNo()).isEqualTo(DEFAULT_APARTMENT_NO);
        assertThat(testEmployees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployees.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testEmployees.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testEmployees.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testEmployees.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);

        // Validate the Employees in Elasticsearch
        Employees employeesEs = employeesSearchRepository.findOne(testEmployees.getId());
        assertThat(employeesEs).isEqualToComparingFieldByField(testEmployees);
    }

    @Test
    @Transactional
    public void createEmployeesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // Create the Employees with an existing ID
        employees.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setFirstName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setLastName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setIdNo(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setDob(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setAge(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setGender(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setEstate(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setEmail(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBankNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setBankName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setAccountNo(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setAccountName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setPaymentMode(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList
        restEmployeesMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].idNo").value(hasItem(DEFAULT_ID_NO)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].estate").value(hasItem(DEFAULT_ESTATE.toString())))
            .andExpect(jsonPath("$.[*].apartmentNo").value(hasItem(DEFAULT_APARTMENT_NO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO.toString())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())));
    }

    @Test
    @Transactional
    public void getEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees/{id}", employees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employees.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.idNo").value(DEFAULT_ID_NO))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.estate").value(DEFAULT_ESTATE.toString()))
            .andExpect(jsonPath("$.apartmentNo").value(DEFAULT_APARTMENT_NO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO.toString()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployees() throws Exception {
        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployees() throws Exception {
        // Initialize the database
        employeesService.save(employees);

        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees
        Employees updatedEmployees = employeesRepository.findOne(employees.getId());
        // Disconnect from session so that the updates on updatedEmployees are not directly saved in db
        em.detach(updatedEmployees);
        updatedEmployees
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNo(UPDATED_ID_NO)
            .dob(UPDATED_DOB)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .estate(UPDATED_ESTATE)
            .apartmentNo(UPDATED_APARTMENT_NO)
            .email(UPDATED_EMAIL)
            .bankName(UPDATED_BANK_NAME)
            .accountNo(UPDATED_ACCOUNT_NO)
            .accountName(UPDATED_ACCOUNT_NAME)
            .paymentMode(UPDATED_PAYMENT_MODE);

        restEmployeesMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployees)))
            .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees.getIdNo()).isEqualTo(UPDATED_ID_NO);
        assertThat(testEmployees.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testEmployees.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testEmployees.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployees.getEstate()).isEqualTo(UPDATED_ESTATE);
        assertThat(testEmployees.getApartmentNo()).isEqualTo(UPDATED_APARTMENT_NO);
        assertThat(testEmployees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployees.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testEmployees.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testEmployees.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testEmployees.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);

        // Validate the Employees in Elasticsearch
        Employees employeesEs = employeesSearchRepository.findOne(testEmployees.getId());
        assertThat(employeesEs).isEqualToComparingFieldByField(testEmployees);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Create the Employees

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeesMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmployees() throws Exception {
        // Initialize the database
        employeesService.save(employees);

        int databaseSizeBeforeDelete = employeesRepository.findAll().size();

        // Get the employees
        restEmployeesMockMvc.perform(delete("/api/employees/{id}", employees.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employeesExistsInEs = employeesSearchRepository.exists(employees.getId());
        assertThat(employeesExistsInEs).isFalse();

        // Validate the database is empty
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmployees() throws Exception {
        // Initialize the database
        employeesService.save(employees);

        // Search the employees
        restEmployeesMockMvc.perform(get("/api/_search/employees?query=id:" + employees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].idNo").value(hasItem(DEFAULT_ID_NO)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].estate").value(hasItem(DEFAULT_ESTATE.toString())))
            .andExpect(jsonPath("$.[*].apartmentNo").value(hasItem(DEFAULT_APARTMENT_NO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO.toString())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employees.class);
        Employees employees1 = new Employees();
        employees1.setId(1L);
        Employees employees2 = new Employees();
        employees2.setId(employees1.getId());
        assertThat(employees1).isEqualTo(employees2);
        employees2.setId(2L);
        assertThat(employees1).isNotEqualTo(employees2);
        employees1.setId(null);
        assertThat(employees1).isNotEqualTo(employees2);
    }
}
