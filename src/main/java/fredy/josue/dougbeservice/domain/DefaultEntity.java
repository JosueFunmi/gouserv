package fredy.josue.dougbeservice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import fredy.josue.dougbeservice.util.ResultInfo;






@Entity
@SqlResultSetMappings({

@SqlResultSetMapping(name="ResultInfo",
classes = {
    @ConstructorResult(

            targetClass = ResultInfo.class,
            columns = {
                @ColumnResult(name = "resultat",  type = String.class)
            })
}),

})


public class DefaultEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
}
