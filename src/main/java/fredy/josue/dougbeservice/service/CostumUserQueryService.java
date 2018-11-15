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

import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.CostumUserRepository;
import fredy.josue.dougbeservice.service.dto.CostumUserCriteria;
import fredy.josue.dougbeservice.service.dto.CostumUserDTO;
import fredy.josue.dougbeservice.service.mapper.CostumUserMapper;

/**
 * Service for executing complex queries for CostumUser entities in the database.
 * The main input is a {@link CostumUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CostumUserDTO} or a {@link Page} of {@link CostumUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CostumUserQueryService extends QueryService<CostumUser> {

    private final Logger log = LoggerFactory.getLogger(CostumUserQueryService.class);

    private final CostumUserRepository costumUserRepository;

    private final CostumUserMapper costumUserMapper;

    public CostumUserQueryService(CostumUserRepository costumUserRepository, CostumUserMapper costumUserMapper) {
        this.costumUserRepository = costumUserRepository;
        this.costumUserMapper = costumUserMapper;
    }

    /**
     * Return a {@link List} of {@link CostumUserDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CostumUserDTO> findByCriteria(CostumUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CostumUser> specification = createSpecification(criteria);
        return costumUserMapper.toDto(costumUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CostumUserDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CostumUserDTO> findByCriteria(CostumUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CostumUser> specification = createSpecification(criteria);
        return costumUserRepository.findAll(specification, page)
            .map(costumUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CostumUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CostumUser> specification = createSpecification(criteria);
        return costumUserRepository.count(specification);
    }

    /**
     * Function to convert CostumUserCriteria to a {@link Specification}
     */
    private Specification<CostumUser> createSpecification(CostumUserCriteria criteria) {
        Specification<CostumUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CostumUser_.id));
            }
            if (criteria.getTeint() != null) {
                specification = specification.and(buildSpecification(criteria.getTeint(), CostumUser_.teint));
            }
            if (criteria.getTaille() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaille(), CostumUser_.taille));
            }
            if (criteria.getDateNais() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNais(), CostumUser_.dateNais));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), CostumUser_.sexe));
            }
            if (criteria.getPublicationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPublicationId(), CostumUser_.publications, Publication_.id));
              
            }
            if (criteria.getVilleId() != null) {
            	specification = specification.and(buildReferringEntitySpecification(criteria.getVilleId(), CostumUser_.ville, Ville_.id));
                //specification = specification.and(buildSpecification(criteria.getVilleId(),
                   // root -> root.join(CostumUser_.ville, JoinType.LEFT).get(Ville_.id)));
            }
            if (criteria.getProfessionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfessionId(), CostumUser_.profession, Profession_.id));;
            }
        }
        return specification;
    }
}
