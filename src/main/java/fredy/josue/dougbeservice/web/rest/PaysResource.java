package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.PaysService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.PaysDTO;
import fredy.josue.dougbeservice.service.dto.PaysCriteria;
import fredy.josue.dougbeservice.service.PaysQueryService;
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
 * REST controller for managing Pays.
 */
@RestController
@RequestMapping("/api")
public class PaysResource {

    private final Logger log = LoggerFactory.getLogger(PaysResource.class);

    private static final String ENTITY_NAME = "dougbeServicePays";

    private final PaysService paysService;

    private final PaysQueryService paysQueryService;

    public PaysResource(PaysService paysService, PaysQueryService paysQueryService) {
        this.paysService = paysService;
        this.paysQueryService = paysQueryService;
    }

    /**
     * POST  /pays : Create a new pays.
     *
     * @param paysDTO the paysDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paysDTO, or with status 400 (Bad Request) if the pays has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pays")
    @Timed
    public ResponseEntity<PaysDTO> createPays(@Valid @RequestBody PaysDTO paysDTO) throws URISyntaxException {
        log.debug("REST request to save Pays : {}", paysDTO);
        if (paysDTO.getId() != null) {
            throw new BadRequestAlertException("A new pays cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaysDTO result = paysService.save(paysDTO);
        return ResponseEntity.created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pays : Updates an existing pays.
     *
     * @param paysDTO the paysDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paysDTO,
     * or with status 400 (Bad Request) if the paysDTO is not valid,
     * or with status 500 (Internal Server Error) if the paysDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pays")
    @Timed
    public ResponseEntity<PaysDTO> updatePays(@Valid @RequestBody PaysDTO paysDTO) throws URISyntaxException {
        log.debug("REST request to update Pays : {}", paysDTO);
        if (paysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaysDTO result = paysService.save(paysDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paysDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pays : get all the pays.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of pays in body
     */
    @GetMapping("/pays")
    @Timed
    public ResponseEntity<List<PaysDTO>> getAllPays(PaysCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pays by criteria: {}", criteria);
        Page<PaysDTO> page = paysQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pays");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /pays/count : count all the pays.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/pays/count")
    @Timed
    public ResponseEntity<Long> countPays(PaysCriteria criteria) {
        log.debug("REST request to count Pays by criteria: {}", criteria);
        return ResponseEntity.ok().body(paysQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /pays/:id : get the "id" pays.
     *
     * @param id the id of the paysDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paysDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pays/{id}")
    @Timed
    public ResponseEntity<PaysDTO> getPays(@PathVariable Long id) {
        log.debug("REST request to get Pays : {}", id);
        Optional<PaysDTO> paysDTO = paysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paysDTO);
    }

    /**
     * DELETE  /pays/:id : delete the "id" pays.
     *
     * @param id the id of the paysDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pays/{id}")
    @Timed
    public ResponseEntity<Void> deletePays(@PathVariable Long id) {
        log.debug("REST request to delete Pays : {}", id);
        paysService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
