package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.PreferenceService;
import fredy.josue.dougbeservice.domain.Preference;
import fredy.josue.dougbeservice.repository.PreferenceRepository;
import fredy.josue.dougbeservice.service.dto.PreferenceDTO;
import fredy.josue.dougbeservice.service.mapper.PreferenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Preference.
 */
@Service
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    private final Logger log = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    private final PreferenceRepository preferenceRepository;

    private final PreferenceMapper preferenceMapper;

    public PreferenceServiceImpl(PreferenceRepository preferenceRepository, PreferenceMapper preferenceMapper) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = preferenceMapper;
    }

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PreferenceDTO save(PreferenceDTO preferenceDTO) {
        log.debug("Request to save Preference : {}", preferenceDTO);

        Preference preference = preferenceMapper.toEntity(preferenceDTO);
        preference = preferenceRepository.save(preference);
        return preferenceMapper.toDto(preference);
    }

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Preferences");
        return preferenceRepository.findAll(pageable)
            .map(preferenceMapper::toDto);
    }


    /**
     * Get one preference by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PreferenceDTO> findOne(Long id) {
        log.debug("Request to get Preference : {}", id);
        return preferenceRepository.findById(id)
            .map(preferenceMapper::toDto);
    }

    /**
     * Delete the preference by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Preference : {}", id);
        preferenceRepository.deleteById(id);
    }
}
