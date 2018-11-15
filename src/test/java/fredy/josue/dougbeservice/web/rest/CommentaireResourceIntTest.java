package fredy.josue.dougbeservice.web.rest;

import fredy.josue.dougbeservice.DougbeServiceApp;

import fredy.josue.dougbeservice.domain.Commentaire;
import fredy.josue.dougbeservice.domain.Publication;
import fredy.josue.dougbeservice.repository.CommentaireRepository;
import fredy.josue.dougbeservice.service.CommentaireService;
import fredy.josue.dougbeservice.service.dto.CommentaireDTO;
import fredy.josue.dougbeservice.service.mapper.CommentaireMapper;
import fredy.josue.dougbeservice.web.rest.errors.ExceptionTranslator;
import fredy.josue.dougbeservice.service.dto.CommentaireCriteria;
import fredy.josue.dougbeservice.service.CommentaireQueryService;

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
 * Test class for the CommentaireResource REST controller.
 *
 * @see CommentaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DougbeServiceApp.class)
public class CommentaireResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_JAIME = false;
    private static final Boolean UPDATED_JAIME = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private CommentaireMapper commentaireMapper;

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private CommentaireQueryService commentaireQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommentaireResource commentaireResource = new CommentaireResource(commentaireService, commentaireQueryService);
        this.restCommentaireMockMvc = MockMvcBuilders.standaloneSetup(commentaireResource)
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
    public static Commentaire createEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire()
            .comment(DEFAULT_COMMENT)
            .jaime(DEFAULT_JAIME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return commentaire;
    }

    @Before
    public void initTest() {
        commentaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentaire() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);
        restCommentaireMockMvc.perform(post("/api/commentaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isCreated());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate + 1);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testCommentaire.isJaime()).isEqualTo(DEFAULT_JAIME);
        assertThat(testCommentaire.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommentaire.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCommentaire.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCommentaire.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createCommentaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // Create the Commentaire with an existing ID
        commentaire.setId(1L);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentaireMockMvc.perform(post("/api/commentaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentaireRepository.findAll().size();
        // set the field null
        commentaire.setComment(null);

        // Create the Commentaire, which fails.
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        restCommentaireMockMvc.perform(post("/api/commentaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommentaires() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList
        restCommentaireMockMvc.perform(get("/api/commentaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].jaime").value(hasItem(DEFAULT_JAIME.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.jaime").value(DEFAULT_JAIME.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllCommentairesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where comment equals to DEFAULT_COMMENT
        defaultCommentaireShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the commentaireList where comment equals to UPDATED_COMMENT
        defaultCommentaireShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultCommentaireShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the commentaireList where comment equals to UPDATED_COMMENT
        defaultCommentaireShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where comment is not null
        defaultCommentaireShouldBeFound("comment.specified=true");

        // Get all the commentaireList where comment is null
        defaultCommentaireShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByJaimeIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where jaime equals to DEFAULT_JAIME
        defaultCommentaireShouldBeFound("jaime.equals=" + DEFAULT_JAIME);

        // Get all the commentaireList where jaime equals to UPDATED_JAIME
        defaultCommentaireShouldNotBeFound("jaime.equals=" + UPDATED_JAIME);
    }

    @Test
    @Transactional
    public void getAllCommentairesByJaimeIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where jaime in DEFAULT_JAIME or UPDATED_JAIME
        defaultCommentaireShouldBeFound("jaime.in=" + DEFAULT_JAIME + "," + UPDATED_JAIME);

        // Get all the commentaireList where jaime equals to UPDATED_JAIME
        defaultCommentaireShouldNotBeFound("jaime.in=" + UPDATED_JAIME);
    }

    @Test
    @Transactional
    public void getAllCommentairesByJaimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where jaime is not null
        defaultCommentaireShouldBeFound("jaime.specified=true");

        // Get all the commentaireList where jaime is null
        defaultCommentaireShouldNotBeFound("jaime.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdBy equals to DEFAULT_CREATED_BY
        defaultCommentaireShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the commentaireList where createdBy equals to UPDATED_CREATED_BY
        defaultCommentaireShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCommentaireShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the commentaireList where createdBy equals to UPDATED_CREATED_BY
        defaultCommentaireShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdBy is not null
        defaultCommentaireShouldBeFound("createdBy.specified=true");

        // Get all the commentaireList where createdBy is null
        defaultCommentaireShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdBy greater than or equals to DEFAULT_CREATED_BY
        defaultCommentaireShouldBeFound("createdBy.greaterOrEqualThan=" + DEFAULT_CREATED_BY);

        // Get all the commentaireList where createdBy greater than or equals to UPDATED_CREATED_BY
        defaultCommentaireShouldNotBeFound("createdBy.greaterOrEqualThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdBy less than or equals to DEFAULT_CREATED_BY
        defaultCommentaireShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the commentaireList where createdBy less than or equals to UPDATED_CREATED_BY
        defaultCommentaireShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllCommentairesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCommentaireShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the commentaireList where createdDate equals to UPDATED_CREATED_DATE
        defaultCommentaireShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCommentaireShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the commentaireList where createdDate equals to UPDATED_CREATED_DATE
        defaultCommentaireShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommentairesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where createdDate is not null
        defaultCommentaireShouldBeFound("createdDate.specified=true");

        // Get all the commentaireList where createdDate is null
        defaultCommentaireShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCommentaireShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the commentaireList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCommentaireShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCommentaireShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the commentaireList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCommentaireShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedBy is not null
        defaultCommentaireShouldBeFound("lastModifiedBy.specified=true");

        // Get all the commentaireList where lastModifiedBy is null
        defaultCommentaireShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedBy greater than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultCommentaireShouldBeFound("lastModifiedBy.greaterOrEqualThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the commentaireList where lastModifiedBy greater than or equals to UPDATED_LAST_MODIFIED_BY
        defaultCommentaireShouldNotBeFound("lastModifiedBy.greaterOrEqualThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedBy less than or equals to DEFAULT_LAST_MODIFIED_BY
        defaultCommentaireShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the commentaireList where lastModifiedBy less than or equals to UPDATED_LAST_MODIFIED_BY
        defaultCommentaireShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCommentaireShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the commentaireList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCommentaireShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCommentaireShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the commentaireList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCommentaireShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommentairesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where lastModifiedDate is not null
        defaultCommentaireShouldBeFound("lastModifiedDate.specified=true");

        // Get all the commentaireList where lastModifiedDate is null
        defaultCommentaireShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentairesByPublicationIsEqualToSomething() throws Exception {
        // Initialize the database
        Publication publication = PublicationResourceIntTest.createEntity(em);
        em.persist(publication);
        em.flush();
        commentaire.setPublication(publication);
        commentaireRepository.saveAndFlush(commentaire);
        Long publicationId = publication.getId();

        // Get all the commentaireList where publication equals to publicationId
        defaultCommentaireShouldBeFound("publicationId.equals=" + publicationId);

        // Get all the commentaireList where publication equals to publicationId + 1
        defaultCommentaireShouldNotBeFound("publicationId.equals=" + (publicationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCommentaireShouldBeFound(String filter) throws Exception {
        restCommentaireMockMvc.perform(get("/api/commentaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].jaime").value(hasItem(DEFAULT_JAIME.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restCommentaireMockMvc.perform(get("/api/commentaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCommentaireShouldNotBeFound(String filter) throws Exception {
        restCommentaireMockMvc.perform(get("/api/commentaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentaireMockMvc.perform(get("/api/commentaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire
        Commentaire updatedCommentaire = commentaireRepository.findById(commentaire.getId()).get();
        // Disconnect from session so that the updates on updatedCommentaire are not directly saved in db
        em.detach(updatedCommentaire);
        updatedCommentaire
            .comment(UPDATED_COMMENT)
            .jaime(UPDATED_JAIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(updatedCommentaire);

        restCommentaireMockMvc.perform(put("/api/commentaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testCommentaire.isJaime()).isEqualTo(UPDATED_JAIME);
        assertThat(testCommentaire.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommentaire.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCommentaire.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCommentaire.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc.perform(put("/api/commentaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeDelete = commentaireRepository.findAll().size();

        // Get the commentaire
        restCommentaireMockMvc.perform(delete("/api/commentaires/{id}", commentaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commentaire.class);
        Commentaire commentaire1 = new Commentaire();
        commentaire1.setId(1L);
        Commentaire commentaire2 = new Commentaire();
        commentaire2.setId(commentaire1.getId());
        assertThat(commentaire1).isEqualTo(commentaire2);
        commentaire2.setId(2L);
        assertThat(commentaire1).isNotEqualTo(commentaire2);
        commentaire1.setId(null);
        assertThat(commentaire1).isNotEqualTo(commentaire2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentaireDTO.class);
        CommentaireDTO commentaireDTO1 = new CommentaireDTO();
        commentaireDTO1.setId(1L);
        CommentaireDTO commentaireDTO2 = new CommentaireDTO();
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
        commentaireDTO2.setId(commentaireDTO1.getId());
        assertThat(commentaireDTO1).isEqualTo(commentaireDTO2);
        commentaireDTO2.setId(2L);
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
        commentaireDTO1.setId(null);
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commentaireMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commentaireMapper.fromId(null)).isNull();
    }
}
