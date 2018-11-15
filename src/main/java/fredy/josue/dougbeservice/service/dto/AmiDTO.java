package fredy.josue.dougbeservice.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ami entity.
 */
public class AmiDTO implements Serializable {

    private Long id;

    @NotNull
    private Long interested;

    @NotNull
    private Long friend;

    private Boolean demande;

    private Instant dateAmis;

    private Long createdBy;

    private Instant createdDate;

    private Long lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInterested() {
        return interested;
    }

    public void setInterested(Long interested) {
        this.interested = interested;
    }

    public Long getFriend() {
        return friend;
    }

    public void setFriend(Long friend) {
        this.friend = friend;
    }

    public Boolean isDemande() {
        return demande;
    }

    public void setDemande(Boolean demande) {
        this.demande = demande;
    }

    public Instant getDateAmis() {
        return dateAmis;
    }

    public void setDateAmis(Instant dateAmis) {
        this.dateAmis = dateAmis;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AmiDTO amiDTO = (AmiDTO) o;
        if (amiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AmiDTO{" +
            "id=" + getId() +
            ", interested=" + getInterested() +
            ", friend=" + getFriend() +
            ", demande='" + isDemande() + "'" +
            ", dateAmis='" + getDateAmis() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
