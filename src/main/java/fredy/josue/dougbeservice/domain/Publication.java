package fredy.josue.dougbeservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fredy.josue.dougbeservice.domain.enumeration.QuiVoit;

/**
 * A Publication.
 */
@Entity
@Table(name = "publication")
public class Publication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pub")
    private String pub;

    @Column(name = "imageurl")
    private String imageurl;

    @Column(name = "nb_jaime")
    private Integer nbJaime;

    @Column(name = "nb_jaime_pas")
    private Integer nbJaimePas;

    @Enumerated(EnumType.STRING)
    @Column(name = "qui_voit")
    private QuiVoit quiVoit;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "publication")
    private Set<Commentaire> commentaires = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("publications")
    private CostumUser costumUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPub() {
        return pub;
    }

    public Publication pub(String pub) {
        this.pub = pub;
        return this;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getImageurl() {
        return imageurl;
    }

    public Publication imageurl(String imageurl) {
        this.imageurl = imageurl;
        return this;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Integer getNbJaime() {
        return nbJaime;
    }

    public Publication nbJaime(Integer nbJaime) {
        this.nbJaime = nbJaime;
        return this;
    }

    public void setNbJaime(Integer nbJaime) {
        this.nbJaime = nbJaime;
    }

    public Integer getNbJaimePas() {
        return nbJaimePas;
    }

    public Publication nbJaimePas(Integer nbJaimePas) {
        this.nbJaimePas = nbJaimePas;
        return this;
    }

    public void setNbJaimePas(Integer nbJaimePas) {
        this.nbJaimePas = nbJaimePas;
    }

    public QuiVoit getQuiVoit() {
        return quiVoit;
    }

    public Publication quiVoit(QuiVoit quiVoit) {
        this.quiVoit = quiVoit;
        return this;
    }

    public void setQuiVoit(QuiVoit quiVoit) {
        this.quiVoit = quiVoit;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Publication createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Publication createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Publication lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Publication lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Commentaire> getCommentaires() {
        return commentaires;
    }

    public Publication commentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
        return this;
    }

    public Publication addCommentaire(Commentaire commentaire) {
        this.commentaires.add(commentaire);
        commentaire.setPublication(this);
        return this;
    }

    public Publication removeCommentaire(Commentaire commentaire) {
        this.commentaires.remove(commentaire);
        commentaire.setPublication(null);
        return this;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public CostumUser getCostumUser() {
        return costumUser;
    }

    public Publication costumUser(CostumUser costumUser) {
        this.costumUser = costumUser;
        return this;
    }

    public void setCostumUser(CostumUser costumUser) {
        this.costumUser = costumUser;
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
        Publication publication = (Publication) o;
        if (publication.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publication.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Publication{" +
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
            "}";
    }
}
