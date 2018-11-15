package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.PublicationService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.PublicationDTO;
import fredy.josue.dougbeservice.service.dto.PublicationCriteria;
import fredy.josue.dougbeservice.service.PublicationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Publication.
 */
@RestController
@RequestMapping("/api")
public class PublicationResource {

    private final Logger log = LoggerFactory.getLogger(PublicationResource.class);

    private static final String ENTITY_NAME = "dougbeServicePublication";

    private final PublicationService publicationService;

    private final PublicationQueryService publicationQueryService;

    public PublicationResource(PublicationService publicationService, PublicationQueryService publicationQueryService) {
        this.publicationService = publicationService;
        this.publicationQueryService = publicationQueryService;
    }

    /**
     * POST  /publications : Create a new publication.
     *
     * @param publicationDTO the publicationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publicationDTO, or with status 400 (Bad Request) if the publication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/publications")
    @Timed
    public ResponseEntity<PublicationDTO> createPublication(@RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to save Publication : {}", publicationDTO);
        if (publicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new publication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.created(new URI("/api/publications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publications : Updates an existing publication.
     *
     * @param publicationDTO the publicationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publicationDTO,
     * or with status 400 (Bad Request) if the publicationDTO is not valid,
     * or with status 500 (Internal Server Error) if the publicationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publications")
    @Timed
    public ResponseEntity<PublicationDTO> updatePublication(@RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to update Publication : {}", publicationDTO);
        if (publicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publications : get all the publications.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of publications in body
     */
    @GetMapping("/publications")
    @Timed
    public ResponseEntity<List<PublicationDTO>> getAllPublications(PublicationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Publications by criteria: {}", criteria);
        Page<PublicationDTO> page = publicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/publications");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /publications/count : count all the publications.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/publications/count")
    @Timed
    public ResponseEntity<Long> countPublications(PublicationCriteria criteria) {
        log.debug("REST request to count Publications by criteria: {}", criteria);
        return ResponseEntity.ok().body(publicationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /publications/:id : get the "id" publication.
     *
     * @param id the id of the publicationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publicationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/publications/{id}")
    @Timed
    public ResponseEntity<PublicationDTO> getPublication(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<PublicationDTO> publicationDTO = publicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicationDTO);
    }

    /**
     * DELETE  /publications/:id : delete the "id" publication.
     *
     * @param id the id of the publicationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/publications/{id}")
    @Timed
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);
        publicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
