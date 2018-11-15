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

import fredy.josue.dougbeservice.domain.Preference;
import fredy.josue.dougbeservice.domain.*; // for static metamodels
import fredy.josue.dougbeservice.repository.PreferenceRepository;
import fredy.josue.dougbeservice.service.dto.PreferenceCriteria;
import fredy.josue.dougbeservice.service.dto.PreferenceDTO;
import fredy.josue.dougbeservice.service.mapper.PreferenceMapper;

/**
 * Service for executing complex queries for Preference entities in the database.
 * The main input is a {@link PreferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PreferenceDTO} or a {@link Page} of {@link PreferenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PreferenceQueryService extends QueryService<Preference> {

    private final Logger log = LoggerFactory.getLogger(PreferenceQueryService.class);

    private final PreferenceRepository preferenceRepository;

    private final PreferenceMapper preferenceMapper;

    public PreferenceQueryService(PreferenceRepository preferenceRepository, PreferenceMapper preferenceMapper) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = preferenceMapper;
    }

    /**
     * Return a {@link List} of {@link PreferenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PreferenceDTO> findByCriteria(PreferenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceMapper.toDto(preferenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PreferenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PreferenceDTO> findByCriteria(PreferenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceRepository.findAll(specification, page)
            .map(preferenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreferenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceRepository.count(specification);
    }

    /**
     * Function to convert PreferenceCriteria to a {@link Specification}
     */
    private Specification<Preference> createSpecification(PreferenceCriteria criteria) {
        Specification<Preference> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Preference_.id));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexe(), Preference_.sexe));
            }
            if (criteria.getTaille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaille(), Preference_.taille));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge(), Preference_.age));
            }
            if (criteria.getPays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPays(), Preference_.pays));
            }
            if (criteria.getTeint() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeint(), Preference_.teint));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Preference_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Preference_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedBy(), Preference_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Preference_.lastModifiedDate));
            }
            if (criteria.getTypeRencontre() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeRencontre(), Preference_.typeRencontre));
            }
        }
        return specification;
    }
}
