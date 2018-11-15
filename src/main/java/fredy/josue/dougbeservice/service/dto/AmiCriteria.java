package fredy.josue.dougbeservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Ami entity. This class is used in AmiResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /amis?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AmiCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter interested;

    private LongFilter friend;

    private BooleanFilter demande;

    private InstantFilter dateAmis;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public AmiCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getInterested() {
        return interested;
    }

    public void setInterested(LongFilter interested) {
        this.interested = interested;
    }

    public LongFilter getFriend() {
        return friend;
    }

    public void setFriend(LongFilter friend) {
        this.friend = friend;
    }

    public BooleanFilter getDemande() {
        return demande;
    }

    public void setDemande(BooleanFilter demande) {
        this.demande = demande;
    }

    public InstantFilter getDateAmis() {
        return dateAmis;
    }

    public void setDateAmis(InstantFilter dateAmis) {
        this.dateAmis = dateAmis;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AmiCriteria that = (AmiCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(interested, that.interested) &&
            Objects.equals(friend, that.friend) &&
            Objects.equals(demande, that.demande) &&
            Objects.equals(dateAmis, that.dateAmis) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        interested,
        friend,
        demande,
        dateAmis,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate
        );
    }

    @Override
    public String toString() {
        return "AmiCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (interested != null ? "interested=" + interested + ", " : "") +
                (friend != null ? "friend=" + friend + ", " : "") +
                (demande != null ? "demande=" + demande + ", " : "") +
                (dateAmis != null ? "dateAmis=" + dateAmis + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            "}";
    }

}
