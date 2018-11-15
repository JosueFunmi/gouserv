package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Preference;
import fredy.josue.dougbeservice.repository.PreferenceRepository;
import fredy.josue.dougbeservice.service.PreferenceService;
import fredy.josue.dougbeservice.service.dto.PreferenceDTO;
import fredy.josue.dougbeservice.service.mapper.PreferenceMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.PreferenceCriteria;
import fredy.josue.dougbeservice.service.PreferenceQueryService;

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

import fredy.josue.dougbeservice.domain.enumeration.TypeRencontre;
/**
 * Test class for the PreferenceResource REST controller.
 *
 * @see PreferenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class PreferenceResourceIntTest {

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_TAILLE = "AAAAAAAAAA";
    private static final String UPDATED_TAILLE = "BBBBBBBBBB";

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_TEINT = "AAAAAAAAAA";
    private static final String UPDATED_TEINT = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TypeRencontre DEFAULT_TYPE_RENCONTRE = TypeRencontre.SERIEUX;
    private static final TypeRencontre UPDATED_TYPE_RENCONTRE = TypeRencontre.LIBERTINE;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private PreferenceQueryService preferenceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPreferenceMockMvc;

    private Preference preference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreferenceResource preferenceResource = new PreferenceResource(preferenceService, preferenceQueryService);
        this.restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
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
    public static Preference createEntity(EntityManager em) {
        Preference preference = new Preference()
            .sexe(DEFAULT_SEXE)
            .taille(DEFAULT_TAILLE)
            .age(DEFAULT_AGE)
            .pays(DEFAULT_PAYS)
            .teint(DEFAULT_TEINT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .typeRencontre(DEFAULT_TYPE_RENCONTRE);
        return preference;
    }

    @Before
    public void initTest() {
        preference = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreference() throws Exception {
        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate + 1);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPreference.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testPreference.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPreference.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testPreference.getTeint()).isEqualTo(DEFAULT_TEINT);
        assertThat(testPreference.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPreference.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPreference.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPreference.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPreference.getTypeRencontre()).isEqualTo(DEFAULT_TYPE_RENCONTRE);
    }

    @Test
    @Transactional
    public void createPreferenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference with an existing ID
        preference.setId(1L);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS.toString())))
            .andExpect(jsonPath("$.[*].teint").value(hasItem(DEFAULT_TEINT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].typeRencontre").value(hasItem(DEFAULT_TYPE_RENCONTRE.toString())));
    }
    
    @Test
    @Transactional
    public void getPreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", preference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preference.getId().intValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.toString()))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS.toString()))
            .andExpect(jsonPath("$.teint").value(DEFAULT_TEINT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.typeRencontre").value(DEFAULT_TYPE_RENCONTRE.toString()));
    }

    @Test
    @Transactional
    public void getAllPreferencesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where sexe equals to DEFAULT_SEXE
        defaultPreferenceShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the preferenceList where sexe equals to UPDATED_SEXE
        defaultPreferenceShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPreferencesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultPreferenceShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the preferenceList where sexe equals to UPDATED_SEXE
        defaultPreferenceShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPreferencesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where sexe is not null
        defaultPreferenceShouldBeFound("sexe.specified=true");

        // Get all the preferenceList where sexe is null
        defaultPreferenceShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByTailleIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where taille equals to DEFAULT_TAILLE
        defaultPreferenceShouldBeFound("taille.equals=" + DEFAULT_TAILLE);

        // Get all the preferenceList where taille equals to UPDATED_TAILLE
        defaultPreferenceShouldNotBeFound("taille.equals=" + UPDATED_TAILLE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTailleIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where taille in DEFAULT_TAILLE or UPDATED_TAILLE
        defaultPreferenceShouldBeFound("taille.in=" + DEFAULT_TAILLE + "," + UPDATED_TAILLE);

        // Get all the preferenceList where taille equals to UPDATED_TAILLE
        defaultPreferenceShouldNotBeFound("taille.in=" + UPDATED_TAILLE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTailleIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where taille is not null
        defaultPreferenceShouldBeFound("taille.specified=true");

        // Get all the preferenceList where taille is null
        defaultPreferenceShouldNotBeFound("taille.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where age equals to DEFAULT_AGE
        defaultPreferenceShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the preferenceList where age equals to UPDATED_AGE
        defaultPreferenceShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where age in DEFAULT_AGE or UPDATED_AGE
        defaultPreferenceShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the preferenceList where age equals to UPDATED_AGE
        defaultPreferenceShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where age is not null
        defaultPreferenceShouldBeFound("age.specified=true");

        // Get all the preferenceList where age is null
        defaultPreferenceShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where pays equals to DEFAULT_PAYS
        defaultPreferenceShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the preferenceList where pays equals to UPDATED_PAYS
        defaultPreferenceShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllPreferencesByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultPreferenceShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the preferenceList where pays equals to UPDATED_PAYS
        defaultPreferenceShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllPreferencesByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where pays is not null
        defaultPreferenceShouldBeFound("pays.specified=true");

        // Get all the preferenceList where pays is null
        defaultPreferenceShouldNotBeFound("pays.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByTeintIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where teint equals to DEFAULT_TEINT
        defaultPreferenceShouldBeFound("teint.equals=" + DEFAULT_TEINT);

        // Get all the preferenceList where teint equals to UPDATED_TEINT
        defaultPreferenceShouldNotBeFound("teint.equals=" + UPDATED_TEINT);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTeintIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where teint in DEFAULT_TEINT or UPDATED_TEINT
        defaultPreferenceShouldBeFound("teint.in=" + DEFAULT_TEINT + "," + UPDATED_TEINT);

        // Get all the preferenceList where teint equals to UPDATED_TEINT
        defaultPreferenceShouldNotBeFound("teint.in=" + UPDATED_TEINT);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTeintIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where teint is not null
        defaultPreferenceShouldBeFound("teint.specified=true");

        // Get all the preferenceList where teint is null
        defaultPreferenceShouldNotBeFound("teint.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdBy equals to DEFAULT_CREATED_BY
        defaultPreferenceShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the preferenceList where createdBy equals to UPDATED_CREATED_BY
        defaultPreferenceShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultPreferenceShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the preferenceList where createdBy equals to UPDATED_CREATED_BY
        defaultPreferenceShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdBy is not null
        defaultPreferenceShouldBeFound("createdBy.specified=true");

        // Get all the preferenceList where createdBy is null
        defaultPreferenceShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdBy greater than or equals to DEFAULT_CREATED_BY
        defaultPreferenceShouldBeFound("createdBy.greaterOrEqualThan=" + DEFAULT_CREATED_BY);

        // Get all the preferenceList where createdBy greater than or equals to UPDATED_CREATED_BY
        defaultPreferenceShouldNotBeFound("createdBy.greaterOrEqualThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdBy less than or equals to DEFAULT_CREATED_BY
        defaultPreferenceShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the preferenceList where createdBy less than or equals to UPDATED_CREATED_BY
        defaultPreferenceShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllPreferencesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdDate equals to DEFAULT_CREATED_DATE
        defaultPreferenceShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the preferenceList where createdDate equals to UPDATED_CREATED_DATE
        defaultPreferenceShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultPreferenceShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the preferenceList where createdDate equals to UPDATED_CREATED_DATE
        defaultPreferenceShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where createdDate is not null
        defaultPreferenceShouldBeFound("createdDate.specified=true");

        // Get all the preferenceList where createdDate is null
        defaultPreferenceShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPreferenceShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the preferenceList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPreferenceShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPreferenceShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the preferenceList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPreferenceShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedBy is not null
        defaultPreferenceShouldBeFound("lastModifiedBy.specified=true");

        // Get all the preferenceList where lastModifiedBy is null
        defaultPreferenceShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedBy greater than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultPreferenceShouldBeFound("lastModifiedBy.greaterOrEqualThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the preferenceList where lastModifiedBy greater than or equals to UPDATED_LAST_MODIFIED_BY
        defaultPreferenceShouldNotBeFound("lastModifiedBy.greaterOrEqualThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedBy less than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultPreferenceShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the preferenceList where lastModifiedBy less than or equals to UPDATED_LAST_MODIFIED_BY
        defaultPreferenceShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPreferenceShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the preferenceList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPreferenceShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultPreferenceShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the preferenceList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPreferenceShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where lastModifiedDate is not null
        defaultPreferenceShouldBeFound("lastModifiedDate.specified=true");

        // Get all the preferenceList where lastModifiedDate is null
        defaultPreferenceShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByTypeRencontreIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where typeRencontre equals to DEFAULT_TYPE_RENCONTRE
        defaultPreferenceShouldBeFound("typeRencontre.equals=" + DEFAULT_TYPE_RENCONTRE);

        // Get all the preferenceList where typeRencontre equals to UPDATED_TYPE_RENCONTRE
        defaultPreferenceShouldNotBeFound("typeRencontre.equals=" + UPDATED_TYPE_RENCONTRE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTypeRencontreIsInShouldWork() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where typeRencontre in DEFAULT_TYPE_RENCONTRE or UPDATED_TYPE_RENCONTRE
        defaultPreferenceShouldBeFound("typeRencontre.in=" + DEFAULT_TYPE_RENCONTRE + "," + UPDATED_TYPE_RENCONTRE);

        // Get all the preferenceList where typeRencontre equals to UPDATED_TYPE_RENCONTRE
        defaultPreferenceShouldNotBeFound("typeRencontre.in=" + UPDATED_TYPE_RENCONTRE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByTypeRencontreIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList where typeRencontre is not null
        defaultPreferenceShouldBeFound("typeRencontre.specified=true");

        // Get all the preferenceList where typeRencontre is null
        defaultPreferenceShouldNotBeFound("typeRencontre.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPreferenceShouldBeFound(String filter) throws Exception {
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS.toString())))
            .andExpect(jsonPath("$.[*].teint").value(hasItem(DEFAULT_TEINT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].typeRencontre").value(hasItem(DEFAULT_TYPE_RENCONTRE.toString())));

        // Check, that the count call also returns 1
        restPreferenceMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPreferenceShouldNotBeFound(String filter) throws Exception {
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPreferenceMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPreference() throws Exception {
        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Update the preference
        Preference updatedPreference = preferenceRepository.findById(preference.getId()).get();
        // Disconnect from session so that the updates on updatedPreference are not directly saved in db
        em.detach(updatedPreference);
        updatedPreference
            .sexe(UPDATED_SEXE)
            .taille(UPDATED_TAILLE)
            .age(UPDATED_AGE)
            .pays(UPDATED_PAYS)
            .teint(UPDATED_TEINT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .typeRencontre(UPDATED_TYPE_RENCONTRE);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(updatedPreference);

        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPreference.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testPreference.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPreference.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testPreference.getTeint()).isEqualTo(UPDATED_TEINT);
        assertThat(testPreference.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPreference.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPreference.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPreference.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPreference.getTypeRencontre()).isEqualTo(UPDATED_TYPE_RENCONTRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPreference() throws Exception {
        int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        int databaseSizeBeforeDelete = preferenceRepository.findAll().size();

        // Get the preference
        restPreferenceMockMvc.perform(delete("/api/preferences/{id}", preference.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Preference.class);
        Preference preference1 = new Preference();
        preference1.setId(1L);
        Preference preference2 = new Preference();
        preference2.setId(preference1.getId());
        assertThat(preference1).isEqualTo(preference2);
        preference2.setId(2L);
        assertThat(preference1).isNotEqualTo(preference2);
        preference1.setId(null);
        assertThat(preference1).isNotEqualTo(preference2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferenceDTO.class);
        PreferenceDTO preferenceDTO1 = new PreferenceDTO();
        preferenceDTO1.setId(1L);
        PreferenceDTO preferenceDTO2 = new PreferenceDTO();
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO2.setId(preferenceDTO1.getId());
        assertThat(preferenceDTO1).isEqualTo(preferenceDTO2);
        preferenceDTO2.setId(2L);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO1.setId(null);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preferenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preferenceMapper.fromId(null)).isNull();
    }
}
