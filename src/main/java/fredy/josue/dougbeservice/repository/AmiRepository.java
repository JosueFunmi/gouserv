package fredy.josue.dougbeservice.repository;

import fredy.josue.dougbeservice.domain.Ami;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ami entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmiRepository extends JpaRepository<Ami, Long>, JpaSpecificationExecutor<Ami> {

}
