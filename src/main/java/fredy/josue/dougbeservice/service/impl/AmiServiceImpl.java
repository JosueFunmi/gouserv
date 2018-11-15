package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.AmiService;
import fredy.josue.dougbeservice.domain.Ami;
import fredy.josue.dougbeservice.repository.AmiRepository;
import fredy.josue.dougbeservice.service.dto.AmiDTO;
import fredy.josue.dougbeservice.service.mapper.AmiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Ami.
 */
@Service
@Transactional
public class AmiServiceImpl implements AmiService {

    private final Logger log = LoggerFactory.getLogger(AmiServiceImpl.class);

    private final AmiRepository amiRepository;

    private final AmiMapper amiMapper;

    public AmiServiceImpl(AmiRepository amiRepository, AmiMapper amiMapper) {
        this.amiRepository = amiRepository;
        this.amiMapper = amiMapper;
    }

    /**
     * Save a ami.
     *
     * @param amiDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AmiDTO save(AmiDTO amiDTO) {
        log.debug("Request to save Ami : {}", amiDTO);

        Ami ami = amiMapper.toEntity(amiDTO);
        ami = amiRepository.save(ami);
        return amiMapper.toDto(ami);
    }

    /**
     * Get all the amis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AmiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Amis");
        return amiRepository.findAll(pageable)
            .map(amiMapper::toDto);
    }


    /**
     * Get one ami by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AmiDTO> findOne(Long id) {
        log.debug("Request to get Ami : {}", id);
        return amiRepository.findById(id)
            .map(amiMapper::toDto);
    }

    /**
     * Delete the ami by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ami : {}", id);
        amiRepository.deleteById(id);
    }
}
