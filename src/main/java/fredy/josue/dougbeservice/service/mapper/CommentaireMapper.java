package fredy.josue.dougbeservice.service.mapper;

import fredy.josue.dougbeservice.domain.*;
import fredy.josue.dougbeservice.service.dto.CommentaireDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Commentaire and its DTO CommentaireDTO.
 */
@Mapper(componentModel = "spring", uses = {PublicationMapper.class})
public interface CommentaireMapper extends EntityMapper<CommentaireDTO, Commentaire> {

    @Mapping(source = "publication.id", target = "publicationId")
    CommentaireDTO toDto(Commentaire commentaire);

    @Mapping(source = "publicationId", target = "publication")
    Commentaire toEntity(CommentaireDTO commentaireDTO);

    default Commentaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        return commentaire;
    }
}
