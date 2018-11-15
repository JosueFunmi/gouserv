package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.CostumUserService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.CostumUserDTO;
import fredy.josue.dougbeservice.service.dto.CostumUserCriteria;
import fredy.josue.dougbeservice.service.CostumUserQueryService;
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
 * REST controller for managing CostumUser.
 */
@RestController
@RequestMapping("/api")
public class CostumUserResource {

    private final Logger log = LoggerFactory.getLogger(CostumUserResource.class);

    private static final String ENTITY_NAME = "dougbeServiceCostumUser";

    private final CostumUserService costumUserService;

    private final CostumUserQueryService costumUserQueryService;

    public CostumUserResource(CostumUserService costumUserService, CostumUserQueryService costumUserQueryService) {
        this.costumUserService = costumUserService;
        this.costumUserQueryService = costumUserQueryService;
    }

    /**
     * POST  /costum-users : Create a new costumUser.
     *
     * @param costumUserDTO the costumUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new costumUserDTO, or with status 400 (Bad Request) if the costumUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/costum-users")
    @Timed
    public ResponseEntity<CostumUserDTO> createCostumUser(@RequestBody CostumUserDTO costumUserDTO) throws URISyntaxException {
        log.debug("REST request to save CostumUser : {}", costumUserDTO);
        if (costumUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new costumUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostumUserDTO result = costumUserService.save(costumUserDTO);
        return ResponseEntity.created(new URI("/api/costum-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /costum-users : Updates an existing costumUser.
     *
     * @param costumUserDTO the costumUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated costumUserDTO,
     * or with status 400 (Bad Request) if the costumUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the costumUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/costum-users")
    @Timed
    public ResponseEntity<CostumUserDTO> updateCostumUser(@RequestBody CostumUserDTO costumUserDTO) throws URISyntaxException {
        log.debug("REST request to update CostumUser : {}", costumUserDTO);
        if (costumUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostumUserDTO result = costumUserService.save(costumUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, costumUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /costum-users : get all the costumUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of costumUsers in body
     */
    @GetMapping("/costum-users")
    @Timed
    public ResponseEntity<List<CostumUserDTO>> getAllCostumUsers(CostumUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CostumUsers by criteria: {}", criteria);
        Page<CostumUserDTO> page = costumUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/costum-users");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /costum-users/count : count all the costumUsers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/costum-users/count")
    @Timed
    public ResponseEntity<Long> countCostumUsers(CostumUserCriteria criteria) {
        log.debug("REST request to count CostumUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(costumUserQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /costum-users/:id : get the "id" costumUser.
     *
     * @param id the id of the costumUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costumUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/costum-users/{id}")
    @Timed
    public ResponseEntity<CostumUserDTO> getCostumUser(@PathVariable Long id) {
        log.debug("REST request to get CostumUser : {}", id);
        Optional<CostumUserDTO> costumUserDTO = costumUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costumUserDTO);
    }

    /**
     * DELETE  /costum-users/:id : delete the "id" costumUser.
     *
     * @param id the id of the costumUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/costum-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCostumUser(@PathVariable Long id) {
        log.debug("REST request to delete CostumUser : {}", id);
        costumUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
