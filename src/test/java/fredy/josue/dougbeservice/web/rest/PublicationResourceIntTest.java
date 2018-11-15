package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Publication;
import fredy.josue.dougbeservice.domain.Commentaire;
import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.repository.PublicationRepository;
import fredy.josue.dougbeservice.service.PublicationService;
import fredy.josue.dougbeservice.service.dto.PublicationDTO;
import fredy.josue.dougbeservice.service.mapper.PublicationMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.PublicationCriteria;
import fredy.josue.dougbeservice.service.PublicationQueryService;

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

import fredy.josue.dougbeservice.domain.enumeration.QuiVoit;
/**
 * Test class for the PublicationResource REST controller.
 *
 * @see PublicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class PublicationResourceIntTest {

    private static final String DEFAULT_PUB = "AAAAAAAAAA";
    private static final String UPDATED_PUB = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGEURL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEURL = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_JAIME = 1;
    private static final Integer UPDATED_NB_JAIME = 2;

    private static final Integer DEFAULT_NB_JAIME_PAS = 1;
    private static final Integer UPDATED_NB_JAIME_PAS = 2;

    private static final QuiVoit DEFAULT_QUI_VOIT = QuiVoit.PUBLIC;
    private static final QuiVoit UPDATED_QUI_VOIT = QuiVoit.AMIS;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationMapper publicationMapper;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private PublicationQueryService publicationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPublicationMockMvc;

    private Publication publication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PublicationResource publicationResource = new PublicationResource(publicationService, publicationQueryService);
        this.restPublicationMockMvc = MockMvcBuilders.standaloneSetup(publicationResource)
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
    public static Publication createEntity(EntityManager em) {
        Publication publication = new Publication()
            .pub(DEFAULT_PUB)
            .imageurl(DEFAULT_IMAGEURL)
            .nbJaime(DEFAULT_NB_JAIME)
            .nbJaimePas(DEFAULT_NB_JAIME_PAS)
            .quiVoit(DEFAULT_QUI_VOIT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return publication;
    }

    @Before
    public void initTest() {
        publication = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublication() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);
        restPublicationMockMvc.perform(post("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isCreated());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeCreate + 1);
        Publication testPublication = publicationList.get(publicationList.size() - 1);
        assertThat(testPublication.getPub()).isEqualTo(DEFAULT_PUB);
        assertThat(testPublication.getImageurl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(testPublication.getNbJaime()).isEqualTo(DEFAULT_NB_JAIME);
        assertThat(testPublication.getNbJaimePas()).isEqualTo(DEFAULT_NB_JAIME_PAS);
        assertThat(testPublication.getQuiVoit()).isEqualTo(DEFAULT_QUI_VOIT);
        assertThat(testPublication.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPublication.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPublication.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPublication.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createPublicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication with an existing ID
        publication.setId(1L);
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicationMockMvc.perform(post("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPublications() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
            .andExpect(jsonPath("$.[*].pub").value(hasItem(DEFAULT_PUB.toString())))
            .andExpect(jsonPath("$.[*].imageurl").value(hasItem(DEFAULT_IMAGEURL.toString())))
            .andExpect(jsonPath("$.[*].nbJaime").value(hasItem(DEFAULT_NB_JAIME)))
            .andExpect(jsonPath("$.[*].nbJaimePas").value(hasItem(DEFAULT_NB_JAIME_PAS)))
            .andExpect(jsonPath("$.[*].quiVoit").value(hasItem(DEFAULT_QUI_VOIT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", publication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publication.getId().intValue()))
            .andExpect(jsonPath("$.pub").value(DEFAULT_PUB.toString()))
            .andExpect(jsonPath("$.imageurl").value(DEFAULT_IMAGEURL.toString()))
            .andExpect(jsonPath("$.nbJaime").value(DEFAULT_NB_JAIME))
            .andExpect(jsonPath("$.nbJaimePas").value(DEFAULT_NB_JAIME_PAS))
            .andExpect(jsonPath("$.quiVoit").value(DEFAULT_QUI_VOIT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllPublicationsByPubIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where pub equals to DEFAULT_PUB
        defaultPublicationShouldBeFound("pub.equals=" + DEFAULT_PUB);

        // Get all the publicationList where pub equals to UPDATED_PUB
        defaultPublicationShouldNotBeFound("pub.equals=" + UPDATED_PUB);
    }

    @Test
    @Transactional
    public void getAllPublicationsByPubIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where pub in DEFAULT_PUB or UPDATED_PUB
        defaultPublicationShouldBeFound("pub.in=" + DEFAULT_PUB + "," + UPDATED_PUB);

        // Get all the publicationList where pub equals to UPDATED_PUB
        defaultPublicationShouldNotBeFound("pub.in=" + UPDATED_PUB);
    }

    @Test
    @Transactional
    public void getAllPublicationsByPubIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where pub is not null
        defaultPublicationShouldBeFound("pub.specified=true");

        // Get all the publicationList where pub is null
        defaultPublicationShouldNotBeFound("pub.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByImageurlIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where imageurl equals to DEFAULT_IMAGEURL
        defaultPublicationShouldBeFound("imageurl.equals=" + DEFAULT_IMAGEURL);

        // Get all the publicationList where imageurl equals to UPDATED_IMAGEURL
        defaultPublicationShouldNotBeFound("imageurl.equals=" + UPDATED_IMAGEURL);
    }

    @Test
    @Transactional
    public void getAllPublicationsByImageurlIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where imageurl in DEFAULT_IMAGEURL or UPDATED_IMAGEURL
        defaultPublicationShouldBeFound("imageurl.in=" + DEFAULT_IMAGEURL + "," + UPDATED_IMAGEURL);

        // Get all the publicationList where imageurl equals to UPDATED_IMAGEURL
        defaultPublicationShouldNotBeFound("imageurl.in=" + UPDATED_IMAGEURL);
    }

    @Test
    @Transactional
    public void getAllPublicationsByImageurlIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where imageurl is not null
        defaultPublicationShouldBeFound("imageurl.specified=true");

        // Get all the publicationList where imageurl is null
        defaultPublicationShouldNotBeFound("imageurl.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimeIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaime equals to DEFAULT_NB_JAIME
        defaultPublicationShouldBeFound("nbJaime.equals=" + DEFAULT_NB_JAIME);

        // Get all the publicationList where nbJaime equals to UPDATED_NB_JAIME
        defaultPublicationShouldNotBeFound("nbJaime.equals=" + UPDATED_NB_JAIME);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimeIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaime in DEFAULT_NB_JAIME or UPDATED_NB_JAIME
        defaultPublicationShouldBeFound("nbJaime.in=" + DEFAULT_NB_JAIME + "," + UPDATED_NB_JAIME);

        // Get all the publicationList where nbJaime equals to UPDATED_NB_JAIME
        defaultPublicationShouldNotBeFound("nbJaime.in=" + UPDATED_NB_JAIME);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaime is not null
        defaultPublicationShouldBeFound("nbJaime.specified=true");

        // Get all the publicationList where nbJaime is null
        defaultPublicationShouldNotBeFound("nbJaime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaime greater than or equals to DEFAULT_NB_JAIME
        defaultPublicationShouldBeFound("nbJaime.greaterOrEqualThan=" + DEFAULT_NB_JAIME);

        // Get all the publicationList where nbJaime greater than or equals to UPDATED_NB_JAIME
        defaultPublicationShouldNotBeFound("nbJaime.greaterOrEqualThan=" + UPDATED_NB_JAIME);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimeIsLessThanSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaime less than or equals to DEFAULT_NB_JAIME
        defaultPublicationShouldNotBeFound("nbJaime.lessThan=" + DEFAULT_NB_JAIME);

        // Get all the publicationList where nbJaime less than or equals to UPDATED_NB_JAIME
        defaultPublicationShouldBeFound("nbJaime.lessThan=" + UPDATED_NB_JAIME);
    }


    @Test
    @Transactional
    public void getAllPublicationsByNbJaimePasIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaimePas equals to DEFAULT_NB_JAIME_PAS
        defaultPublicationShouldBeFound("nbJaimePas.equals=" + DEFAULT_NB_JAIME_PAS);

        // Get all the publicationList where nbJaimePas equals to UPDATED_NB_JAIME_PAS
        defaultPublicationShouldNotBeFound("nbJaimePas.equals=" + UPDATED_NB_JAIME_PAS);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimePasIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaimePas in DEFAULT_NB_JAIME_PAS or UPDATED_NB_JAIME_PAS
        defaultPublicationShouldBeFound("nbJaimePas.in=" + DEFAULT_NB_JAIME_PAS + "," + UPDATED_NB_JAIME_PAS);

        // Get all the publicationList where nbJaimePas equals to UPDATED_NB_JAIME_PAS
        defaultPublicationShouldNotBeFound("nbJaimePas.in=" + UPDATED_NB_JAIME_PAS);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimePasIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaimePas is not null
        defaultPublicationShouldBeFound("nbJaimePas.specified=true");

        // Get all the publicationList where nbJaimePas is null
        defaultPublicationShouldNotBeFound("nbJaimePas.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimePasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaimePas greater than or equals to DEFAULT_NB_JAIME_PAS
        defaultPublicationShouldBeFound("nbJaimePas.greaterOrEqualThan=" + DEFAULT_NB_JAIME_PAS);

        // Get all the publicationList where nbJaimePas greater than or equals to UPDATED_NB_JAIME_PAS
        defaultPublicationShouldNotBeFound("nbJaimePas.greaterOrEqualThan=" + UPDATED_NB_JAIME_PAS);
    }

    @Test
    @Transactional
    public void getAllPublicationsByNbJaimePasIsLessThanSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where nbJaimePas less than or equals to DEFAULT_NB_JAIME_PAS
        defaultPublicationShouldNotBeFound("nbJaimePas.lessThan=" + DEFAULT_NB_JAIME_PAS);

        // Get all the publicationList where nbJaimePas less than or equals to UPDATED_NB_JAIME_PAS
        defaultPublicationShouldBeFound("nbJaimePas.lessThan=" + UPDATED_NB_JAIME_PAS);
    }


    @Test
    @Transactional
    public void getAllPublicationsByQuiVoitIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where quiVoit equals to DEFAULT_QUI_VOIT
        defaultPublicationShouldBeFound("quiVoit.equals=" + DEFAULT_QUI_VOIT);

        // Get all the publicationList where quiVoit equals to UPDATED_QUI_VOIT
        defaultPublicationShouldNotBeFound("quiVoit.equals=" + UPDATED_QUI_VOIT);
    }

    @Test
    @Transactional
    public void getAllPublicationsByQuiVoitIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where quiVoit in DEFAULT_QUI_VOIT or UPDATED_QUI_VOIT
        defaultPublicationShouldBeFound("quiVoit.in=" + DEFAULT_QUI_VOIT + "," + UPDATED_QUI_VOIT);

        // Get all the publicationList where quiVoit equals to UPDATED_QUI_VOIT
        defaultPublicationShouldNotBeFound("quiVoit.in=" + UPDATED_QUI_VOIT);
    }

    @Test
    @Transactional
    public void getAllPublicationsByQuiVoitIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where quiVoit is not null
        defaultPublicationShouldBeFound("quiVoit.specified=true");

        // Get all the publicationList where quiVoit is null
        defaultPublicationShouldNotBeFound("quiVoit.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdBy equals to DEFAULT_CREATED_BY
        defaultPublicationShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the publicationList where createdBy equals to UPDATED_CREATED_BY
        defaultPublicationShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultPublicationShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the publicationList where createdBy equals to UPDATED_CREATED_BY
        defaultPublicationShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdBy is not null
        defaultPublicationShouldBeFound("createdBy.specified=true");

        // Get all the publicationList where createdBy is null
        defaultPublicationShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdBy greater than or equals to DEFAULT_CREATED_BY
        defaultPublicationShouldBeFound("createdBy.greaterOrEqualThan=" + DEFAULT_CREATED_BY);

        // Get all the publicationList where createdBy greater than or equals to UPDATED_CREATED_BY
        defaultPublicationShouldNotBeFound("createdBy.greaterOrEqualThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdBy less than or equals to DEFAULT_CREATED_BY
        defaultPublicationShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the publicationList where createdBy less than or equals to UPDATED_CREATED_BY
        defaultPublicationShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllPublicationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultPublicationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the publicationList where createdDate equals to UPDATED_CREATED_DATE
        defaultPublicationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultPublicationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the publicationList where createdDate equals to UPDATED_CREATED_DATE
        defaultPublicationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPublicationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where createdDate is not null
        defaultPublicationShouldBeFound("createdDate.specified=true");

        // Get all the publicationList where createdDate is null
        defaultPublicationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPublicationShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the publicationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPublicationShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPublicationShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the publicationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPublicationShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedBy is not null
        defaultPublicationShouldBeFound("lastModifiedBy.specified=true");

        // Get all the publicationList where lastModifiedBy is null
        defaultPublicationShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedBy greater than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultPublicationShouldBeFound("lastModifiedBy.greaterOrEqualThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the publicationList where lastModifiedBy greater than or equals to UPDATED_LAST_MODIFIED_BY
        defaultPublicationShouldNotBeFound("lastModifiedBy.greaterOrEqualThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedBy less than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultPublicationShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the publicationList where lastModifiedBy less than or equals to UPDATED_LAST_MODIFIED_BY
        defaultPublicationShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPublicationShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the publicationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPublicationShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultPublicationShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the publicationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPublicationShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPublicationsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList where lastModifiedDate is not null
        defaultPublicationShouldBeFound("lastModifiedDate.specified=true");

        // Get all the publicationList where lastModifiedDate is null
        defaultPublicationShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        Commentaire commentaire = CommentaireResourceIntTest.createEntity(em);
        em.persist(commentaire);
        em.flush();
        publication.addCommentaire(commentaire);
        publicationRepository.saveAndFlush(publication);
        Long commentaireId = commentaire.getId();

        // Get all the publicationList where commentaire equals to commentaireId
        defaultPublicationShouldBeFound("commentaireId.equals=" + commentaireId);

        // Get all the publicationList where commentaire equals to commentaireId + 1
        defaultPublicationShouldNotBeFound("commentaireId.equals=" + (commentaireId + 1));
    }


    @Test
    @Transactional
    public void getAllPublicationsByCostumUserIsEqualToSomething() throws Exception {
        // Initialize the database
        CostumUser costumUser = CostumUserResourceIntTest.createEntity(em);
        em.persist(costumUser);
        em.flush();
        publication.setCostumUser(costumUser);
        publicationRepository.saveAndFlush(publication);
        Long costumUserId = costumUser.getId();

        // Get all the publicationList where costumUser equals to costumUserId
        defaultPublicationShouldBeFound("costumUserId.equals=" + costumUserId);

        // Get all the publicationList where costumUser equals to costumUserId + 1
        defaultPublicationShouldNotBeFound("costumUserId.equals=" + (costumUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPublicationShouldBeFound(String filter) throws Exception {
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
            .andExpect(jsonPath("$.[*].pub").value(hasItem(DEFAULT_PUB.toString())))
            .andExpect(jsonPath("$.[*].imageurl").value(hasItem(DEFAULT_IMAGEURL.toString())))
            .andExpect(jsonPath("$.[*].nbJaime").value(hasItem(DEFAULT_NB_JAIME)))
            .andExpect(jsonPath("$.[*].nbJaimePas").value(hasItem(DEFAULT_NB_JAIME_PAS)))
            .andExpect(jsonPath("$.[*].quiVoit").value(hasItem(DEFAULT_QUI_VOIT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restPublicationMockMvc.perform(get("/api/publications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPublicationShouldNotBeFound(String filter) throws Exception {
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublicationMockMvc.perform(get("/api/publications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPublication() throws Exception {
        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Update the publication
        Publication updatedPublication = publicationRepository.findById(publication.getId()).get();
        // Disconnect from session so that the updates on updatedPublication are not directly saved in db
        em.detach(updatedPublication);
        updatedPublication
            .pub(UPDATED_PUB)
            .imageurl(UPDATED_IMAGEURL)
            .nbJaime(UPDATED_NB_JAIME)
            .nbJaimePas(UPDATED_NB_JAIME_PAS)
            .quiVoit(UPDATED_QUI_VOIT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PublicationDTO publicationDTO = publicationMapper.toDto(updatedPublication);

        restPublicationMockMvc.perform(put("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isOk());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeUpdate);
        Publication testPublication = publicationList.get(publicationList.size() - 1);
        assertThat(testPublication.getPub()).isEqualTo(UPDATED_PUB);
        assertThat(testPublication.getImageurl()).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testPublication.getNbJaime()).isEqualTo(UPDATED_NB_JAIME);
        assertThat(testPublication.getNbJaimePas()).isEqualTo(UPDATED_NB_JAIME_PAS);
        assertThat(testPublication.getQuiVoit()).isEqualTo(UPDATED_QUI_VOIT);
        assertThat(testPublication.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPublication.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPublication.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPublication.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPublication() throws Exception {
        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Create the Publication
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublicationMockMvc.perform(put("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        int databaseSizeBeforeDelete = publicationRepository.findAll().size();

        // Get the publication
        restPublicationMockMvc.perform(delete("/api/publications/{id}", publication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publication.class);
        Publication publication1 = new Publication();
        publication1.setId(1L);
        Publication publication2 = new Publication();
        publication2.setId(publication1.getId());
        assertThat(publication1).isEqualTo(publication2);
        publication2.setId(2L);
        assertThat(publication1).isNotEqualTo(publication2);
        publication1.setId(null);
        assertThat(publication1).isNotEqualTo(publication2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicationDTO.class);
        PublicationDTO publicationDTO1 = new PublicationDTO();
        publicationDTO1.setId(1L);
        PublicationDTO publicationDTO2 = new PublicationDTO();
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
        publicationDTO2.setId(publicationDTO1.getId());
        assertThat(publicationDTO1).isEqualTo(publicationDTO2);
        publicationDTO2.setId(2L);
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
        publicationDTO1.setId(null);
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(publicationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(publicationMapper.fromId(null)).isNull();
    }
}
