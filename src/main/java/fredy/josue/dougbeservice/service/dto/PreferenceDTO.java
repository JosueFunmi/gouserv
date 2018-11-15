package fredy.josue.dougbeservice.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.TypeRencontre;

/**
 * A DTO for the Preference entity.
 */
public class PreferenceDTO implements Serializable {

    private Long id;

    private String sexe;

    private String taille;

    private String age;

    private String pays;

    private String teint;

    private Long createdBy;

    private Instant createdDate;

    private Long lastModifiedBy;

    private Instant lastModifiedDate;

    private TypeRencontre typeRencontre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTeint() {
        return teint;
    }

    public void setTeint(String teint) {
        this.teint = teint;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public TypeRencontre getTypeRencontre() {
        return typeRencontre;
    }

    public void setTypeRencontre(TypeRencontre typeRencontre) {
        this.typeRencontre = typeRencontre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferenceDTO preferenceDTO = (PreferenceDTO) o;
        if (preferenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreferenceDTO{" +
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
