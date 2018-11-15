package fredy.josue.dougbeservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fredy.josue.dougbeservice.domain.enumeration.Teint;

import fredy.josue.dougbeservice.domain.enumeration.Sexe;

/**
 * A CostumUser.
 */
@Entity
@Table(name = "costum_user")
public class CostumUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "teint")
    private Teint teint;

    @Column(name = "taille")
    private Double taille;

    @Column(name = "date_nais")
    private Instant dateNais;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @OneToMany(mappedBy = "costumUser")
    private Set<Publication> publications = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("costumUsers")
    private Ville ville;
    
    @ManyToOne
    @JsonIgnoreProperties("costumUser")
    private Profession profession;

    
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teint getTeint() {
        return teint;
    }

    public CostumUser teint(Teint teint) {
        this.teint = teint;
        return this;
    }

    public void setTeint(Teint teint) {
        this.teint = teint;
    }

    public Double getTaille() {
        return taille;
    }

    public CostumUser taille(Double taille) {
        this.taille = taille;
        return this;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public Instant getDateNais() {
        return dateNais;
    }

    public CostumUser dateNais(Instant dateNais) {
        this.dateNais = dateNais;
        return this;
    }

    public void setDateNais(Instant dateNais) {
        this.dateNais = dateNais;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public CostumUser sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public CostumUser publications(Set<Publication> publications) {
        this.publications = publications;
        return this;
    }

    public CostumUser addPublication(Publication publication) {
        this.publications.add(publication);
        publication.setCostumUser(this);
        return this;
    }

    public CostumUser removePublication(Publication publication) {
        this.publications.remove(publication);
        publication.setCostumUser(null);
        return this;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Ville getVille() {
        return ville;
    }

    public CostumUser ville(Ville ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

   
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        CostumUser costumUser = (CostumUser) o;
        if (costumUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), costumUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CostumUser{" +
            "id=" + getId() +
            ", teint='" + getTeint() + "'" +
            ", taille=" + getTaille() +
            ", dateNais='" + getDateNais() + "'" +
            ", sexe='" + getSexe() + "'" +
            "}";
    }
}
