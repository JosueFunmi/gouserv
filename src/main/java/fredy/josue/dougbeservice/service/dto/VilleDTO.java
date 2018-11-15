package fredy.josue.dougbeservice.service.dto;

import javax.validation.constraints.*;

import fredy.josue.dougbeservice.domain.Pays;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ville entity.
 */
public class VilleDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String libelle;

    private Long paysId;
    
    private Pays pays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getPaysId() {
        return paysId;
    }

    public void setPaysId(Long paysId) {
        this.paysId = paysId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VilleDTO villeDTO = (VilleDTO) o;
        if (villeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), villeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VilleDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", pays=" + getPaysId() +
            "}";
    }
}
