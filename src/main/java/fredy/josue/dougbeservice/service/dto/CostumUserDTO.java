package fredy.josue.dougbeservice.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.Teint;
import fredy.josue.dougbeservice.domain.Profession;
import fredy.josue.dougbeservice.domain.Ville;
import fredy.josue.dougbeservice.domain.enumeration.Sexe;

/**
 * A DTO for the CostumUser entity.
 */
public class CostumUserDTO implements Serializable {

    private Long id;

    private Teint teint;

    private Double taille;

    private Instant dateNais;

    private Sexe sexe;

    private Long villeId;
    
    private Ville ville;
    
    private Long profesionId;
    
    private Profession profession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teint getTeint() {
        return teint;
    }

    public void setTeint(Teint teint) {
        this.teint = teint;
    }

    public Double getTaille() {
        return taille;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public Instant getDateNais() {
        return dateNais;
    }

    public void setDateNais(Instant dateNais) {
        this.dateNais = dateNais;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Long getVilleId() {
        return villeId;
    }

    public void setVilleId(Long villeId) {
        this.villeId = villeId;
    }

    
    public Ville getVille() {
		return ville;
	}

	public void setVille(Ville ville) {
		this.ville = ville;
	}

	public Long getProfesionId() {
		return profesionId;
	}

	public void setProfesionId(Long profesionId) {
		this.profesionId = profesionId;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CostumUserDTO costumUserDTO = (CostumUserDTO) o;
        if (costumUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), costumUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CostumUserDTO{" +
            "id=" + getId() +
            ", teint='" + getTeint() + "'" +
            ", taille=" + getTaille() +
            ", dateNais='" + getDateNais() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", ville=" + getVilleId() +
            "}";
    }
}
