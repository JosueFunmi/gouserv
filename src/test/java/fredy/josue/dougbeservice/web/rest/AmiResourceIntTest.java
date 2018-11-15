package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Ami;
import fredy.josue.dougbeservice.repository.AmiRepository;
import fredy.josue.dougbeservice.service.AmiService;
import fredy.josue.dougbeservice.service.dto.AmiDTO;
import fredy.josue.dougbeservice.service.mapper.AmiMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.AmiCriteria;
import fredy.josue.dougbeservice.service.AmiQueryService;

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


import static fredy.josue.dougbeservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AmiResource REST controller.
 *
 * @see AmiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class AmiResourceIntTest {

    private static final Long DEFAULT_INTERESTED = 1L;
    private static final Long UPDATED_INTERESTED = 2L;

    private static final Long DEFAULT_FRIEND = 1L;
    private static final Long UPDATED_FRIEND = 2L;

    private static final Boolean DEFAULT_DEMANDE = false;
    private static final Boolean UPDATED_DEMANDE = true;

    private static final Instant DEFAULT_DATE_AMIS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_AMIS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AmiRepository amiRepository;

    @Autowired
    private AmiMapper amiMapper;

    @Autowired
    private AmiService amiService;

    @Autowired
    private AmiQueryService amiQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAmiMockMvc;

    private Ami ami;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AmiResource amiResource = new AmiResource(amiService, amiQueryService);
        this.restAmiMockMvc = MockMvcBuilders.standaloneSetup(amiResource)
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
    public static Ami createEntity(EntityManager em) {
        Ami ami = new Ami()
            .interested(DEFAULT_INTERESTED)
            .friend(DEFAULT_FRIEND)
            .demande(DEFAULT_DEMANDE)
            .dateAmis(DEFAULT_DATE_AMIS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return ami;
    }

    @Before
    public void initTest() {
        ami = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmi() throws Exception {
        int databaseSizeBeforeCreate = amiRepository.findAll().size();

        // Create the Ami
        AmiDTO amiDTO = amiMapper.toDto(ami);
        restAmiMockMvc.perform(post("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isCreated());

        // Validate the Ami in the database
        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeCreate + 1);
        Ami testAmi = amiList.get(amiList.size() - 1);
        assertThat(testAmi.getInterested()).isEqualTo(DEFAULT_INTERESTED);
        assertThat(testAmi.getFriend()).isEqualTo(DEFAULT_FRIEND);
        assertThat(testAmi.isDemande()).isEqualTo(DEFAULT_DEMANDE);
        assertThat(testAmi.getDateAmis()).isEqualTo(DEFAULT_DATE_AMIS);
        assertThat(testAmi.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAmi.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAmi.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAmi.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createAmiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amiRepository.findAll().size();

        // Create the Ami with an existing ID
        ami.setId(1L);
        AmiDTO amiDTO = amiMapper.toDto(ami);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmiMockMvc.perform(post("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ami in the database
        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInterestedIsRequired() throws Exception {
        int databaseSizeBeforeTest = amiRepository.findAll().size();
        // set the field null
        ami.setInterested(null);

        // Create the Ami, which fails.
        AmiDTO amiDTO = amiMapper.toDto(ami);

        restAmiMockMvc.perform(post("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isBadRequest());

        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFriendIsRequired() throws Exception {
        int databaseSizeBeforeTest = amiRepository.findAll().size();
        // set the field null
        ami.setFriend(null);

        // Create the Ami, which fails.
        AmiDTO amiDTO = amiMapper.toDto(ami);

        restAmiMockMvc.perform(post("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isBadRequest());

        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAmis() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList
        restAmiMockMvc.perform(get("/api/amis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ami.getId().intValue())))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED.intValue())))
            .andExpect(jsonPath("$.[*].friend").value(hasItem(DEFAULT_FRIEND.intValue())))
            .andExpect(jsonPath("$.[*].demande").value(hasItem(DEFAULT_DEMANDE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAmis").value(hasItem(DEFAULT_DATE_AMIS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAmi() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get the ami
        restAmiMockMvc.perform(get("/api/amis/{id}", ami.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ami.getId().intValue()))
            .andExpect(jsonPath("$.interested").value(DEFAULT_INTERESTED.intValue()))
            .andExpect(jsonPath("$.friend").value(DEFAULT_FRIEND.intValue()))
            .andExpect(jsonPath("$.demande").value(DEFAULT_DEMANDE.booleanValue()))
            .andExpect(jsonPath("$.dateAmis").value(DEFAULT_DATE_AMIS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllAmisByInterestedIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where interested equals to DEFAULT_INTERESTED
        defaultAmiShouldBeFound("interested.equals=" + DEFAULT_INTERESTED);

        // Get all the amiList where interested equals to UPDATED_INTERESTED
        defaultAmiShouldNotBeFound("interested.equals=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllAmisByInterestedIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where interested in DEFAULT_INTERESTED or UPDATED_INTERESTED
        defaultAmiShouldBeFound("interested.in=" + DEFAULT_INTERESTED + "," + UPDATED_INTERESTED);

        // Get all the amiList where interested equals to UPDATED_INTERESTED
        defaultAmiShouldNotBeFound("interested.in=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllAmisByInterestedIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where interested is not null
        defaultAmiShouldBeFound("interested.specified=true");

        // Get all the amiList where interested is null
        defaultAmiShouldNotBeFound("interested.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByInterestedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where interested greater than or equals to DEFAULT_INTERESTED
        defaultAmiShouldBeFound("interested.greaterOrEqualThan=" + DEFAULT_INTERESTED);

        // Get all the amiList where interested greater than or equals to UPDATED_INTERESTED
        defaultAmiShouldNotBeFound("interested.greaterOrEqualThan=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllAmisByInterestedIsLessThanSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where interested less than or equals to DEFAULT_INTERESTED
        defaultAmiShouldNotBeFound("interested.lessThan=" + DEFAULT_INTERESTED);

        // Get all the amiList where interested less than or equals to UPDATED_INTERESTED
        defaultAmiShouldBeFound("interested.lessThan=" + UPDATED_INTERESTED);
    }


    @Test
    @Transactional
    public void getAllAmisByFriendIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where friend equals to DEFAULT_FRIEND
        defaultAmiShouldBeFound("friend.equals=" + DEFAULT_FRIEND);

        // Get all the amiList where friend equals to UPDATED_FRIEND
        defaultAmiShouldNotBeFound("friend.equals=" + UPDATED_FRIEND);
    }

    @Test
    @Transactional
    public void getAllAmisByFriendIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where friend in DEFAULT_FRIEND or UPDATED_FRIEND
        defaultAmiShouldBeFound("friend.in=" + DEFAULT_FRIEND + "," + UPDATED_FRIEND);

        // Get all the amiList where friend equals to UPDATED_FRIEND
        defaultAmiShouldNotBeFound("friend.in=" + UPDATED_FRIEND);
    }

    @Test
    @Transactional
    public void getAllAmisByFriendIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where friend is not null
        defaultAmiShouldBeFound("friend.specified=true");

        // Get all the amiList where friend is null
        defaultAmiShouldNotBeFound("friend.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByFriendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where friend greater than or equals to DEFAULT_FRIEND
        defaultAmiShouldBeFound("friend.greaterOrEqualThan=" + DEFAULT_FRIEND);

        // Get all the amiList where friend greater than or equals to UPDATED_FRIEND
        defaultAmiShouldNotBeFound("friend.greaterOrEqualThan=" + UPDATED_FRIEND);
    }

    @Test
    @Transactional
    public void getAllAmisByFriendIsLessThanSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where friend less than or equals to DEFAULT_FRIEND
        defaultAmiShouldNotBeFound("friend.lessThan=" + DEFAULT_FRIEND);

        // Get all the amiList where friend less than or equals to UPDATED_FRIEND
        defaultAmiShouldBeFound("friend.lessThan=" + UPDATED_FRIEND);
    }


    @Test
    @Transactional
    public void getAllAmisByDemandeIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where demande equals to DEFAULT_DEMANDE
        defaultAmiShouldBeFound("demande.equals=" + DEFAULT_DEMANDE);

        // Get all the amiList where demande equals to UPDATED_DEMANDE
        defaultAmiShouldNotBeFound("demande.equals=" + UPDATED_DEMANDE);
    }

    @Test
    @Transactional
    public void getAllAmisByDemandeIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where demande in DEFAULT_DEMANDE or UPDATED_DEMANDE
        defaultAmiShouldBeFound("demande.in=" + DEFAULT_DEMANDE + "," + UPDATED_DEMANDE);

        // Get all the amiList where demande equals to UPDATED_DEMANDE
        defaultAmiShouldNotBeFound("demande.in=" + UPDATED_DEMANDE);
    }

    @Test
    @Transactional
    public void getAllAmisByDemandeIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where demande is not null
        defaultAmiShouldBeFound("demande.specified=true");

        // Get all the amiList where demande is null
        defaultAmiShouldNotBeFound("demande.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByDateAmisIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where dateAmis equals to DEFAULT_DATE_AMIS
        defaultAmiShouldBeFound("dateAmis.equals=" + DEFAULT_DATE_AMIS);

        // Get all the amiList where dateAmis equals to UPDATED_DATE_AMIS
        defaultAmiShouldNotBeFound("dateAmis.equals=" + UPDATED_DATE_AMIS);
    }

    @Test
    @Transactional
    public void getAllAmisByDateAmisIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where dateAmis in DEFAULT_DATE_AMIS or UPDATED_DATE_AMIS
        defaultAmiShouldBeFound("dateAmis.in=" + DEFAULT_DATE_AMIS + "," + UPDATED_DATE_AMIS);

        // Get all the amiList where dateAmis equals to UPDATED_DATE_AMIS
        defaultAmiShouldNotBeFound("dateAmis.in=" + UPDATED_DATE_AMIS);
    }

    @Test
    @Transactional
    public void getAllAmisByDateAmisIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where dateAmis is not null
        defaultAmiShouldBeFound("dateAmis.specified=true");

        // Get all the amiList where dateAmis is null
        defaultAmiShouldNotBeFound("dateAmis.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdBy equals to DEFAULT_CREATED_BY
        defaultAmiShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the amiList where createdBy equals to UPDATED_CREATED_BY
        defaultAmiShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAmiShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the amiList where createdBy equals to UPDATED_CREATED_BY
        defaultAmiShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdBy is not null
        defaultAmiShouldBeFound("createdBy.specified=true");

        // Get all the amiList where createdBy is null
        defaultAmiShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdBy greater than or equals to DEFAULT_CREATED_BY
        defaultAmiShouldBeFound("createdBy.greaterOrEqualThan=" + DEFAULT_CREATED_BY);

        // Get all the amiList where createdBy greater than or equals to UPDATED_CREATED_BY
        defaultAmiShouldNotBeFound("createdBy.greaterOrEqualThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdBy less than or equals to DEFAULT_CREATED_BY
        defaultAmiShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the amiList where createdBy less than or equals to UPDATED_CREATED_BY
        defaultAmiShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllAmisByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdDate equals to DEFAULT_CREATED_DATE
        defaultAmiShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the amiList where createdDate equals to UPDATED_CREATED_DATE
        defaultAmiShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultAmiShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the amiList where createdDate equals to UPDATED_CREATED_DATE
        defaultAmiShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAmisByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where createdDate is not null
        defaultAmiShouldBeFound("createdDate.specified=true");

        // Get all the amiList where createdDate is null
        defaultAmiShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAmiShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amiList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAmiShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAmiShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the amiList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAmiShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedBy is not null
        defaultAmiShouldBeFound("lastModifiedBy.specified=true");

        // Get all the amiList where lastModifiedBy is null
        defaultAmiShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedBy greater than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultAmiShouldBeFound("lastModifiedBy.greaterOrEqualThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amiList where lastModifiedBy greater than or equals to UPDATED_LAST_MODIFIED_BY
        defaultAmiShouldNotBeFound("lastModifiedBy.greaterOrEqualThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedBy less than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultAmiShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amiList where lastModifiedBy less than or equals to UPDATED_LAST_MODIFIED_BY
        defaultAmiShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllAmisByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAmiShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the amiList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAmiShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultAmiShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the amiList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAmiShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllAmisByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        // Get all the amiList where lastModifiedDate is not null
        defaultAmiShouldBeFound("lastModifiedDate.specified=true");

        // Get all the amiList where lastModifiedDate is null
        defaultAmiShouldNotBeFound("lastModifiedDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAmiShouldBeFound(String filter) throws Exception {
        restAmiMockMvc.perform(get("/api/amis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ami.getId().intValue())))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED.intValue())))
            .andExpect(jsonPath("$.[*].friend").value(hasItem(DEFAULT_FRIEND.intValue())))
            .andExpect(jsonPath("$.[*].demande").value(hasItem(DEFAULT_DEMANDE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAmis").value(hasItem(DEFAULT_DATE_AMIS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAmiMockMvc.perform(get("/api/amis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAmiShouldNotBeFound(String filter) throws Exception {
        restAmiMockMvc.perform(get("/api/amis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmiMockMvc.perform(get("/api/amis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAmi() throws Exception {
        // Get the ami
        restAmiMockMvc.perform(get("/api/amis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmi() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        int databaseSizeBeforeUpdate = amiRepository.findAll().size();

        // Update the ami
        Ami updatedAmi = amiRepository.findById(ami.getId()).get();
        // Disconnect from session so that the updates on updatedAmi are not directly saved in db
        em.detach(updatedAmi);
        updatedAmi
            .interested(UPDATED_INTERESTED)
            .friend(UPDATED_FRIEND)
            .demande(UPDATED_DEMANDE)
            .dateAmis(UPDATED_DATE_AMIS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        AmiDTO amiDTO = amiMapper.toDto(updatedAmi);

        restAmiMockMvc.perform(put("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isOk());

        // Validate the Ami in the database
        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeUpdate);
        Ami testAmi = amiList.get(amiList.size() - 1);
        assertThat(testAmi.getInterested()).isEqualTo(UPDATED_INTERESTED);
        assertThat(testAmi.getFriend()).isEqualTo(UPDATED_FRIEND);
        assertThat(testAmi.isDemande()).isEqualTo(UPDATED_DEMANDE);
        assertThat(testAmi.getDateAmis()).isEqualTo(UPDATED_DATE_AMIS);
        assertThat(testAmi.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAmi.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAmi.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAmi.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAmi() throws Exception {
        int databaseSizeBeforeUpdate = amiRepository.findAll().size();

        // Create the Ami
        AmiDTO amiDTO = amiMapper.toDto(ami);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmiMockMvc.perform(put("/api/amis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ami in the database
        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAmi() throws Exception {
        // Initialize the database
        amiRepository.saveAndFlush(ami);

        int databaseSizeBeforeDelete = amiRepository.findAll().size();

        // Get the ami
        restAmiMockMvc.perform(delete("/api/amis/{id}", ami.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ami> amiList = amiRepository.findAll();
        assertThat(amiList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ami.class);
        Ami ami1 = new Ami();
        ami1.setId(1L);
        Ami ami2 = new Ami();
        ami2.setId(ami1.getId());
        assertThat(ami1).isEqualTo(ami2);
        ami2.setId(2L);
        assertThat(ami1).isNotEqualTo(ami2);
        ami1.setId(null);
        assertThat(ami1).isNotEqualTo(ami2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmiDTO.class);
        AmiDTO amiDTO1 = new AmiDTO();
        amiDTO1.setId(1L);
        AmiDTO amiDTO2 = new AmiDTO();
        assertThat(amiDTO1).isNotEqualTo(amiDTO2);
        amiDTO2.setId(amiDTO1.getId());
        assertThat(amiDTO1).isEqualTo(amiDTO2);
        amiDTO2.setId(2L);
        assertThat(amiDTO1).isNotEqualTo(amiDTO2);
        amiDTO1.setId(null);
        assertThat(amiDTO1).isNotEqualTo(amiDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(amiMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(amiMapper.fromId(null)).isNull();
    }
}
