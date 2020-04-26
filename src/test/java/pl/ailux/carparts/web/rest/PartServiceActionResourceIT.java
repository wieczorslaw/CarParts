package pl.ailux.carparts.web.rest;

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
import pl.ailux.carparts.CarPartsApp;
import pl.ailux.carparts.domain.CarPart;
import pl.ailux.carparts.domain.PartServiceAction;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.repository.PartServiceActionRepository;
import pl.ailux.carparts.service.PartServiceActionService;
import pl.ailux.carparts.service.dto.PartServiceActionDTO;
import pl.ailux.carparts.service.mapper.PartServiceActionMapper;
import pl.ailux.carparts.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.ailux.carparts.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link PartServiceActionResource} REST controller.
 */
@SpringBootTest(classes = CarPartsApp.class)
public class PartServiceActionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PartServiceActionRepository partServiceActionRepository;

    @Autowired
    private CarPartRepository carPartRepository;

    @Autowired
    private PartServiceActionMapper partServiceActionMapper;

    @Autowired
    private PartServiceActionService partServiceActionService;

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

    private MockMvc restPartServiceActionMockMvc;

    private PartServiceAction partServiceAction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartServiceActionResource partServiceActionResource = new PartServiceActionResource(partServiceActionService);
        this.restPartServiceActionMockMvc = MockMvcBuilders.standaloneSetup(partServiceActionResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartServiceAction createEntity(final EntityManager em) {
        final PartServiceAction partServiceAction = new PartServiceAction()
                .name(DEFAULT_NAME)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return partServiceAction;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartServiceAction createUpdatedEntity(final EntityManager em) {
        final PartServiceAction partServiceAction = new PartServiceAction()
                .name(UPDATED_NAME)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);
        return partServiceAction;
    }

    @BeforeEach
    public void initTest() {
        partServiceAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartServiceAction() throws Exception {
        CarPart carPart = new CarPart();
        carPart.setAvailable(false);
        carPart.setName("cp_name");
        carPart.setPrice(new BigDecimal("11.5"));
        carPart.setShippingTime(14);
        carPart.setDescription("cp_desc");

        carPart = carPartRepository.saveAndFlush(carPart);
        final int databaseSizeBeforeCreate = partServiceActionRepository.findAll().size();

        // Create the PartServiceAction
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);
        final String url = "/api/car-parts/" + carPart.getId() + "/part-service-actions";
        restPartServiceActionMockMvc.perform(post(url)
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isCreated());

        // Validate the PartServiceAction in the database
        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeCreate + 1);
        final PartServiceAction testPartServiceAction = partServiceActionList.get(partServiceActionList.size() - 1);
        assertThat(testPartServiceAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPartServiceAction.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPartServiceAction.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createPartServiceActionWithExistingId() throws Exception {
        final int databaseSizeBeforeCreate = partServiceActionRepository.findAll().size();

        // Create the PartServiceAction with an existing ID
        partServiceAction.setId(1L);
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartServiceActionMockMvc.perform(post("/api/car-parts/1/part-service-actions")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isBadRequest());

        // Validate the PartServiceAction in the database
        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        final int databaseSizeBeforeTest = partServiceActionRepository.findAll().size();
        // set the field null
        partServiceAction.setName(null);

        // Create the PartServiceAction, which fails.
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);

        restPartServiceActionMockMvc.perform(post("/api/car-parts/1/part-service-actions")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isBadRequest());

        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        final int databaseSizeBeforeTest = partServiceActionRepository.findAll().size();
        // set the field null
        partServiceAction.setStartDate(null);

        // Create the PartServiceAction, which fails.
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);

        restPartServiceActionMockMvc.perform(post("/api/car-parts/1/part-service-actions")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isBadRequest());

        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        final int databaseSizeBeforeTest = partServiceActionRepository.findAll().size();
        // set the field null
        partServiceAction.setEndDate(null);

        // Create the PartServiceAction, which fails.
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);

        restPartServiceActionMockMvc.perform(post("/api/car-parts/1/part-service-actions")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isBadRequest());

        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartServiceActions() throws Exception {
        // Initialize the database
        partServiceActionRepository.saveAndFlush(partServiceAction);

        // Get all the partServiceActionList
        restPartServiceActionMockMvc.perform(get("/api/part-service-actions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partServiceAction.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPartServiceAction() throws Exception {
        // Initialize the database
        partServiceActionRepository.saveAndFlush(partServiceAction);

        // Get the partServiceAction
        restPartServiceActionMockMvc.perform(get("/api/part-service-actions/{id}", partServiceAction.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(partServiceAction.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
                .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPartServiceAction() throws Exception {
        // Get the partServiceAction
        restPartServiceActionMockMvc.perform(get("/api/part-service-actions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartServiceAction() throws Exception {
        // Initialize the database
        CarPart carPart = new CarPart();
        carPart.setAvailable(false);
        carPart.setName("cp_name");
        carPart.setPrice(new BigDecimal("11.5"));
        carPart.setShippingTime(14);
        carPart.setDescription("cp_desc");
        carPart = carPartRepository.saveAndFlush(carPart);
        partServiceActionRepository.saveAndFlush(partServiceAction);

        final int databaseSizeBeforeUpdate = partServiceActionRepository.findAll().size();

        // Update the partServiceAction
        final PartServiceAction updatedPartServiceAction = partServiceActionRepository.findById(partServiceAction.getId()).get();
        // Disconnect from session so that the updates on updatedPartServiceAction are not directly saved in db
        em.detach(updatedPartServiceAction);
        updatedPartServiceAction
                .name(UPDATED_NAME)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(updatedPartServiceAction);

        final String url = "/api/car-parts/" + carPart.getId() + "/part-service-actions";
        restPartServiceActionMockMvc.perform(put(url)
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isOk());

        // Validate the PartServiceAction in the database
        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeUpdate);
        final PartServiceAction testPartServiceAction = partServiceActionList.get(partServiceActionList.size() - 1);
        assertThat(testPartServiceAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPartServiceAction.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPartServiceAction.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPartServiceAction() throws Exception {
        final int databaseSizeBeforeUpdate = partServiceActionRepository.findAll().size();

        // Create the PartServiceAction
        final PartServiceActionDTO partServiceActionDTO = partServiceActionMapper.toDto(partServiceAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartServiceActionMockMvc.perform(put("/api/car-parts/1/part-service-actions")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partServiceActionDTO)))
                .andExpect(status().isBadRequest());

        // Validate the PartServiceAction in the database
        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePartServiceAction() throws Exception {
        // Initialize the database
        partServiceActionRepository.saveAndFlush(partServiceAction);

        final int databaseSizeBeforeDelete = partServiceActionRepository.findAll().size();

        // Delete the partServiceAction
        restPartServiceActionMockMvc.perform(delete("/api/part-service-actions/{id}", partServiceAction.getId())
                .accept(TestUtil.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        final List<PartServiceAction> partServiceActionList = partServiceActionRepository.findAll();
        assertThat(partServiceActionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
