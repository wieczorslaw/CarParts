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
import pl.ailux.carparts.domain.PartSellingArgument;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.repository.PartSellingArgumentRepository;
import pl.ailux.carparts.service.PartSellingArgumentService;
import pl.ailux.carparts.service.dto.PartSellingArgumentDTO;
import pl.ailux.carparts.service.mapper.PartSellingArgumentMapper;
import pl.ailux.carparts.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PartSellingArgumentResource} REST controller.
 */
@SpringBootTest(classes = CarPartsApp.class)
public class PartSellingArgumentResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    @Autowired
    private PartSellingArgumentRepository partSellingArgumentRepository;

    @Autowired
    private CarPartRepository carPartRepository;

    @Autowired
    private PartSellingArgumentMapper partSellingArgumentMapper;

    @Autowired
    private PartSellingArgumentService partSellingArgumentService;

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

    private MockMvc restPartSellingArgumentMockMvc;

    private PartSellingArgument partSellingArgument;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartSellingArgumentResource partSellingArgumentResource = new PartSellingArgumentResource(partSellingArgumentService);
        this.restPartSellingArgumentMockMvc = MockMvcBuilders.standaloneSetup(partSellingArgumentResource)
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
    public static PartSellingArgument createEntity(final EntityManager em) {
        final PartSellingArgument partSellingArgument = new PartSellingArgument()
                .reason(DEFAULT_REASON);
        return partSellingArgument;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartSellingArgument createUpdatedEntity(final EntityManager em) {
        final PartSellingArgument partSellingArgument = new PartSellingArgument()
                .reason(UPDATED_REASON);
        return partSellingArgument;
    }

    @BeforeEach
    public void initTest() {
        partSellingArgument = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartSellingArgument() throws Exception {
        CarPart carPart = new CarPart();
        carPart.setAvailable(false);
        carPart.setName("cp_name");
        carPart.setPrice(new BigDecimal("11.5"));
        carPart.setShippingTime(14);
        carPart.setDescription("cp_desc");

        carPart = carPartRepository.saveAndFlush(carPart);
        final int databaseSizeBeforeCreate = partSellingArgumentRepository.findAll().size();

        // Create the PartSellingArgument
        final PartSellingArgumentDTO partSellingArgumentDTO = partSellingArgumentMapper.toDto(partSellingArgument);
        final String url = "/api/car-parts/" + carPart.getId() + "/part-selling-arguments";
        restPartSellingArgumentMockMvc.perform(post(url)
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partSellingArgumentDTO)))
                .andExpect(status().isCreated());

        // Validate the PartSellingArgument in the database
        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeCreate + 1);
        final PartSellingArgument testPartSellingArgument = partSellingArgumentList.get(partSellingArgumentList.size() - 1);
        assertThat(testPartSellingArgument.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    public void createPartSellingArgumentWithExistingId() throws Exception {
        final int databaseSizeBeforeCreate = partSellingArgumentRepository.findAll().size();

        // Create the PartSellingArgument with an existing ID
        partSellingArgument.setId(1L);
        final PartSellingArgumentDTO partSellingArgumentDTO = partSellingArgumentMapper.toDto(partSellingArgument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartSellingArgumentMockMvc.perform(post("/api/car-parts/1/part-selling-arguments")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partSellingArgumentDTO)))
                .andExpect(status().isBadRequest());

        // Validate the PartSellingArgument in the database
        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        final int databaseSizeBeforeTest = partSellingArgumentRepository.findAll().size();
        // set the field null
        partSellingArgument.setReason(null);

        // Create the PartSellingArgument, which fails.
        final PartSellingArgumentDTO partSellingArgumentDTO = partSellingArgumentMapper.toDto(partSellingArgument);

        restPartSellingArgumentMockMvc.perform(post("/api/car-parts/1/part-selling-arguments")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partSellingArgumentDTO)))
                .andExpect(status().isBadRequest());

        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartSellingArguments() throws Exception {
        // Initialize the database
        partSellingArgumentRepository.saveAndFlush(partSellingArgument);

        // Get all the partSellingArgumentList
        restPartSellingArgumentMockMvc.perform(get("/api/part-selling-arguments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partSellingArgument.getId().intValue())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    public void getPartSellingArgument() throws Exception {
        // Initialize the database
        partSellingArgumentRepository.saveAndFlush(partSellingArgument);

        // Get the partSellingArgument
        restPartSellingArgumentMockMvc.perform(get("/api/part-selling-arguments/{id}", partSellingArgument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(partSellingArgument.getId().intValue()))
                .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    public void getNonExistingPartSellingArgument() throws Exception {
        // Get the partSellingArgument
        restPartSellingArgumentMockMvc.perform(get("/api/part-selling-arguments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartSellingArgument() throws Exception {
        // Initialize the database
        CarPart carPart = new CarPart();
        carPart.setAvailable(false);
        carPart.setName("cp_name");
        carPart.setPrice(new BigDecimal("11.5"));
        carPart.setShippingTime(14);
        carPart.setDescription("cp_desc");

        carPart = carPartRepository.saveAndFlush(carPart);
        partSellingArgumentRepository.saveAndFlush(partSellingArgument);

        final int databaseSizeBeforeUpdate = partSellingArgumentRepository.findAll().size();

        // Update the partSellingArgument
        final PartSellingArgument updatedPartSellingArgument = partSellingArgumentRepository.findById(partSellingArgument.getId()).get();
        // Disconnect from session so that the updates on updatedPartSellingArgument are not directly saved in db
        em.detach(updatedPartSellingArgument);
        updatedPartSellingArgument
                .reason(UPDATED_REASON);
        final PartSellingArgumentDTO partSellingArgumentDTO = partSellingArgumentMapper.toDto(updatedPartSellingArgument);

        final String url = "/api/car-parts/" + carPart.getId() + "/part-selling-arguments";
        restPartSellingArgumentMockMvc.perform(put(url)
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partSellingArgumentDTO)))
                .andExpect(status().isOk());

        // Validate the PartSellingArgument in the database
        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeUpdate);
        final PartSellingArgument testPartSellingArgument = partSellingArgumentList.get(partSellingArgumentList.size() - 1);
        assertThat(testPartSellingArgument.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    public void updateNonExistingPartSellingArgument() throws Exception {
        final int databaseSizeBeforeUpdate = partSellingArgumentRepository.findAll().size();

        // Create the PartSellingArgument
        final PartSellingArgumentDTO partSellingArgumentDTO = partSellingArgumentMapper.toDto(partSellingArgument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartSellingArgumentMockMvc.perform(put("/api/car-parts/1/part-selling-arguments")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(partSellingArgumentDTO)))
                .andExpect(status().isBadRequest());

        // Validate the PartSellingArgument in the database
        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePartSellingArgument() throws Exception {
        // Initialize the database
        partSellingArgumentRepository.saveAndFlush(partSellingArgument);

        final int databaseSizeBeforeDelete = partSellingArgumentRepository.findAll().size();

        // Delete the partSellingArgument
        restPartSellingArgumentMockMvc.perform(delete("/api/part-selling-arguments/{id}", partSellingArgument.getId())
                .accept(TestUtil.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        final List<PartSellingArgument> partSellingArgumentList = partSellingArgumentRepository.findAll();
        assertThat(partSellingArgumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
