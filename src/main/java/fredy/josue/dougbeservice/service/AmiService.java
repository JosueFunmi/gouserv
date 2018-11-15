package fredy.josue.dougbeservice.service;

import fredy.josue.dougbeservice.service.dto.AmiDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Ami.
 */
public interface AmiService {

    /**
     * Save a ami.
     *
     * @param amiDTO the entity to save
     * @return the persisted entity
     */
    AmiDTO save(AmiDTO amiDTO);

    /**
     * Get all the amis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AmiDTO> findAll(Pageable pageable);


    /**
     * Get the "id" ami.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AmiDTO> findOne(Long id);

    /**
     * Delete the "id" ami.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
