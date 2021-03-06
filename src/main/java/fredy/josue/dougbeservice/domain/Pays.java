package fredy.josue.dougbeservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pays.
 */
@Entity
@Table(name = "pays")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "indice")
    private Integer indice;

    @OneToMany(mappedBy = "pays")
    private Set<Ville> villes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Pays code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Pays libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getIndice() {
        return indice;
    }

    public Pays indice(Integer indice) {
        this.indice = indice;
        return this;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public Set<Ville> getVilles() {
        return villes;
    }

    public Pays villes(Set<Ville> villes) {
        this.villes = villes;
        return this;
    }

    public Pays addVille(Ville ville) {
        this.villes.add(ville);
        ville.setPays(this);
        return this;
    }

    public Pays removeVille(Ville ville) {
        this.villes.remove(ville);
        ville.setPays(null);
        return this;
    }

    public void setVilles(Set<Ville> villes) {
        this.villes = villes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pays pays = (Pays) o;
        if (pays.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pays.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", indice=" + getIndice() +
            "}";
    }
}
