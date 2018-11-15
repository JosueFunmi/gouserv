package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.PublicationService;
import fredy.josue.dougbeservice.domain.Publication;
import fredy.josue.dougbeservice.repository.PublicationRepository;
import fredy.josue.dougbeservice.service.dto.PublicationDTO;
import fredy.josue.dougbeservice.service.mapper.PublicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Publication.
 */
@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {

    private final Logger log = LoggerFactory.getLogger(PublicationServiceImpl.class);

    private final PublicationRepository publicationRepository;

    private final PublicationMapper publicationMapper;

    public PublicationServiceImpl(PublicationRepository publicationRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.publicationMapper = publicationMapper;
    }

    /**
     * Save a publication.
     *
     * @param publicationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PublicationDTO save(PublicationDTO publicationDTO) {
        log.debug("Request to save Publication : {}", publicationDTO);

        Publication publication = publicationMapper.toEntity(publicationDTO);
        publication = publicationRepository.save(publication);
        return publicationMapper.toDto(publication);
    }

    /**
     * Get all the publications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PublicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Publications");
        return publicationRepository.findAll(pageable)
            .map(publicationMapper::toDto);
    }


    /**
     * Get one publication by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PublicationDTO> findOne(Long id) {
        log.debug("Request to get Publication : {}", id);
        return publicationRepository.findById(id)
            .map(publicationMapper::toDto);
    }

    /**
     * Delete the publication by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Publication : {}", id);
        publicationRepository.deleteById(id);
    }
}
