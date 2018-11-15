package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.PaysDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pays and its DTO PaysDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaysMapper extends EntityMapper<PaysDTO, Pays> {


    @Mapping(target = "villes", ignore = true)
    Pays toEntity(PaysDTO paysDTO);

    default Pays fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pays pays = new Pays();
        pays.setId(id);
        return pays;
    }
}
