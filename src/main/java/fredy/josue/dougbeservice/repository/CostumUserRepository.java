package fredy.josue.dougbeservice.repository;

import fredy.josue.dougbeservice.domain.CostumUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CostumUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostumUserRepository extends JpaRepository<CostumUser, Long>, JpaSpecificationExecutor<CostumUser> {

}
