package fredy.josue.dougbeservice.service;

import fredy.josue.dougbeservice.service.dto.PublicationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Publication.
 */
public interface PublicationService {

    /**
     * Save a publication.
     *
     * @param publicationDTO the entity to save
     * @return the persisted entity
     */
    PublicationDTO save(PublicationDTO publicationDTO);

    /**
     * Get all the publications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PublicationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" publication.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PublicationDTO> findOne(Long id);

    /**
     * Delete the "id" publication.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
