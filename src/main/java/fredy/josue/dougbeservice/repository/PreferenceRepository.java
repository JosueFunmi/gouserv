package fredy.josue.dougbeservice.repository;

import fredy.josue.dougbeservice.domain.Preference;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Preference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long>, JpaSpecificationExecutor<Preference> {

}
