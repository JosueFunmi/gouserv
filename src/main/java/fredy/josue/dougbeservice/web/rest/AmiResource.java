package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.AmiService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.AmiDTO;
import fredy.josue.dougbeservice.service.dto.AmiCriteria;
import fredy.josue.dougbeservice.service.AmiQueryService;
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
 * REST controller for managing Ami.
 */
@RestController
@RequestMapping("/api")
public class AmiResource {

    private final Logger log = LoggerFactory.getLogger(AmiResource.class);

    private static final String ENTITY_NAME = "dougbeServiceAmi";

    private final AmiService amiService;

    private final AmiQueryService amiQueryService;

    public AmiResource(AmiService amiService, AmiQueryService amiQueryService) {
        this.amiService = amiService;
        this.amiQueryService = amiQueryService;
    }

    /**
     * POST  /amis : Create a new ami.
     *
     * @param amiDTO the amiDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amiDTO, or with status 400 (Bad Request) if the ami has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amis")
    @Timed
    public ResponseEntity<AmiDTO> createAmi(@Valid @RequestBody AmiDTO amiDTO) throws URISyntaxException {
        log.debug("REST request to save Ami : {}", amiDTO);
        if (amiDTO.getId() != null) {
            throw new BadRequestAlertException("A new ami cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmiDTO result = amiService.save(amiDTO);
        return ResponseEntity.created(new URI("/api/amis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amis : Updates an existing ami.
     *
     * @param amiDTO the amiDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amiDTO,
     * or with status 400 (Bad Request) if the amiDTO is not valid,
     * or with status 500 (Internal Server Error) if the amiDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amis")
    @Timed
    public ResponseEntity<AmiDTO> updateAmi(@Valid @RequestBody AmiDTO amiDTO) throws URISyntaxException {
        log.debug("REST request to update Ami : {}", amiDTO);
        if (amiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AmiDTO result = amiService.save(amiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amiDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amis : get all the amis.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of amis in body
     */
    @GetMapping("/amis")
    @Timed
    public ResponseEntity<List<AmiDTO>> getAllAmis(AmiCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Amis by criteria: {}", criteria);
        Page<AmiDTO> page = amiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amis");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /amis/count : count all the amis.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/amis/count")
    @Timed
    public ResponseEntity<Long> countAmis(AmiCriteria criteria) {
        log.debug("REST request to count Amis by criteria: {}", criteria);
        return ResponseEntity.ok().body(amiQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /amis/:id : get the "id" ami.
     *
     * @param id the id of the amiDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amiDTO, or with status 404 (Not Found)
     */
    @GetMapping("/amis/{id}")
    @Timed
    public ResponseEntity<AmiDTO> getAmi(@PathVariable Long id) {
        log.debug("REST request to get Ami : {}", id);
        Optional<AmiDTO> amiDTO = amiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amiDTO);
    }

    /**
     * DELETE  /amis/:id : delete the "id" ami.
     *
     * @param id the id of the amiDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amis/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmi(@PathVariable Long id) {
        log.debug("REST request to delete Ami : {}", id);
        amiService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
