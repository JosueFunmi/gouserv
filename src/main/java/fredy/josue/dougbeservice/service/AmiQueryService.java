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

import fredy.josue.dougbeservice.domain.Ami;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.AmiRepository;
import fredy.josue.dougbeservice.service.dto.AmiCriteria;
import fredy.josue.dougbeservice.service.dto.AmiDTO;
import fredy.josue.dougbeservice.service.mapper.AmiMapper;

/**
 * Service for executing complex queries for Ami entities in the database.
 * The main input is a {@link AmiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AmiDTO} or a {@link Page} of {@link AmiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AmiQueryService extends QueryService<Ami> {

    private final Logger log = LoggerFactory.getLogger(AmiQueryService.class);

    private final AmiRepository amiRepository;

    private final AmiMapper amiMapper;

    public AmiQueryService(AmiRepository amiRepository, AmiMapper amiMapper) {
        this.amiRepository = amiRepository;
        this.amiMapper = amiMapper;
    }

    /**
     * Return a {@link List} of {@link AmiDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AmiDTO> findByCriteria(AmiCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ami> specification = createSpecification(criteria);
        return amiMapper.toDto(amiRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AmiDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AmiDTO> findByCriteria(AmiCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ami> specification = createSpecification(criteria);
        return amiRepository.findAll(specification, page)
            .map(amiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AmiCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ami> specification = createSpecification(criteria);
        return amiRepository.count(specification);
    }

    /**
     * Function to convert AmiCriteria to a {@link Specification}
     */
    private Specification<Ami> createSpecification(AmiCriteria criteria) {
        Specification<Ami> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ami_.id));
            }
            if (criteria.getInterested() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInterested(), Ami_.interested));
            }
            if (criteria.getFriend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFriend(), Ami_.friend));
            }
            if (criteria.getDemande() != null) {
                specification = specification.and(buildSpecification(criteria.getDemande(), Ami_.demande));
            }
            if (criteria.getDateAmis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAmis(), Ami_.dateAmis));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Ami_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Ami_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedBy(), Ami_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Ami_.lastModifiedDate));
            }
        }
        return specification;
    }
}
