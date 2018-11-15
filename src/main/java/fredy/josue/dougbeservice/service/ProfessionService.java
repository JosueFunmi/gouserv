package fredy.josue.dougbeservice.service;

import fredy.josue.dougbeservice.service.dto.ProfessionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Profession.
 */
public interface ProfessionService {

    /**
     * Save a profession.
     *
     * @param professionDTO the entity to save
     * @return the persisted entity
     */
    ProfessionDTO save(ProfessionDTO professionDTO);

    /**
     * Get all the professions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProfessionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" profession.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProfessionDTO> findOne(Long id);

    /**
     * Delete the "id" profession.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
