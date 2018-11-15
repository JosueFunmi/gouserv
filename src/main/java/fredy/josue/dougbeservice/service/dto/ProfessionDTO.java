package fredy.josue.dougbeservice.service.dto;

import javax.validation.constraints.*;

import fredy.josue.dougbeservice.domain.CostumUser;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Profession entity.
 */
public class ProfessionDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String libelle;
    
    private Set<CostumUser> costumUsers = new HashSet<>();

    //private Long costumUserId;

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
    

    
    public Set<CostumUser> getCostumUsers() {
		return costumUsers;
	}

	public void setCostumUsers(Set<CostumUser> costumUsers) {
		this.costumUsers = costumUsers;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfessionDTO professionDTO = (ProfessionDTO) o;
        if (professionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), professionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfessionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
