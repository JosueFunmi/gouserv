package fredy.josue.dougbeservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.Teint;
import fredy.josue.dougbeservice.domain.enumeration.Sexe;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the CostumUser entity. This class is used in CostumUserResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /costum-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CostumUserCriteria implements Serializable {
    /**
     * Class for filtering Teint
     */
    public static class TeintFilter extends Filter<Teint> {
    }
    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TeintFilter teint;

    private DoubleFilter taille;

    private InstantFilter dateNais;

    private SexeFilter sexe;

    private LongFilter publicationId;

    private LongFilter villeId;

    private LongFilter professionId;

    public CostumUserCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TeintFilter getTeint() {
        return teint;
    }

    public void setTeint(TeintFilter teint) {
        this.teint = teint;
    }

    public DoubleFilter getTaille() {
        return taille;
    }

    public void setTaille(DoubleFilter taille) {
        this.taille = taille;
    }

    public InstantFilter getDateNais() {
        return dateNais;
    }

    public void setDateNais(InstantFilter dateNais) {
        this.dateNais = dateNais;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public LongFilter getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(LongFilter publicationId) {
        this.publicationId = publicationId;
    }

    public LongFilter getVilleId() {
        return villeId;
    }

    public void setVilleId(LongFilter villeId) {
        this.villeId = villeId;
    }

    public LongFilter getProfessionId() {
        return professionId;
    }

    public void setProfessionId(LongFilter professionId) {
        this.professionId = professionId;
    }


    @Override
    public String toString() {
        return "CostumUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (teint != null ? "teint=" + teint + ", " : "") +
                (taille != null ? "taille=" + taille + ", " : "") +
                (dateNais != null ? "dateNais=" + dateNais + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (publicationId != null ? "publicationId=" + publicationId + ", " : "") +
                (villeId != null ? "villeId=" + villeId + ", " : "") +
                (professionId != null ? "professionId=" + professionId + ", " : "") +
            "}";
    }

}
