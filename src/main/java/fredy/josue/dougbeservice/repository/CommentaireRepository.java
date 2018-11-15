package fredy.josue.dougbeservice.repository;

import fredy.josue.dougbeservice.domain.Commentaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Commentaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long>, JpaSpecificationExecutor<Commentaire> {

}
