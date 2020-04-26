package pl.ailux.carparts.web.rest;

import pl.ailux.carparts.CarPartsApp;
import pl.ailux.carparts.domain.CarMake;
import pl.ailux.carparts.repository.CarMakeRepository;
import pl.ailux.carparts.service.CarMakeService;
import pl.ailux.carparts.service.dto.CarMakeDTO;
import pl.ailux.carparts.service.mapper.CarMakeMapper;
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
import java.util.List;

import static pl.ailux.carparts.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarMakeResource} REST controller.
 */
@SpringBootTest(classes = CarPartsApp.class)
public class CarMakeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CarMakeRepository carMakeRepository;

    @Autowired
    private CarMakeMapper carMakeMapper;

    @Autowired
    private CarMakeService carMakeService;

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

    private MockMvc restCarMakeMockMvc;

    private CarMake carMake;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarMakeResource carMakeResource = new CarMakeResource(carMakeService);
        this.restCarMakeMockMvc = MockMvcBuilders.standaloneSetup(carMakeResource)
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
    public static CarMake createEntity(EntityManager em) {
        CarMake carMake = new CarMake()
            .name(DEFAULT_NAME);
        return carMake;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarMake createUpdatedEntity(EntityManager em) {
        CarMake carMake = new CarMake()
            .name(UPDATED_NAME);
        return carMake;
    }

    @BeforeEach
    public void initTest() {
        carMake = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarMake() throws Exception {
        int databaseSizeBeforeCreate = carMakeRepository.findAll().size();

        // Create the CarMake
        CarMakeDTO carMakeDTO = carMakeMapper.toDto(carMake);
        restCarMakeMockMvc.perform(post("/api/car-makes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carMakeDTO)))
            .andExpect(status().isCreated());

        // Validate the CarMake in the database
        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeCreate + 1);
        CarMake testCarMake = carMakeList.get(carMakeList.size() - 1);
        assertThat(testCarMake.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCarMakeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carMakeRepository.findAll().size();

        // Create the CarMake with an existing ID
        carMake.setId(1L);
        CarMakeDTO carMakeDTO = carMakeMapper.toDto(carMake);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMakeMockMvc.perform(post("/api/car-makes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carMakeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarMake in the database
        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carMakeRepository.findAll().size();
        // set the field null
        carMake.setName(null);

        // Create the CarMake, which fails.
        CarMakeDTO carMakeDTO = carMakeMapper.toDto(carMake);

        restCarMakeMockMvc.perform(post("/api/car-makes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carMakeDTO)))
            .andExpect(status().isBadRequest());

        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarMakes() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        // Get all the carMakeList
        restCarMakeMockMvc.perform(get("/api/car-makes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carMake.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCarMake() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        // Get the carMake
        restCarMakeMockMvc.perform(get("/api/car-makes/{id}", carMake.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carMake.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCarMake() throws Exception {
        // Get the carMake
        restCarMakeMockMvc.perform(get("/api/car-makes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarMake() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        int databaseSizeBeforeUpdate = carMakeRepository.findAll().size();

        // Update the carMake
        CarMake updatedCarMake = carMakeRepository.findById(carMake.getId()).get();
        // Disconnect from session so that the updates on updatedCarMake are not directly saved in db
        em.detach(updatedCarMake);
        updatedCarMake
            .name(UPDATED_NAME);
        CarMakeDTO carMakeDTO = carMakeMapper.toDto(updatedCarMake);

        restCarMakeMockMvc.perform(put("/api/car-makes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carMakeDTO)))
            .andExpect(status().isOk());

        // Validate the CarMake in the database
        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeUpdate);
        CarMake testCarMake = carMakeList.get(carMakeList.size() - 1);
        assertThat(testCarMake.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCarMake() throws Exception {
        int databaseSizeBeforeUpdate = carMakeRepository.findAll().size();

        // Create the CarMake
        CarMakeDTO carMakeDTO = carMakeMapper.toDto(carMake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMakeMockMvc.perform(put("/api/car-makes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carMakeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarMake in the database
        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarMake() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        int databaseSizeBeforeDelete = carMakeRepository.findAll().size();

        // Delete the carMake
        restCarMakeMockMvc.perform(delete("/api/car-makes/{id}", carMake.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarMake> carMakeList = carMakeRepository.findAll();
        assertThat(carMakeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
