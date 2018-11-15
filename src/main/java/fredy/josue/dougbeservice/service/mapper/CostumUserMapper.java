package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.CostumUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CostumUser and its DTO CostumUserDTO.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class, ProfessionMapper.class})
public interface CostumUserMapper extends EntityMapper<CostumUserDTO, CostumUser> {

    @Mapping(source = "ville.id", target = "villeId")
    @Mapping(source = "profesion.id", target = "professionId")
    CostumUserDTO toDto(CostumUser costumUser);

    @Mapping(target = "publications", ignore = true)
    @Mapping(source = "villeId", target = "ville")
    @Mapping(source = "professionId", target = "profession")
    CostumUser toEntity(CostumUserDTO costumUserDTO);

    default CostumUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        CostumUser costumUser = new CostumUser();
        costumUser.setId(id);
        return costumUser;
    }
}
