package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Ville;
import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.domain.Pays;
import fredy.josue.dougbeservice.repository.VilleRepository;
import fredy.josue.dougbeservice.service.VilleService;
import fredy.josue.dougbeservice.service.dto.VilleDTO;
import fredy.josue.dougbeservice.service.mapper.VilleMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.VilleCriteria;
import fredy.josue.dougbeservice.service.VilleQueryService;

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
 * Test class for the VilleResource REST controller.
 *
 * @see VilleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class VilleResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private VilleMapper villeMapper;

    @Autowired
    private VilleService villeService;

    @Autowired
    private VilleQueryService villeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVilleMockMvc;

    private Ville ville;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VilleResource villeResource = new VilleResource(villeService, villeQueryService);
        this.restVilleMockMvc = MockMvcBuilders.standaloneSetup(villeResource)
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
    public static Ville createEntity(EntityManager em) {
        Ville ville = new Ville()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return ville;
    }

    @Before
    public void initTest() {
        ville = createEntity(em);
    }

    @Test
    @Transactional
    public void createVille() throws Exception {
        int databaseSizeBeforeCreate = villeRepository.findAll().size();

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);
        restVilleMockMvc.perform(post("/api/villes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isCreated());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeCreate + 1);
        Ville testVille = villeList.get(villeList.size() - 1);
        assertThat(testVille.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVille.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createVilleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = villeRepository.findAll().size();

        // Create the Ville with an existing ID
        ville.setId(1L);
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVilleMockMvc.perform(post("/api/villes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = villeRepository.findAll().size();
        // set the field null
        ville.setLibelle(null);

        // Create the Ville, which fails.
        VilleDTO villeDTO = villeMapper.toDto(ville);

        restVilleMockMvc.perform(post("/api/villes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVilles() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
    
    @Test
    @Transactional
    public void getVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", ville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ville.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getAllVillesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where code equals to DEFAULT_CODE
        defaultVilleShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the villeList where code equals to UPDATED_CODE
        defaultVilleShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllVillesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultVilleShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the villeList where code equals to UPDATED_CODE
        defaultVilleShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllVillesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where code is not null
        defaultVilleShouldBeFound("code.specified=true");

        // Get all the villeList where code is null
        defaultVilleShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllVillesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where libelle equals to DEFAULT_LIBELLE
        defaultVilleShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the villeList where libelle equals to UPDATED_LIBELLE
        defaultVilleShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllVillesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultVilleShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the villeList where libelle equals to UPDATED_LIBELLE
        defaultVilleShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllVillesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where libelle is not null
        defaultVilleShouldBeFound("libelle.specified=true");

        // Get all the villeList where libelle is null
        defaultVilleShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllVillesByCostumUserIsEqualToSomething() throws Exception {
        // Initialize the database
        CostumUser costumUser = CostumUserResourceIntTest.createEntity(em);
        em.persist(costumUser);
        em.flush();
        ville.addCostumUser(costumUser);
        villeRepository.saveAndFlush(ville);
        Long costumUserId = costumUser.getId();

        // Get all the villeList where costumUser equals to costumUserId
        defaultVilleShouldBeFound("costumUserId.equals=" + costumUserId);

        // Get all the villeList where costumUser equals to costumUserId + 1
        defaultVilleShouldNotBeFound("costumUserId.equals=" + (costumUserId + 1));
    }


    @Test
    @Transactional
    public void getAllVillesByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        Pays pays = PaysResourceIntTest.createEntity(em);
        em.persist(pays);
        em.flush();
        ville.setPays(pays);
        villeRepository.saveAndFlush(ville);
        Long paysId = pays.getId();

        // Get all the villeList where pays equals to paysId
        defaultVilleShouldBeFound("paysId.equals=" + paysId);

        // Get all the villeList where pays equals to paysId + 1
        defaultVilleShouldNotBeFound("paysId.equals=" + (paysId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVilleShouldBeFound(String filter) throws Exception {
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));

        // Check, that the count call also returns 1
        restVilleMockMvc.perform(get("/api/villes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVilleShouldNotBeFound(String filter) throws Exception {
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVilleMockMvc.perform(get("/api/villes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVille() throws Exception {
        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        int databaseSizeBeforeUpdate = villeRepository.findAll().size();

        // Update the ville
        Ville updatedVille = villeRepository.findById(ville.getId()).get();
        // Disconnect from session so that the updates on updatedVille are not directly saved in db
        em.detach(updatedVille);
        updatedVille
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        VilleDTO villeDTO = villeMapper.toDto(updatedVille);

        restVilleMockMvc.perform(put("/api/villes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isOk());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeUpdate);
        Ville testVille = villeList.get(villeList.size() - 1);
        assertThat(testVille.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVille.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingVille() throws Exception {
        int databaseSizeBeforeUpdate = villeRepository.findAll().size();

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVilleMockMvc.perform(put("/api/villes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        int databaseSizeBeforeDelete = villeRepository.findAll().size();

        // Get the ville
        restVilleMockMvc.perform(delete("/api/villes/{id}", ville.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ville.class);
        Ville ville1 = new Ville();
        ville1.setId(1L);
        Ville ville2 = new Ville();
        ville2.setId(ville1.getId());
        assertThat(ville1).isEqualTo(ville2);
        ville2.setId(2L);
        assertThat(ville1).isNotEqualTo(ville2);
        ville1.setId(null);
        assertThat(ville1).isNotEqualTo(ville2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VilleDTO.class);
        VilleDTO villeDTO1 = new VilleDTO();
        villeDTO1.setId(1L);
        VilleDTO villeDTO2 = new VilleDTO();
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
        villeDTO2.setId(villeDTO1.getId());
        assertThat(villeDTO1).isEqualTo(villeDTO2);
        villeDTO2.setId(2L);
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
        villeDTO1.setId(null);
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(villeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(villeMapper.fromId(null)).isNull();
    }
}
