package fredy.josue.dougbeservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import fredy.josue.dougbeservice.domain.enumeration.TypeRencontre;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Preference entity. This class is used in PreferenceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /preferences?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PreferenceCriteria implements Serializable {
    /**
     * Class for filtering TypeRencontre
     */
    public static class TypeRencontreFilter extends Filter<TypeRencontre> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sexe;

    private StringFilter taille;

    private StringFilter age;

    private StringFilter pays;

    private StringFilter teint;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private TypeRencontreFilter typeRencontre;

    public PreferenceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSexe() {
        return sexe;
    }

    public void setSexe(StringFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getTaille() {
        return taille;
    }

    public void setTaille(StringFilter taille) {
        this.taille = taille;
    }

    public StringFilter getAge() {
        return age;
    }

    public void setAge(StringFilter age) {
        this.age = age;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getTeint() {
        return teint;
    }

    public void setTeint(StringFilter teint) {
        this.teint = teint;
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

    public TypeRencontreFilter getTypeRencontre() {
        return typeRencontre;
    }

    public void setTypeRencontre(TypeRencontreFilter typeRencontre) {
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
        final PreferenceCriteria that = (PreferenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(taille, that.taille) &&
            Objects.equals(age, that.age) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(teint, that.teint) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(typeRencontre, that.typeRencontre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sexe,
        taille,
        age,
        pays,
        teint,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate,
        typeRencontre
        );
    }

    @Override
    public String toString() {
        return "PreferenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (taille != null ? "taille=" + taille + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (teint != null ? "teint=" + teint + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (typeRencontre != null ? "typeRencontre=" + typeRencontre + ", " : "") +
            "}";
    }

}
