package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.ProfessionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Profession and its DTO ProfessionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfessionMapper extends EntityMapper<ProfessionDTO, Profession> {

    //@Mapping(source = "costumUser.id", target = "costumUserId")
    //ProfessionDTO toDto(Profession profession);

    //@Mapping(source = "costumUserId", target = "costumUser")
    //Profession toEntity(ProfessionDTO professionDTO);

    default Profession fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profession profession = new Profession();
        profession.setId(id);
        return profession;
    }
}
