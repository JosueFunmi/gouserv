package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Profession;
import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.repository.ProfessionRepository;
import fredy.josue.dougbeservice.service.ProfessionService;
import fredy.josue.dougbeservice.service.dto.ProfessionDTO;
import fredy.josue.dougbeservice.service.mapper.ProfessionMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.ProfessionCriteria;
import fredy.josue.dougbeservice.service.ProfessionQueryService;

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


import static fredy.josue.dougbeservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfessionResource REST controller.
 *
 * @see ProfessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class ProfessionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private ProfessionService professionService;

    @Autowired
    private ProfessionQueryService professionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfessionMockMvc;

    private Profession profession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfessionResource professionResource = new ProfessionResource(professionService, professionQueryService);
        this.restProfessionMockMvc = MockMvcBuilders.standaloneSetup(professionResource)
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
    public static Profession createEntity(EntityManager em) {
        Profession profession = new Profession()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return profession;
    }

    @Before
    public void initTest() {
        profession = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfession() throws Exception {
        int databaseSizeBeforeCreate = professionRepository.findAll().size();

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);
        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isCreated());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate + 1);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProfession.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createProfessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = professionRepository.findAll().size();

        // Create the Profession with an existing ID
        profession.setId(1L);
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setLibelle(null);

        // Create the Profession, which fails.
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfessions() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList
        restProfessionMockMvc.perform(get("/api/professions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
    
    @Test
    @Transactional
    public void getProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get the profession
        restProfessionMockMvc.perform(get("/api/professions/{id}", profession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profession.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getAllProfessionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where code equals to DEFAULT_CODE
        defaultProfessionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the professionList where code equals to UPDATED_CODE
        defaultProfessionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProfessionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProfessionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the professionList where code equals to UPDATED_CODE
        defaultProfessionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProfessionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where code is not null
        defaultProfessionShouldBeFound("code.specified=true");

        // Get all the professionList where code is null
        defaultProfessionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessionsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where libelle equals to DEFAULT_LIBELLE
        defaultProfessionShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the professionList where libelle equals to UPDATED_LIBELLE
        defaultProfessionShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProfessionsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultProfessionShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the professionList where libelle equals to UPDATED_LIBELLE
        defaultProfessionShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllProfessionsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList where libelle is not null
        defaultProfessionShouldBeFound("libelle.specified=true");

        // Get all the professionList where libelle is null
        defaultProfessionShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessionsByCostumUserIsEqualToSomething() throws Exception {
        // Initialize the database
        CostumUser costumUser = CostumUserResourceIntTest.createEntity(em);
        em.persist(costumUser);
        em.flush();
        profession.setCostumUser(costumUser);
        professionRepository.saveAndFlush(profession);
        Long costumUserId = costumUser.getId();

        // Get all the professionList where costumUser equals to costumUserId
        defaultProfessionShouldBeFound("costumUserId.equals=" + costumUserId);

        // Get all the professionList where costumUser equals to costumUserId + 1
        defaultProfessionShouldNotBeFound("costumUserId.equals=" + (costumUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfessionShouldBeFound(String filter) throws Exception {
        restProfessionMockMvc.perform(get("/api/professions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));

        // Check, that the count call also returns 1
        restProfessionMockMvc.perform(get("/api/professions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfessionShouldNotBeFound(String filter) throws Exception {
        restProfessionMockMvc.perform(get("/api/professions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionMockMvc.perform(get("/api/professions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfession() throws Exception {
        // Get the profession
        restProfessionMockMvc.perform(get("/api/professions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Update the profession
        Profession updatedProfession = professionRepository.findById(profession.getId()).get();
        // Disconnect from session so that the updates on updatedProfession are not directly saved in db
        em.detach(updatedProfession);
        updatedProfession
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        ProfessionDTO professionDTO = professionMapper.toDto(updatedProfession);

        restProfessionMockMvc.perform(put("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isOk());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProfession.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Create the Profession
        ProfessionDTO professionDTO = professionMapper.toDto(profession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionMockMvc.perform(put("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        int databaseSizeBeforeDelete = professionRepository.findAll().size();

        // Get the profession
        restProfessionMockMvc.perform(delete("/api/professions/{id}", profession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profession.class);
        Profession profession1 = new Profession();
        profession1.setId(1L);
        Profession profession2 = new Profession();
        profession2.setId(profession1.getId());
        assertThat(profession1).isEqualTo(profession2);
        profession2.setId(2L);
        assertThat(profession1).isNotEqualTo(profession2);
        profession1.setId(null);
        assertThat(profession1).isNotEqualTo(profession2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionDTO.class);
        ProfessionDTO professionDTO1 = new ProfessionDTO();
        professionDTO1.setId(1L);
        ProfessionDTO professionDTO2 = new ProfessionDTO();
        assertThat(professionDTO1).isNotEqualTo(professionDTO2);
        professionDTO2.setId(professionDTO1.getId());
        assertThat(professionDTO1).isEqualTo(professionDTO2);
        professionDTO2.setId(2L);
        assertThat(professionDTO1).isNotEqualTo(professionDTO2);
        professionDTO1.setId(null);
        assertThat(professionDTO1).isNotEqualTo(professionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(professionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(professionMapper.fromId(null)).isNull();
    }
}
