package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.PreferenceService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.PreferenceDTO;
import fredy.josue.dougbeservice.service.dto.PreferenceCriteria;
import fredy.josue.dougbeservice.service.PreferenceQueryService;
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
 * REST controller for managing Preference.
 */
@RestController
@RequestMapping("/api")
public class PreferenceResource {

    private final Logger log = LoggerFactory.getLogger(PreferenceResource.class);

    private static final String ENTITY_NAME = "dougbeServicePreference";

    private final PreferenceService preferenceService;

    private final PreferenceQueryService preferenceQueryService;

    public PreferenceResource(PreferenceService preferenceService, PreferenceQueryService preferenceQueryService) {
        this.preferenceService = preferenceService;
        this.preferenceQueryService = preferenceQueryService;
    }

    /**
     * POST  /preferences : Create a new preference.
     *
     * @param preferenceDTO the preferenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preferenceDTO, or with status 400 (Bad Request) if the preference has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/preferences")
    @Timed
    public ResponseEntity<PreferenceDTO> createPreference(@RequestBody PreferenceDTO preferenceDTO) throws URISyntaxException {
        log.debug("REST request to save Preference : {}", preferenceDTO);
        if (preferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new preference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PreferenceDTO result = preferenceService.save(preferenceDTO);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferences : Updates an existing preference.
     *
     * @param preferenceDTO the preferenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preferenceDTO,
     * or with status 400 (Bad Request) if the preferenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the preferenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/preferences")
    @Timed
    public ResponseEntity<PreferenceDTO> updatePreference(@RequestBody PreferenceDTO preferenceDTO) throws URISyntaxException {
        log.debug("REST request to update Preference : {}", preferenceDTO);
        if (preferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PreferenceDTO result = preferenceService.save(preferenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preferenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences : get all the preferences.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of preferences in body
     */
    @GetMapping("/preferences")
    @Timed
    public ResponseEntity<List<PreferenceDTO>> getAllPreferences(PreferenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Preferences by criteria: {}", criteria);
        Page<PreferenceDTO> page = preferenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/preferences");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /preferences/count : count all the preferences.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/preferences/count")
    @Timed
    public ResponseEntity<Long> countPreferences(PreferenceCriteria criteria) {
        log.debug("REST request to count Preferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(preferenceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /preferences/:id : get the "id" preference.
     *
     * @param id the id of the preferenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preferenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<PreferenceDTO> getPreference(@PathVariable Long id) {
        log.debug("REST request to get Preference : {}", id);
        Optional<PreferenceDTO> preferenceDTO = preferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preferenceDTO);
    }

    /**
     * DELETE  /preferences/:id : delete the "id" preference.
     *
     * @param id the id of the preferenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        log.debug("REST request to delete Preference : {}", id);
        preferenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
