package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Pays;
import fredy.josue.dougbeservice.domain.Ville;
import fredy.josue.dougbeservice.repository.PaysRepository;
import fredy.josue.dougbeservice.service.PaysService;
import fredy.josue.dougbeservice.service.dto.PaysDTO;
import fredy.josue.dougbeservice.service.mapper.PaysMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.PaysCriteria;
import fredy.josue.dougbeservice.service.PaysQueryService;

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
 * Test class for the PaysResource REST controller.
 *
 * @see PaysResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class PaysResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDICE = 1;
    private static final Integer UPDATED_INDICE = 2;

    @Autowired
    private PaysRepository paysRepository;

    @Autowired
    private PaysMapper paysMapper;

    @Autowired
    private PaysService paysService;

    @Autowired
    private PaysQueryService paysQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaysMockMvc;

    private Pays pays;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaysResource paysResource = new PaysResource(paysService, paysQueryService);
        this.restPaysMockMvc = MockMvcBuilders.standaloneSetup(paysResource)
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
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .indice(DEFAULT_INDICE);
        return pays;
    }

    @Before
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    public void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);
        restPaysMockMvc.perform(post("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPays.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPays.getIndice()).isEqualTo(DEFAULT_INDICE);
    }

    @Test
    @Transactional
    public void createPaysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays with an existing ID
        pays.setId(1L);
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaysMockMvc.perform(post("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setLibelle(null);

        // Create the Pays, which fails.
        PaysDTO paysDTO = paysMapper.toDto(pays);

        restPaysMockMvc.perform(post("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isBadRequest());

        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].indice").value(hasItem(DEFAULT_INDICE)));
    }
    
    @Test
    @Transactional
    public void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.indice").value(DEFAULT_INDICE));
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code equals to DEFAULT_CODE
        defaultPaysShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the paysList where code equals to UPDATED_CODE
        defaultPaysShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPaysShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the paysList where code equals to UPDATED_CODE
        defaultPaysShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code is not null
        defaultPaysShouldBeFound("code.specified=true");

        // Get all the paysList where code is null
        defaultPaysShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaysByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where libelle equals to DEFAULT_LIBELLE
        defaultPaysShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the paysList where libelle equals to UPDATED_LIBELLE
        defaultPaysShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPaysByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultPaysShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the paysList where libelle equals to UPDATED_LIBELLE
        defaultPaysShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPaysByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where libelle is not null
        defaultPaysShouldBeFound("libelle.specified=true");

        // Get all the paysList where libelle is null
        defaultPaysShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaysByIndiceIsEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where indice equals to DEFAULT_INDICE
        defaultPaysShouldBeFound("indice.equals=" + DEFAULT_INDICE);

        // Get all the paysList where indice equals to UPDATED_INDICE
        defaultPaysShouldNotBeFound("indice.equals=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    public void getAllPaysByIndiceIsInShouldWork() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where indice in DEFAULT_INDICE or UPDATED_INDICE
        defaultPaysShouldBeFound("indice.in=" + DEFAULT_INDICE + "," + UPDATED_INDICE);

        // Get all the paysList where indice equals to UPDATED_INDICE
        defaultPaysShouldNotBeFound("indice.in=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    public void getAllPaysByIndiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where indice is not null
        defaultPaysShouldBeFound("indice.specified=true");

        // Get all the paysList where indice is null
        defaultPaysShouldNotBeFound("indice.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaysByIndiceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where indice greater than or equals to DEFAULT_INDICE
        defaultPaysShouldBeFound("indice.greaterOrEqualThan=" + DEFAULT_INDICE);

        // Get all the paysList where indice greater than or equals to UPDATED_INDICE
        defaultPaysShouldNotBeFound("indice.greaterOrEqualThan=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    public void getAllPaysByIndiceIsLessThanSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where indice less than or equals to DEFAULT_INDICE
        defaultPaysShouldNotBeFound("indice.lessThan=" + DEFAULT_INDICE);

        // Get all the paysList where indice less than or equals to UPDATED_INDICE
        defaultPaysShouldBeFound("indice.lessThan=" + UPDATED_INDICE);
    }


    @Test
    @Transactional
    public void getAllPaysByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        Ville ville = VilleResourceIntTest.createEntity(em);
        em.persist(ville);
        em.flush();
        pays.addVille(ville);
        paysRepository.saveAndFlush(pays);
        Long villeId = ville.getId();

        // Get all the paysList where ville equals to villeId
        defaultPaysShouldBeFound("villeId.equals=" + villeId);

        // Get all the paysList where ville equals to villeId + 1
        defaultPaysShouldNotBeFound("villeId.equals=" + (villeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPaysShouldBeFound(String filter) throws Exception {
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].indice").value(hasItem(DEFAULT_INDICE)));

        // Check, that the count call also returns 1
        restPaysMockMvc.perform(get("/api/pays/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPaysShouldNotBeFound(String filter) throws Exception {
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaysMockMvc.perform(get("/api/pays/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays
        Pays updatedPays = paysRepository.findById(pays.getId()).get();
        // Disconnect from session so that the updates on updatedPays are not directly saved in db
        em.detach(updatedPays);
        updatedPays
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .indice(UPDATED_INDICE);
        PaysDTO paysDTO = paysMapper.toDto(updatedPays);

        restPaysMockMvc.perform(put("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPays.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPays.getIndice()).isEqualTo(UPDATED_INDICE);
    }

    @Test
    @Transactional
    public void updateNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc.perform(put("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeDelete = paysRepository.findAll().size();

        // Get the pays
        restPaysMockMvc.perform(delete("/api/pays/{id}", pays.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pays.class);
        Pays pays1 = new Pays();
        pays1.setId(1L);
        Pays pays2 = new Pays();
        pays2.setId(pays1.getId());
        assertThat(pays1).isEqualTo(pays2);
        pays2.setId(2L);
        assertThat(pays1).isNotEqualTo(pays2);
        pays1.setId(null);
        assertThat(pays1).isNotEqualTo(pays2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaysDTO.class);
        PaysDTO paysDTO1 = new PaysDTO();
        paysDTO1.setId(1L);
        PaysDTO paysDTO2 = new PaysDTO();
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
        paysDTO2.setId(paysDTO1.getId());
        assertThat(paysDTO1).isEqualTo(paysDTO2);
        paysDTO2.setId(2L);
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
        paysDTO1.setId(null);
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paysMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paysMapper.fromId(null)).isNull();
    }
}
