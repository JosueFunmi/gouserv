package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.AmiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ami and its DTO AmiDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AmiMapper extends EntityMapper<AmiDTO, Ami> {



    default Ami fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ami ami = new Ami();
        ami.setId(id);
        return ami;
    }
}
