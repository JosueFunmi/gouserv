package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.PublicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Publication and its DTO PublicationDTO.
 */
@Mapper(componentModel = "spring", uses = {CostumUserMapper.class})
public interface PublicationMapper extends EntityMapper<PublicationDTO, Publication> {

    @Mapping(source = "costumUser.id", target = "costumUserId")
    PublicationDTO toDto(Publication publication);

    @Mapping(target = "commentaires", ignore = true)
    @Mapping(source = "costumUserId", target = "costumUser")
    Publication toEntity(PublicationDTO publicationDTO);

    default Publication fromId(Long id) {
        if (id == null) {
            return null;
        }
        Publication publication = new Publication();
        publication.setId(id);
        return publication;
    }
}
