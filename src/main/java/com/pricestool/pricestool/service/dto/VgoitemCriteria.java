package com.pricestool.pricestool.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Vgoitem entity. This class is used in VgoitemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /vgoitems?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VgoitemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter category;

    private StringFilter rarity;

    private StringFilter type;

    private StringFilter color;

    private StringFilter image300px;

    private StringFilter image600px;

    private IntegerFilter suggestedPrice;

    private IntegerFilter suggestedPrice7day;

    private IntegerFilter suggestedPrice30day;

    public VgoitemCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getRarity() {
        return rarity;
    }

    public void setRarity(StringFilter rarity) {
        this.rarity = rarity;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getColor() {
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getImage300px() {
        return image300px;
    }

    public void setImage300px(StringFilter image300px) {
        this.image300px = image300px;
    }

    public StringFilter getImage600px() {
        return image600px;
    }

    public void setImage600px(StringFilter image600px) {
        this.image600px = image600px;
    }

    public IntegerFilter getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(IntegerFilter suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public IntegerFilter getSuggestedPrice7day() {
        return suggestedPrice7day;
    }

    public void setSuggestedPrice7day(IntegerFilter suggestedPrice7day) {
        this.suggestedPrice7day = suggestedPrice7day;
    }

    public IntegerFilter getSuggestedPrice30day() {
        return suggestedPrice30day;
    }

    public void setSuggestedPrice30day(IntegerFilter suggestedPrice30day) {
        this.suggestedPrice30day = suggestedPrice30day;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VgoitemCriteria that = (VgoitemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(category, that.category) &&
            Objects.equals(rarity, that.rarity) &&
            Objects.equals(type, that.type) &&
            Objects.equals(color, that.color) &&
            Objects.equals(image300px, that.image300px) &&
            Objects.equals(image600px, that.image600px) &&
            Objects.equals(suggestedPrice, that.suggestedPrice) &&
            Objects.equals(suggestedPrice7day, that.suggestedPrice7day) &&
            Objects.equals(suggestedPrice30day, that.suggestedPrice30day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        category,
        rarity,
        type,
        color,
        image300px,
        image600px,
        suggestedPrice,
        suggestedPrice7day,
        suggestedPrice30day
        );
    }

    @Override
    public String toString() {
        return "VgoitemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (rarity != null ? "rarity=" + rarity + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (color != null ? "color=" + color + ", " : "") +
                (image300px != null ? "image300px=" + image300px + ", " : "") +
                (image600px != null ? "image600px=" + image600px + ", " : "") +
                (suggestedPrice != null ? "suggestedPrice=" + suggestedPrice + ", " : "") +
                (suggestedPrice7day != null ? "suggestedPrice7day=" + suggestedPrice7day + ", " : "") +
                (suggestedPrice30day != null ? "suggestedPrice30day=" + suggestedPrice30day + ", " : "") +
            "}";
    }

}
