package fredy.josue.dougbeservice.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fredy.josue.dougbeservice.domain.Commentaire;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.CommentaireRepository;
import fredy.josue.dougbeservice.service.dto.CommentaireCriteria;
import fredy.josue.dougbeservice.service.dto.CommentaireDTO;
import fredy.josue.dougbeservice.service.mapper.CommentaireMapper;

/**
 * Service for executing complex queries for Commentaire entities in the database.
 * The main input is a {@link CommentaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentaireDTO} or a {@link Page} of {@link CommentaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentaireQueryService extends QueryService<Commentaire> {

    private final Logger log = LoggerFactory.getLogger(CommentaireQueryService.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireQueryService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Return a {@link List} of {@link CommentaireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentaireDTO> findByCriteria(CommentaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireMapper.toDto(commentaireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentaireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentaireDTO> findByCriteria(CommentaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.findAll(specification, page)
            .map(commentaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.count(specification);
    }

    /**
     * Function to convert CommentaireCriteria to a {@link Specification}
     */
    private Specification<Commentaire> createSpecification(CommentaireCriteria criteria) {
        Specification<Commentaire> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Commentaire_.id));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Commentaire_.comment));
            }
            if (criteria.getJaime() != null) {
                specification = specification.and(buildSpecification(criteria.getJaime(), Commentaire_.jaime));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Commentaire_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Commentaire_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedBy(), Commentaire_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Commentaire_.lastModifiedDate));
            }
            if (criteria.getPublicationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPublicationId(), Commentaire_.publication, Publication_.id));
            }
        }
        return specification;
    }
}
