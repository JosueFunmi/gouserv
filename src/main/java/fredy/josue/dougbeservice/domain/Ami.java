package fredy.josue.dougbeservice.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Ami.
 */
@Entity
@Table(name = "ami")
public class Ami implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "interested", nullable = false)
    private Long interested;

    @NotNull
    @Column(name = "friend", nullable = false)
    private Long friend;

    @Column(name = "demande")
    private Boolean demande;

    @Column(name = "date_amis")
    private Instant dateAmis;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInterested() {
        return interested;
    }

    public Ami interested(Long interested) {
        this.interested = interested;
        return this;
    }

    public void setInterested(Long interested) {
        this.interested = interested;
    }

    public Long getFriend() {
        return friend;
    }

    public Ami friend(Long friend) {
        this.friend = friend;
        return this;
    }

    public void setFriend(Long friend) {
        this.friend = friend;
    }

    public Boolean isDemande() {
        return demande;
    }

    public Ami demande(Boolean demande) {
        this.demande = demande;
        return this;
    }

    public void setDemande(Boolean demande) {
        this.demande = demande;
    }

    public Instant getDateAmis() {
        return dateAmis;
    }

    public Ami dateAmis(Instant dateAmis) {
        this.dateAmis = dateAmis;
        return this;
    }

    public void setDateAmis(Instant dateAmis) {
        this.dateAmis = dateAmis;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Ami createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Ami createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Ami lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Ami lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        Ami ami = (Ami) o;
        if (ami.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ami.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ami{" +
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
