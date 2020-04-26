package pl.ailux.carparts.web.rest;

import pl.ailux.carparts.CarPartsApp;
import pl.ailux.carparts.domain.CarModel;
import pl.ailux.carparts.repository.CarModelRepository;
import pl.ailux.carparts.service.CarModelService;
import pl.ailux.carparts.service.dto.CarModelDTO;
import pl.ailux.carparts.service.mapper.CarModelMapper;
import pl.ailux.carparts.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pl.ailux.carparts.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarModelResource} REST controller.
 */
@SpringBootTest(classes = CarPartsApp.class)
public class CarModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_PRODUCTION_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRODUCTION_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PRODUCTION_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRODUCTION_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarModelMapper carModelMapper;

    @Autowired
    private CarModelService carModelService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCarModelMockMvc;

    private CarModel carModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarModelResource carModelResource = new CarModelResource(carModelService);
        this.restCarModelMockMvc = MockMvcBuilders.standaloneSetup(carModelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createEntity(EntityManager em) {
        CarModel carModel = new CarModel()
            .name(DEFAULT_NAME)
            .productionStartDate(DEFAULT_PRODUCTION_START_DATE)
            .productionEndDate(DEFAULT_PRODUCTION_END_DATE);
        return carModel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createUpdatedEntity(EntityManager em) {
        CarModel carModel = new CarModel()
            .name(UPDATED_NAME)
            .productionStartDate(UPDATED_PRODUCTION_START_DATE)
            .productionEndDate(UPDATED_PRODUCTION_END_DATE);
        return carModel;
    }

    @BeforeEach
    public void initTest() {
        carModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarModel() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);
        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isCreated());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate + 1);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarModel.getProductionStartDate()).isEqualTo(DEFAULT_PRODUCTION_START_DATE);
        assertThat(testCarModel.getProductionEndDate()).isEqualTo(DEFAULT_PRODUCTION_END_DATE);
    }

    @Test
    @Transactional
    public void createCarModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // Create the CarModel with an existing ID
        carModel.setId(1L);
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carModelRepository.findAll().size();
        // set the field null
        carModel.setName(null);

        // Create the CarModel, which fails.
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductionStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carModelRepository.findAll().size();
        // set the field null
        carModel.setProductionStartDate(null);

        // Create the CarModel, which fails.
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductionEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carModelRepository.findAll().size();
        // set the field null
        carModel.setProductionEndDate(null);

        // Create the CarModel, which fails.
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarModels() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList
        restCarModelMockMvc.perform(get("/api/car-models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].productionStartDate").value(hasItem(DEFAULT_PRODUCTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].productionEndDate").value(hasItem(DEFAULT_PRODUCTION_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.productionStartDate").value(DEFAULT_PRODUCTION_START_DATE.toString()))
            .andExpect(jsonPath("$.productionEndDate").value(DEFAULT_PRODUCTION_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarModel() throws Exception {
        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel
        CarModel updatedCarModel = carModelRepository.findById(carModel.getId()).get();
        // Disconnect from session so that the updates on updatedCarModel are not directly saved in db
        em.detach(updatedCarModel);
        updatedCarModel
            .name(UPDATED_NAME)
            .productionStartDate(UPDATED_PRODUCTION_START_DATE)
            .productionEndDate(UPDATED_PRODUCTION_END_DATE);
        CarModelDTO carModelDTO = carModelMapper.toDto(updatedCarModel);

        restCarModelMockMvc.perform(put("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarModel.getProductionStartDate()).isEqualTo(UPDATED_PRODUCTION_START_DATE);
        assertThat(testCarModel.getProductionEndDate()).isEqualTo(UPDATED_PRODUCTION_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarModelMockMvc.perform(put("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeDelete = carModelRepository.findAll().size();

        // Delete the carModel
        restCarModelMockMvc.perform(delete("/api/car-models/{id}", carModel.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
