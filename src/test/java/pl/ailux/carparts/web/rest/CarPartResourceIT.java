package pl.ailux.carparts.web.rest;

import pl.ailux.carparts.CarPartsApp;
import pl.ailux.carparts.domain.CarPart;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.service.CarPartService;
import pl.ailux.carparts.service.dto.CarPartDTO;
import pl.ailux.carparts.service.mapper.CarPartMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static pl.ailux.carparts.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarPartResource} REST controller.
 */
@SpringBootTest(classes = CarPartsApp.class)
public class CarPartResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final Integer DEFAULT_SHIPPING_TIME = 1;
    private static final Integer UPDATED_SHIPPING_TIME = 2;

    @Autowired
    private CarPartRepository carPartRepository;

    @Autowired
    private CarPartMapper carPartMapper;

    @Autowired
    private CarPartService carPartService;

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

    private MockMvc restCarPartMockMvc;

    private CarPart carPart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarPartResource carPartResource = new CarPartResource(carPartService);
        this.restCarPartMockMvc = MockMvcBuilders.standaloneSetup(carPartResource)
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
    public static CarPart createEntity(EntityManager em) {
        CarPart carPart = new CarPart()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .available(DEFAULT_AVAILABLE)
            .shippingTime(DEFAULT_SHIPPING_TIME);
        return carPart;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarPart createUpdatedEntity(EntityManager em) {
        CarPart carPart = new CarPart()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .available(UPDATED_AVAILABLE)
            .shippingTime(UPDATED_SHIPPING_TIME);
        return carPart;
    }

    @BeforeEach
    public void initTest() {
        carPart = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarPart() throws Exception {
        int databaseSizeBeforeCreate = carPartRepository.findAll().size();

        // Create the CarPart
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);
        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isCreated());

        // Validate the CarPart in the database
        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeCreate + 1);
        CarPart testCarPart = carPartList.get(carPartList.size() - 1);
        assertThat(testCarPart.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarPart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCarPart.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCarPart.isAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testCarPart.getShippingTime()).isEqualTo(DEFAULT_SHIPPING_TIME);
    }

    @Test
    @Transactional
    public void createCarPartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carPartRepository.findAll().size();

        // Create the CarPart with an existing ID
        carPart.setId(1L);
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarPart in the database
        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPartRepository.findAll().size();
        // set the field null
        carPart.setName(null);

        // Create the CarPart, which fails.
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPartRepository.findAll().size();
        // set the field null
        carPart.setPrice(null);

        // Create the CarPart, which fails.
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPartRepository.findAll().size();
        // set the field null
        carPart.setAvailable(null);

        // Create the CarPart, which fails.
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShippingTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPartRepository.findAll().size();
        // set the field null
        carPart.setShippingTime(null);

        // Create the CarPart, which fails.
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        restCarPartMockMvc.perform(post("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarParts() throws Exception {
        // Initialize the database
        carPartRepository.saveAndFlush(carPart);

        // Get all the carPartList
        restCarPartMockMvc.perform(get("/api/car-parts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carPart.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].shippingTime").value(hasItem(DEFAULT_SHIPPING_TIME)));
    }
    
    @Test
    @Transactional
    public void getCarPart() throws Exception {
        // Initialize the database
        carPartRepository.saveAndFlush(carPart);

        // Get the carPart
        restCarPartMockMvc.perform(get("/api/car-parts/{id}", carPart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carPart.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.shippingTime").value(DEFAULT_SHIPPING_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingCarPart() throws Exception {
        // Get the carPart
        restCarPartMockMvc.perform(get("/api/car-parts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarPart() throws Exception {
        // Initialize the database
        carPartRepository.saveAndFlush(carPart);

        int databaseSizeBeforeUpdate = carPartRepository.findAll().size();

        // Update the carPart
        CarPart updatedCarPart = carPartRepository.findById(carPart.getId()).get();
        // Disconnect from session so that the updates on updatedCarPart are not directly saved in db
        em.detach(updatedCarPart);
        updatedCarPart
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .available(UPDATED_AVAILABLE)
            .shippingTime(UPDATED_SHIPPING_TIME);
        CarPartDTO carPartDTO = carPartMapper.toDto(updatedCarPart);

        restCarPartMockMvc.perform(put("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isOk());

        // Validate the CarPart in the database
        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeUpdate);
        CarPart testCarPart = carPartList.get(carPartList.size() - 1);
        assertThat(testCarPart.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarPart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCarPart.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCarPart.isAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testCarPart.getShippingTime()).isEqualTo(UPDATED_SHIPPING_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingCarPart() throws Exception {
        int databaseSizeBeforeUpdate = carPartRepository.findAll().size();

        // Create the CarPart
        CarPartDTO carPartDTO = carPartMapper.toDto(carPart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarPartMockMvc.perform(put("/api/car-parts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carPartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarPart in the database
        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarPart() throws Exception {
        // Initialize the database
        carPartRepository.saveAndFlush(carPart);

        int databaseSizeBeforeDelete = carPartRepository.findAll().size();

        // Delete the carPart
        restCarPartMockMvc.perform(delete("/api/car-parts/{id}", carPart.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarPart> carPartList = carPartRepository.findAll();
        assertThat(carPartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
