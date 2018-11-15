package fredy.josue.dougbeservice.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import fredy.josue.dougbeservice.domain.enumeration.TypeRencontre;

/**
 * A Preference.
 */
@Entity
@Table(name = "preference")
public class Preference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "taille")
    private String taille;

    @Column(name = "age")
    private String age;

    @Column(name = "pays")
    private String pays;

    @Column(name = "teint")
    private String teint;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_rencontre")
    private TypeRencontre typeRencontre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSexe() {
        return sexe;
    }

    public Preference sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTaille() {
        return taille;
    }

    public Preference taille(String taille) {
        this.taille = taille;
        return this;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getAge() {
        return age;
    }

    public Preference age(String age) {
        this.age = age;
        return this;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPays() {
        return pays;
    }

    public Preference pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTeint() {
        return teint;
    }

    public Preference teint(String teint) {
        this.teint = teint;
        return this;
    }

    public void setTeint(String teint) {
        this.teint = teint;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Preference createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Preference createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Preference lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Preference lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public TypeRencontre getTypeRencontre() {
        return typeRencontre;
    }

    public Preference typeRencontre(TypeRencontre typeRencontre) {
        this.typeRencontre = typeRencontre;
        return this;
    }

    public void setTypeRencontre(TypeRencontre typeRencontre) {
        this.typeRencontre = typeRencontre;
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
        Preference preference = (Preference) o;
        if (preference.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preference.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Preference{" +
            "id=" + getId() +
            ", sexe='" + getSexe() + "'" +
            ", taille='" + getTaille() + "'" +
            ", age='" + getAge() + "'" +
            ", pays='" + getPays() + "'" +
            ", teint='" + getTeint() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", typeRencontre='" + getTypeRencontre() + "'" +
            "}";
    }
}
