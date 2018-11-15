package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.VilleService;
import fredy.josue.dougbeservice.domain.Ville;
import fredy.josue.dougbeservice.repository.VilleRepository;
import fredy.josue.dougbeservice.service.dto.VilleDTO;
import fredy.josue.dougbeservice.service.mapper.VilleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Ville.
 */
@Service
@Transactional
public class VilleServiceImpl implements VilleService {

    private final Logger log = LoggerFactory.getLogger(VilleServiceImpl.class);

    private final VilleRepository villeRepository;

    private final VilleMapper villeMapper;

    public VilleServiceImpl(VilleRepository villeRepository, VilleMapper villeMapper) {
        this.villeRepository = villeRepository;
        this.villeMapper = villeMapper;
    }

    /**
     * Save a ville.
     *
     * @param villeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VilleDTO save(VilleDTO villeDTO) {
        log.debug("Request to save Ville : {}", villeDTO);

        Ville ville = villeMapper.toEntity(villeDTO);
        ville = villeRepository.save(ville);
        return villeMapper.toDto(ville);
    }

    /**
     * Get all the villes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VilleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Villes");
        return villeRepository.findAll(pageable)
            .map(villeMapper::toDto);
    }


    /**
     * Get one ville by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VilleDTO> findOne(Long id) {
        log.debug("Request to get Ville : {}", id);
        return villeRepository.findById(id)
            .map(villeMapper::toDto);
    }

    /**
     * Delete the ville by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ville : {}", id);
        villeRepository.deleteById(id);
    }
}
