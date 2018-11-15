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

import fredy.josue.dougbeservice.domain.Pays;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.PaysRepository;
import fredy.josue.dougbeservice.service.dto.PaysCriteria;
import fredy.josue.dougbeservice.service.dto.PaysDTO;
import fredy.josue.dougbeservice.service.mapper.PaysMapper;

/**
 * Service for executing complex queries for Pays entities in the database.
 * The main input is a {@link PaysCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaysDTO} or a {@link Page} of {@link PaysDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaysQueryService extends QueryService<Pays> {

    private final Logger log = LoggerFactory.getLogger(PaysQueryService.class);

    private final PaysRepository paysRepository;

    private final PaysMapper paysMapper;

    public PaysQueryService(PaysRepository paysRepository, PaysMapper paysMapper) {
        this.paysRepository = paysRepository;
        this.paysMapper = paysMapper;
    }

    /**
     * Return a {@link List} of {@link PaysDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaysDTO> findByCriteria(PaysCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pays> specification = createSpecification(criteria);
        return paysMapper.toDto(paysRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaysDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaysDTO> findByCriteria(PaysCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pays> specification = createSpecification(criteria);
        return paysRepository.findAll(specification, page)
            .map(paysMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaysCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pays> specification = createSpecification(criteria);
        return paysRepository.count(specification);
    }

    /**
     * Function to convert PaysCriteria to a {@link Specification}
     */
    private Specification<Pays> createSpecification(PaysCriteria criteria) {
        Specification<Pays> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Pays_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Pays_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Pays_.libelle));
            }
            if (criteria.getIndice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndice(), Pays_.indice));
            }
            if (criteria.getVilleId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVilleId(), Pays_.villes, Ville_.id));
            }
        }
        return specification;
    }
}
