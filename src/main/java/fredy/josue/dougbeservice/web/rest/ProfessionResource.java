package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.ProfessionService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.ProfessionDTO;
import fredy.josue.dougbeservice.service.dto.ProfessionCriteria;
import fredy.josue.dougbeservice.service.ProfessionQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Profession.
 */
@RestController
@RequestMapping("/api")
public class ProfessionResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionResource.class);

    private static final String ENTITY_NAME = "dougbeServiceProfession";

    private final ProfessionService professionService;

    private final ProfessionQueryService professionQueryService;

    public ProfessionResource(ProfessionService professionService, ProfessionQueryService professionQueryService) {
        this.professionService = professionService;
        this.professionQueryService = professionQueryService;
    }

    /**
     * POST  /professions : Create a new profession.
     *
     * @param professionDTO the professionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new professionDTO, or with status 400 (Bad Request) if the profession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/professions")
    @Timed
    public ResponseEntity<ProfessionDTO> createProfession(@Valid @RequestBody ProfessionDTO professionDTO) throws URISyntaxException {
        log.debug("REST request to save Profession : {}", professionDTO);
        if (professionDTO.getId() != null) {
            throw new BadRequestAlertException("A new profession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfessionDTO result = professionService.save(professionDTO);
        return ResponseEntity.created(new URI("/api/professions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /professions : Updates an existing profession.
     *
     * @param professionDTO the professionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated professionDTO,
     * or with status 400 (Bad Request) if the professionDTO is not valid,
     * or with status 500 (Internal Server Error) if the professionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/professions")
    @Timed
    public ResponseEntity<ProfessionDTO> updateProfession(@Valid @RequestBody ProfessionDTO professionDTO) throws URISyntaxException {
        log.debug("REST request to update Profession : {}", professionDTO);
        if (professionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfessionDTO result = professionService.save(professionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, professionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /professions : get all the professions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of professions in body
     */
    @GetMapping("/professions")
    @Timed
    public ResponseEntity<List<ProfessionDTO>> getAllProfessions(ProfessionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Professions by criteria: {}", criteria);
        Page<ProfessionDTO> page = professionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /professions/count : count all the professions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/professions/count")
    @Timed
    public ResponseEntity<Long> countProfessions(ProfessionCriteria criteria) {
        log.debug("REST request to count Professions by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /professions/:id : get the "id" profession.
     *
     * @param id the id of the professionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the professionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/professions/{id}")
    @Timed
    public ResponseEntity<ProfessionDTO> getProfession(@PathVariable Long id) {
        log.debug("REST request to get Profession : {}", id);
        Optional<ProfessionDTO> professionDTO = professionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionDTO);
    }

    /**
     * DELETE  /professions/:id : delete the "id" profession.
     *
     * @param id the id of the professionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/professions/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfession(@PathVariable Long id) {
        log.debug("REST request to delete Profession : {}", id);
        professionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
