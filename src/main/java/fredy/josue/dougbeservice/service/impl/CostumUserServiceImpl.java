package fredy.josue.dougbeservice.service.impl;

import fredy.josue.dougbeservice.service.CostumUserService;
import fredy.josue.dougbeservice.domain.CostumUser;
import fredy.josue.dougbeservice.repository.CostumUserRepository;
import fredy.josue.dougbeservice.service.dto.CostumUserDTO;
import fredy.josue.dougbeservice.service.mapper.CostumUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CostumUser.
 */
@Service
@Transactional
public class CostumUserServiceImpl implements CostumUserService {

    private final Logger log = LoggerFactory.getLogger(CostumUserServiceImpl.class);

    private final CostumUserRepository costumUserRepository;

    private final CostumUserMapper costumUserMapper;

    public CostumUserServiceImpl(CostumUserRepository costumUserRepository, CostumUserMapper costumUserMapper) {
        this.costumUserRepository = costumUserRepository;
        this.costumUserMapper = costumUserMapper;
    }

    /**
     * Save a costumUser.
     *
     * @param costumUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CostumUserDTO save(CostumUserDTO costumUserDTO) {
        log.debug("Request to save CostumUser : {}", costumUserDTO);

        CostumUser costumUser = costumUserMapper.toEntity(costumUserDTO);
        costumUser = costumUserRepository.save(costumUser);
        return costumUserMapper.toDto(costumUser);
    }

    /**
     * Get all the costumUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CostumUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CostumUsers");
        return costumUserRepository.findAll(pageable)
            .map(costumUserMapper::toDto);
    }


    /**
     * Get one costumUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CostumUserDTO> findOne(Long id) {
        log.debug("Request to get CostumUser : {}", id);
        return costumUserRepository.findById(id)
            .map(costumUserMapper::toDto);
    }

    /**
     * Delete the costumUser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CostumUser : {}", id);
        costumUserRepository.deleteById(id);
    }
}
