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

import fredy.josue.dougbeservice.domain.Publication;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.PublicationRepository;
import fredy.josue.dougbeservice.service.dto.PublicationCriteria;
import fredy.josue.dougbeservice.service.dto.PublicationDTO;
import fredy.josue.dougbeservice.service.mapper.PublicationMapper;

/**
 * Service for executing complex queries for Publication entities in the database.
 * The main input is a {@link PublicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PublicationDTO} or a {@link Page} of {@link PublicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PublicationQueryService extends QueryService<Publication> {

    private final Logger log = LoggerFactory.getLogger(PublicationQueryService.class);

    private final PublicationRepository publicationRepository;

    private final PublicationMapper publicationMapper;

    public PublicationQueryService(PublicationRepository publicationRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.publicationMapper = publicationMapper;
    }

    /**
     * Return a {@link List} of {@link PublicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PublicationDTO> findByCriteria(PublicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Publication> specification = createSpecification(criteria);
        return publicationMapper.toDto(publicationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PublicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PublicationDTO> findByCriteria(PublicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Publication> specification = createSpecification(criteria);
        return publicationRepository.findAll(specification, page)
            .map(publicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PublicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Publication> specification = createSpecification(criteria);
        return publicationRepository.count(specification);
    }

    /**
     * Function to convert PublicationCriteria to a {@link Specification}
     */
    private Specification<Publication> createSpecification(PublicationCriteria criteria) {
        Specification<Publication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Publication_.id));
            }
            if (criteria.getPub() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPub(), Publication_.pub));
            }
            if (criteria.getImageurl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageurl(), Publication_.imageurl));
            }
            if (criteria.getNbJaime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbJaime(), Publication_.nbJaime));
            }
            if (criteria.getNbJaimePas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbJaimePas(), Publication_.nbJaimePas));
            }
            if (criteria.getQuiVoit() != null) {
                specification = specification.and(buildSpecification(criteria.getQuiVoit(), Publication_.quiVoit));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Publication_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Publication_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedBy(), Publication_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Publication_.lastModifiedDate));
            }
            if (criteria.getCommentaireId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCommentaireId(), Publication_.commentaires, Commentaire_.id));
            }
            if (criteria.getCostumUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCostumUserId(), Publication_.costumUser, CostumUser_.id));
            }
        }
        return specification;
    }
}
