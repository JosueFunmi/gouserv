package fredy.josue.dougbeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import fredy.josue.dougbeservice.service.CommentaireService;
import fredy.josue.dougbeservice.web.rest.errors.BadRequestAlertException;
import fredy.josue.dougbeservice.web.rest.util.HeaderUtil;
import fredy.josue.dougbeservice.web.rest.util.PaginationUtil;
import fredy.josue.dougbeservice.service.dto.CommentaireDTO;
import fredy.josue.dougbeservice.service.dto.CommentaireCriteria;
import fredy.josue.dougbeservice.service.CommentaireQueryService;
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
 * REST controller for managing Commentaire.
 */
@RestController
@RequestMapping("/api")
public class CommentaireResource {

    private final Logger log = LoggerFactory.getLogger(CommentaireResource.class);

    private static final String ENTITY_NAME = "dougbeServiceCommentaire";

    private final CommentaireService commentaireService;

    private final CommentaireQueryService commentaireQueryService;

    public CommentaireResource(CommentaireService commentaireService, CommentaireQueryService commentaireQueryService) {
        this.commentaireService = commentaireService;
        this.commentaireQueryService = commentaireQueryService;
    }

    /**
     * POST  /commentaires : Create a new commentaire.
     *
     * @param commentaireDTO the commentaireDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentaireDTO, or with status 400 (Bad Request) if the commentaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commentaires")
    @Timed
    public ResponseEntity<CommentaireDTO> createCommentaire(@Valid @RequestBody CommentaireDTO commentaireDTO) throws URISyntaxException {
        log.debug("REST request to save Commentaire : {}", commentaireDTO);
        if (commentaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentaireDTO result = commentaireService.save(commentaireDTO);
        return ResponseEntity.created(new URI("/api/commentaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentaires : Updates an existing commentaire.
     *
     * @param commentaireDTO the commentaireDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentaireDTO,
     * or with status 400 (Bad Request) if the commentaireDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentaireDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commentaires")
    @Timed
    public ResponseEntity<CommentaireDTO> updateCommentaire(@Valid @RequestBody CommentaireDTO commentaireDTO) throws URISyntaxException {
        log.debug("REST request to update Commentaire : {}", commentaireDTO);
        if (commentaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentaireDTO result = commentaireService.save(commentaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentaires : get all the commentaires.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of commentaires in body
     */
    @GetMapping("/commentaires")
    @Timed
    public ResponseEntity<List<CommentaireDTO>> getAllCommentaires(CommentaireCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Commentaires by criteria: {}", criteria);
        Page<CommentaireDTO> page = commentaireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaires");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /commentaires/count : count all the commentaires.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/commentaires/count")
    @Timed
    public ResponseEntity<Long> countCommentaires(CommentaireCriteria criteria) {
        log.debug("REST request to count Commentaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentaireQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /commentaires/:id : get the "id" commentaire.
     *
     * @param id the id of the commentaireDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentaireDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commentaires/{id}")
    @Timed
    public ResponseEntity<CommentaireDTO> getCommentaire(@PathVariable Long id) {
        log.debug("REST request to get Commentaire : {}", id);
        Optional<CommentaireDTO> commentaireDTO = commentaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentaireDTO);
    }

    /**
     * DELETE  /commentaires/:id : delete the "id" commentaire.
     *
     * @param id the id of the commentaireDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commentaires/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        log.debug("REST request to delete Commentaire : {}", id);
        commentaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
