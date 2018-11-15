package fredy.josue.dougbeservice.repository;

import fredy.josue.dougbeservice.domain.Profession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Profession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long>, JpaSpecificationExecutor<Profession> {

}
