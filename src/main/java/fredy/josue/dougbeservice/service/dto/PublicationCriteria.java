package fredy.josue.dougbeservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.QuiVoit;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Publication entity. This class is used in PublicationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /publications?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PublicationCriteria implements Serializable {
    /**
     * Class for filtering QuiVoit
     */
    public static class QuiVoitFilter extends Filter<QuiVoit> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pub;

    private StringFilter imageurl;

    private IntegerFilter nbJaime;

    private IntegerFilter nbJaimePas;

    private QuiVoitFilter quiVoit;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter commentaireId;

    private LongFilter costumUserId;

    public PublicationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPub() {
        return pub;
    }

    public void setPub(StringFilter pub) {
        this.pub = pub;
    }

    public StringFilter getImageurl() {
        return imageurl;
    }

    public void setImageurl(StringFilter imageurl) {
        this.imageurl = imageurl;
    }

    public IntegerFilter getNbJaime() {
        return nbJaime;
    }

    public void setNbJaime(IntegerFilter nbJaime) {
        this.nbJaime = nbJaime;
    }

    public IntegerFilter getNbJaimePas() {
        return nbJaimePas;
    }

    public void setNbJaimePas(IntegerFilter nbJaimePas) {
        this.nbJaimePas = nbJaimePas;
    }

    public QuiVoitFilter getQuiVoit() {
        return quiVoit;
    }

    public void setQuiVoit(QuiVoitFilter quiVoit) {
        this.quiVoit = quiVoit;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getCommentaireId() {
        return commentaireId;
    }

    public void setCommentaireId(LongFilter commentaireId) {
        this.commentaireId = commentaireId;
    }

    public LongFilter getCostumUserId() {
        return costumUserId;
    }

    public void setCostumUserId(LongFilter costumUserId) {
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
        final PublicationCriteria that = (PublicationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(pub, that.pub) &&
            Objects.equals(imageurl, that.imageurl) &&
            Objects.equals(nbJaime, that.nbJaime) &&
            Objects.equals(nbJaimePas, that.nbJaimePas) &&
            Objects.equals(quiVoit, that.quiVoit) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(commentaireId, that.commentaireId) &&
            Objects.equals(costumUserId, that.costumUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        pub,
        imageurl,
        nbJaime,
        nbJaimePas,
        quiVoit,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate,
        commentaireId,
        costumUserId
        );
    }

    @Override
    public String toString() {
        return "PublicationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pub != null ? "pub=" + pub + ", " : "") +
                (imageurl != null ? "imageurl=" + imageurl + ", " : "") +
                (nbJaime != null ? "nbJaime=" + nbJaime + ", " : "") +
                (nbJaimePas != null ? "nbJaimePas=" + nbJaimePas + ", " : "") +
                (quiVoit != null ? "quiVoit=" + quiVoit + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (commentaireId != null ? "commentaireId=" + commentaireId + ", " : "") +
                (costumUserId != null ? "costumUserId=" + costumUserId + ", " : "") +
            "}";
    }

}
