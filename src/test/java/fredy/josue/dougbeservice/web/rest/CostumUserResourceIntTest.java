package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.domain.Publication;
import fredy.josue.dougbeservice.domain.Ville;
import fredy.josue.dougbeservice.domain.Profession;
import fredy.josue.dougbeservice.repository.CostumUserRepository;
import fredy.josue.dougbeservice.service.CostumUserService;
import fredy.josue.dougbeservice.service.dto.CostumUserDTO;
import fredy.josue.dougbeservice.service.mapper.CostumUserMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.CostumUserCriteria;
import fredy.josue.dougbeservice.service.CostumUserQueryService;

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

import fredy.josue.dougbeservice.domain.enumeration.Teint;
import fredy.josue.dougbeservice.domain.enumeration.Sexe;
/**
 * Test class for the CostumUserResource REST controller.
 *
 * @see CostumUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class CostumUserResourceIntTest {

    private static final Teint DEFAULT_TEINT = Teint.NOIR;
    private static final Teint UPDATED_TEINT = Teint.BRONZE;

    private static final Double DEFAULT_TAILLE = 1D;
    private static final Double UPDATED_TAILLE = 2D;

    private static final Instant DEFAULT_DATE_NAIS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAIS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    @Autowired
    private CostumUserRepository costumUserRepository;

    @Autowired
    private CostumUserMapper costumUserMapper;

    @Autowired
    private CostumUserService costumUserService;

    @Autowired
    private CostumUserQueryService costumUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCostumUserMockMvc;

    private CostumUser costumUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CostumUserResource costumUserResource = new CostumUserResource(costumUserService, costumUserQueryService);
        this.restCostumUserMockMvc = MockMvcBuilders.standaloneSetup(costumUserResource)
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
    public static CostumUser createEntity(EntityManager em) {
        CostumUser costumUser = new CostumUser()
            .teint(DEFAULT_TEINT)
            .taille(DEFAULT_TAILLE)
            .dateNais(DEFAULT_DATE_NAIS)
            .sexe(DEFAULT_SEXE);
        return costumUser;
    }

    @Before
    public void initTest() {
        costumUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostumUser() throws Exception {
        int databaseSizeBeforeCreate = costumUserRepository.findAll().size();

        // Create the CostumUser
        CostumUserDTO costumUserDTO = costumUserMapper.toDto(costumUser);
        restCostumUserMockMvc.perform(post("/api/costum-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CostumUser in the database
        List<CostumUser> costumUserList = costumUserRepository.findAll();
        assertThat(costumUserList).hasSize(databaseSizeBeforeCreate + 1);
        CostumUser testCostumUser = costumUserList.get(costumUserList.size() - 1);
        assertThat(testCostumUser.getTeint()).isEqualTo(DEFAULT_TEINT);
        assertThat(testCostumUser.getTaille()).isEqualTo(DEFAULT_TAILLE);
        assertThat(testCostumUser.getDateNais()).isEqualTo(DEFAULT_DATE_NAIS);
        assertThat(testCostumUser.getSexe()).isEqualTo(DEFAULT_SEXE);
    }

    @Test
    @Transactional
    public void createCostumUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costumUserRepository.findAll().size();

        // Create the CostumUser with an existing ID
        costumUser.setId(1L);
        CostumUserDTO costumUserDTO = costumUserMapper.toDto(costumUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostumUserMockMvc.perform(post("/api/costum-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CostumUser in the database
        List<CostumUser> costumUserList = costumUserRepository.findAll();
        assertThat(costumUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCostumUsers() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList
        restCostumUserMockMvc.perform(get("/api/costum-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costumUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teint").value(hasItem(DEFAULT_TEINT.toString())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateNais").value(hasItem(DEFAULT_DATE_NAIS.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }
    
    @Test
    @Transactional
    public void getCostumUser() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get the costumUser
        restCostumUserMockMvc.perform(get("/api/costum-users/{id}", costumUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costumUser.getId().intValue()))
            .andExpect(jsonPath("$.teint").value(DEFAULT_TEINT.toString()))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE.doubleValue()))
            .andExpect(jsonPath("$.dateNais").value(DEFAULT_DATE_NAIS.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTeintIsEqualToSomething() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where teint equals to DEFAULT_TEINT
        defaultCostumUserShouldBeFound("teint.equals=" + DEFAULT_TEINT);

        // Get all the costumUserList where teint equals to UPDATED_TEINT
        defaultCostumUserShouldNotBeFound("teint.equals=" + UPDATED_TEINT);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTeintIsInShouldWork() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where teint in DEFAULT_TEINT or UPDATED_TEINT
        defaultCostumUserShouldBeFound("teint.in=" + DEFAULT_TEINT + "," + UPDATED_TEINT);

        // Get all the costumUserList where teint equals to UPDATED_TEINT
        defaultCostumUserShouldNotBeFound("teint.in=" + UPDATED_TEINT);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTeintIsNullOrNotNull() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where teint is not null
        defaultCostumUserShouldBeFound("teint.specified=true");

        // Get all the costumUserList where teint is null
        defaultCostumUserShouldNotBeFound("teint.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTailleIsEqualToSomething() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where taille equals to DEFAULT_TAILLE
        defaultCostumUserShouldBeFound("taille.equals=" + DEFAULT_TAILLE);

        // Get all the costumUserList where taille equals to UPDATED_TAILLE
        defaultCostumUserShouldNotBeFound("taille.equals=" + UPDATED_TAILLE);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTailleIsInShouldWork() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where taille in DEFAULT_TAILLE or UPDATED_TAILLE
        defaultCostumUserShouldBeFound("taille.in=" + DEFAULT_TAILLE + "," + UPDATED_TAILLE);

        // Get all the costumUserList where taille equals to UPDATED_TAILLE
        defaultCostumUserShouldNotBeFound("taille.in=" + UPDATED_TAILLE);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByTailleIsNullOrNotNull() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where taille is not null
        defaultCostumUserShouldBeFound("taille.specified=true");

        // Get all the costumUserList where taille is null
        defaultCostumUserShouldNotBeFound("taille.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostumUsersByDateNaisIsEqualToSomething() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where dateNais equals to DEFAULT_DATE_NAIS
        defaultCostumUserShouldBeFound("dateNais.equals=" + DEFAULT_DATE_NAIS);

        // Get all the costumUserList where dateNais equals to UPDATED_DATE_NAIS
        defaultCostumUserShouldNotBeFound("dateNais.equals=" + UPDATED_DATE_NAIS);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByDateNaisIsInShouldWork() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where dateNais in DEFAULT_DATE_NAIS or UPDATED_DATE_NAIS
        defaultCostumUserShouldBeFound("dateNais.in=" + DEFAULT_DATE_NAIS + "," + UPDATED_DATE_NAIS);

        // Get all the costumUserList where dateNais equals to UPDATED_DATE_NAIS
        defaultCostumUserShouldNotBeFound("dateNais.in=" + UPDATED_DATE_NAIS);
    }

    @Test
    @Transactional
    public void getAllCostumUsersByDateNaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where dateNais is not null
        defaultCostumUserShouldBeFound("dateNais.specified=true");

        // Get all the costumUserList where dateNais is null
        defaultCostumUserShouldNotBeFound("dateNais.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostumUsersBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where sexe equals to DEFAULT_SEXE
        defaultCostumUserShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the costumUserList where sexe equals to UPDATED_SEXE
        defaultCostumUserShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCostumUsersBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultCostumUserShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the costumUserList where sexe equals to UPDATED_SEXE
        defaultCostumUserShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCostumUsersBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        // Get all the costumUserList where sexe is not null
        defaultCostumUserShouldBeFound("sexe.specified=true");

        // Get all the costumUserList where sexe is null
        defaultCostumUserShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostumUsersByPublicationIsEqualToSomething() throws Exception {
        // Initialize the database
        Publication publication = PublicationResourceIntTest.createEntity(em);
        em.persist(publication);
        em.flush();
        costumUser.addPublication(publication);
        costumUserRepository.saveAndFlush(costumUser);
        Long publicationId = publication.getId();

        // Get all the costumUserList where publication equals to publicationId
        defaultCostumUserShouldBeFound("publicationId.equals=" + publicationId);

        // Get all the costumUserList where publication equals to publicationId + 1
        defaultCostumUserShouldNotBeFound("publicationId.equals=" + (publicationId + 1));
    }


    @Test
    @Transactional
    public void getAllCostumUsersByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        Ville ville = VilleResourceIntTest.createEntity(em);
        em.persist(ville);
        em.flush();
        costumUser.setVille(ville);
        costumUserRepository.saveAndFlush(costumUser);
        Long villeId = ville.getId();

        // Get all the costumUserList where ville equals to villeId
        defaultCostumUserShouldBeFound("villeId.equals=" + villeId);

        // Get all the costumUserList where ville equals to villeId + 1
        defaultCostumUserShouldNotBeFound("villeId.equals=" + (villeId + 1));
    }


    @Test
    @Transactional
    public void getAllCostumUsersByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        Profession profession = ProfessionResourceIntTest.createEntity(em);
        em.persist(profession);
        em.flush();
        costumUser.addProfession(profession);
        costumUserRepository.saveAndFlush(costumUser);
        Long professionId = profession.getId();

        // Get all the costumUserList where profession equals to professionId
        defaultCostumUserShouldBeFound("professionId.equals=" + professionId);

        // Get all the costumUserList where profession equals to professionId + 1
        defaultCostumUserShouldNotBeFound("professionId.equals=" + (professionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCostumUserShouldBeFound(String filter) throws Exception {
        restCostumUserMockMvc.perform(get("/api/costum-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costumUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teint").value(hasItem(DEFAULT_TEINT.toString())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateNais").value(hasItem(DEFAULT_DATE_NAIS.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));

        // Check, that the count call also returns 1
        restCostumUserMockMvc.perform(get("/api/costum-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCostumUserShouldNotBeFound(String filter) throws Exception {
        restCostumUserMockMvc.perform(get("/api/costum-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCostumUserMockMvc.perform(get("/api/costum-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCostumUser() throws Exception {
        // Get the costumUser
        restCostumUserMockMvc.perform(get("/api/costum-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostumUser() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        int databaseSizeBeforeUpdate = costumUserRepository.findAll().size();

        // Update the costumUser
        CostumUser updatedCostumUser = costumUserRepository.findById(costumUser.getId()).get();
        // Disconnect from session so that the updates on updatedCostumUser are not directly saved in db
        em.detach(updatedCostumUser);
        updatedCostumUser
            .teint(UPDATED_TEINT)
            .taille(UPDATED_TAILLE)
            .dateNais(UPDATED_DATE_NAIS)
            .sexe(UPDATED_SEXE);
        CostumUserDTO costumUserDTO = costumUserMapper.toDto(updatedCostumUser);

        restCostumUserMockMvc.perform(put("/api/costum-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumUserDTO)))
            .andExpect(status().isOk());

        // Validate the CostumUser in the database
        List<CostumUser> costumUserList = costumUserRepository.findAll();
        assertThat(costumUserList).hasSize(databaseSizeBeforeUpdate);
        CostumUser testCostumUser = costumUserList.get(costumUserList.size() - 1);
        assertThat(testCostumUser.getTeint()).isEqualTo(UPDATED_TEINT);
        assertThat(testCostumUser.getTaille()).isEqualTo(UPDATED_TAILLE);
        assertThat(testCostumUser.getDateNais()).isEqualTo(UPDATED_DATE_NAIS);
        assertThat(testCostumUser.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void updateNonExistingCostumUser() throws Exception {
        int databaseSizeBeforeUpdate = costumUserRepository.findAll().size();

        // Create the CostumUser
        CostumUserDTO costumUserDTO = costumUserMapper.toDto(costumUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostumUserMockMvc.perform(put("/api/costum-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CostumUser in the database
        List<CostumUser> costumUserList = costumUserRepository.findAll();
        assertThat(costumUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCostumUser() throws Exception {
        // Initialize the database
        costumUserRepository.saveAndFlush(costumUser);

        int databaseSizeBeforeDelete = costumUserRepository.findAll().size();

        // Get the costumUser
        restCostumUserMockMvc.perform(delete("/api/costum-users/{id}", costumUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CostumUser> costumUserList = costumUserRepository.findAll();
        assertThat(costumUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostumUser.class);
        CostumUser costumUser1 = new CostumUser();
        costumUser1.setId(1L);
        CostumUser costumUser2 = new CostumUser();
        costumUser2.setId(costumUser1.getId());
        assertThat(costumUser1).isEqualTo(costumUser2);
        costumUser2.setId(2L);
        assertThat(costumUser1).isNotEqualTo(costumUser2);
        costumUser1.setId(null);
        assertThat(costumUser1).isNotEqualTo(costumUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostumUserDTO.class);
        CostumUserDTO costumUserDTO1 = new CostumUserDTO();
        costumUserDTO1.setId(1L);
        CostumUserDTO costumUserDTO2 = new CostumUserDTO();
        assertThat(costumUserDTO1).isNotEqualTo(costumUserDTO2);
        costumUserDTO2.setId(costumUserDTO1.getId());
        assertThat(costumUserDTO1).isEqualTo(costumUserDTO2);
        costumUserDTO2.setId(2L);
        assertThat(costumUserDTO1).isNotEqualTo(costumUserDTO2);
        costumUserDTO1.setId(null);
        assertThat(costumUserDTO1).isNotEqualTo(costumUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(costumUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(costumUserMapper.fromId(null)).isNull();
    }
}
