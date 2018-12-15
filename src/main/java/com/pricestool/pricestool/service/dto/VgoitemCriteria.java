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

    private IntegerFilter op7day;

    private IntegerFilter op30day;

    private IntegerFilter sales;

    private IntegerFilter qty;

    private IntegerFilter minPrice;

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

    public IntegerFilter getOp7day() {
        return op7day;
    }

    public void setOp7day(IntegerFilter op7day) {
        this.op7day = op7day;
    }

    public IntegerFilter getOp30day() {
        return op30day;
    }

    public void setOp30day(IntegerFilter op30day) {
        this.op30day = op30day;
    }

    public IntegerFilter getSales() {
        return sales;
    }

    public void setSales(IntegerFilter sales) {
        this.sales = sales;
    }

    public IntegerFilter getQty() {
        return qty;
    }

    public void setQty(IntegerFilter qty) {
        this.qty = qty;
    }

    public IntegerFilter getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(IntegerFilter minPrice) {
        this.minPrice = minPrice;
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
            Objects.equals(op7day, that.op7day) &&
            Objects.equals(op30day, that.op30day) &&
            Objects.equals(sales, that.sales) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(minPrice, that.minPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        op7day,
        op30day,
        sales,
        qty,
        minPrice
        );
    }

    @Override
    public String toString() {
        return "VgoitemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (op7day != null ? "op7day=" + op7day + ", " : "") +
                (op30day != null ? "op30day=" + op30day + ", " : "") +
                (sales != null ? "sales=" + sales + ", " : "") +
                (qty != null ? "qty=" + qty + ", " : "") +
                (minPrice != null ? "minPrice=" + minPrice + ", " : "") +
            "}";
    }

}
