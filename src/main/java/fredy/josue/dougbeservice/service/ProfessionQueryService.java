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

import fredy.josue.dougbeservice.domain.Profession;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.ProfessionRepository;
import fredy.josue.dougbeservice.service.dto.ProfessionCriteria;
import fredy.josue.dougbeservice.service.dto.ProfessionDTO;
import fredy.josue.dougbeservice.service.mapper.ProfessionMapper;

/**
 * Service for executing complex queries for Profession entities in the database.
 * The main input is a {@link ProfessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfessionDTO} or a {@link Page} of {@link ProfessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionQueryService extends QueryService<Profession> {

    private final Logger log = LoggerFactory.getLogger(ProfessionQueryService.class);

    private final ProfessionRepository professionRepository;

    private final ProfessionMapper professionMapper;

    public ProfessionQueryService(ProfessionRepository professionRepository, ProfessionMapper professionMapper) {
        this.professionRepository = professionRepository;
        this.professionMapper = professionMapper;
    }

    /**
     * Return a {@link List} of {@link ProfessionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionDTO> findByCriteria(ProfessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionMapper.toDto(professionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfessionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionDTO> findByCriteria(ProfessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionRepository.findAll(specification, page)
            .map(professionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionRepository.count(specification);
    }

    /**
     * Function to convert ProfessionCriteria to a {@link Specification}
     */
    private Specification<Profession> createSpecification(ProfessionCriteria criteria) {
        Specification<Profession> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Profession_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Profession_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Profession_.libelle));
            }
        }
        return specification;
    }
}
