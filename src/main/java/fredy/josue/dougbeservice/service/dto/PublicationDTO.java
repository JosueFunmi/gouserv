package fredy.josue.dougbeservice.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.QuiVoit;

/**
 * A DTO for the Publication entity.
 */
public class PublicationDTO implements Serializable {

    private Long id;

    private String pub;

    private String imageurl;

    private Integer nbJaime;

    private Integer nbJaimePas;

    private QuiVoit quiVoit;

    private Long createdBy;

    private Instant createdDate;

    private Long lastModifiedBy;

    private Instant lastModifiedDate;

    private Long costumUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Integer getNbJaime() {
        return nbJaime;
    }

    public void setNbJaime(Integer nbJaime) {
        this.nbJaime = nbJaime;
    }

    public Integer getNbJaimePas() {
        return nbJaimePas;
    }

    public void setNbJaimePas(Integer nbJaimePas) {
        this.nbJaimePas = nbJaimePas;
    }

    public QuiVoit getQuiVoit() {
        return quiVoit;
    }

    public void setQuiVoit(QuiVoit quiVoit) {
        this.quiVoit = quiVoit;
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

    public Long getCostumUserId() {
        return costumUserId;
    }

    public void setCostumUserId(Long costumUserId) {
        this.costumUserId = costumUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicationDTO publicationDTO = (PublicationDTO) o;
        if (publicationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publicationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PublicationDTO{" +
            "id=" + getId() +
            ", pub='" + getPub() + "'" +
            ", imageurl='" + getImageurl() + "'" +
            ", nbJaime=" + getNbJaime() +
            ", nbJaimePas=" + getNbJaimePas() +
            ", quiVoit='" + getQuiVoit() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", costumUser=" + getCostumUserId() +
            "}";
    }
}
