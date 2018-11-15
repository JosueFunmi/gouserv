package fredy.josue.dougbeservice.service;

import fredy.josue.dougbeservice.service.dto.CostumUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CostumUser.
 */
public interface CostumUserService {

    /**
     * Save a costumUser.
     *
     * @param costumUserDTO the entity to save
     * @return the persisted entity
     */
    CostumUserDTO save(CostumUserDTO costumUserDTO);

    /**
     * Get all the costumUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CostumUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" costumUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CostumUserDTO> findOne(Long id);

    /**
     * Delete the "id" costumUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
