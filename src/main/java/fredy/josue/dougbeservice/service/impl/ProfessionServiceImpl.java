package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.ProfessionService;
import fredy.josue.dougbeservice.domain.Profession;
import fredy.josue.dougbeservice.repository.ProfessionRepository;
import fredy.josue.dougbeservice.service.dto.ProfessionDTO;
import fredy.josue.dougbeservice.service.mapper.ProfessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Profession.
 */
@Service
@Transactional
public class ProfessionServiceImpl implements ProfessionService {

    private final Logger log = LoggerFactory.getLogger(ProfessionServiceImpl.class);

    private final ProfessionRepository professionRepository;

    private final ProfessionMapper professionMapper;

    public ProfessionServiceImpl(ProfessionRepository professionRepository, ProfessionMapper professionMapper) {
        this.professionRepository = professionRepository;
        this.professionMapper = professionMapper;
    }

    /**
     * Save a profession.
     *
     * @param professionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProfessionDTO save(ProfessionDTO professionDTO) {
        log.debug("Request to save Profession : {}", professionDTO);

        Profession profession = professionMapper.toEntity(professionDTO);
        profession = professionRepository.save(profession);
        return professionMapper.toDto(profession);
    }

    /**
     * Get all the professions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Professions");
        return professionRepository.findAll(pageable)
            .map(professionMapper::toDto);
    }


    /**
     * Get one profession by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionDTO> findOne(Long id) {
        log.debug("Request to get Profession : {}", id);
        return professionRepository.findById(id)
            .map(professionMapper::toDto);
    }

    /**
     * Delete the profession by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profession : {}", id);
        professionRepository.deleteById(id);
    }
}
