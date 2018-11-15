package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.CommentaireService;
import fredy.josue.dougbeservice.domain.Commentaire;
import fredy.josue.dougbeservice.repository.CommentaireRepository;
import fredy.josue.dougbeservice.service.dto.CommentaireDTO;
import fredy.josue.dougbeservice.service.mapper.CommentaireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Commentaire.
 */
@Service
@Transactional
public class CommentaireServiceImpl implements CommentaireService {

    private final Logger log = LoggerFactory.getLogger(CommentaireServiceImpl.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Save a commentaire.
     *
     * @param commentaireDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CommentaireDTO save(CommentaireDTO commentaireDTO) {
        log.debug("Request to save Commentaire : {}", commentaireDTO);

        Commentaire commentaire = commentaireMapper.toEntity(commentaireDTO);
        commentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDto(commentaire);
    }

    /**
     * Get all the commentaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaires");
        return commentaireRepository.findAll(pageable)
            .map(commentaireMapper::toDto);
    }


    /**
     * Get one commentaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CommentaireDTO> findOne(Long id) {
        log.debug("Request to get Commentaire : {}", id);
        return commentaireRepository.findById(id)
            .map(commentaireMapper::toDto);
    }

    /**
     * Delete the commentaire by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commentaire : {}", id);
        commentaireRepository.deleteById(id);
    }
}
