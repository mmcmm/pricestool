package com.pricestool.pricestool.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Vgoitem.
 */
@Entity
@Table(name = "vgoitem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vgoitem")
public class Vgoitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "op_7_day")
    private Integer op7day;

    @Column(name = "op_30_day")
    private Integer op30day;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Vgoitem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOp7day() {
        return op7day;
    }

    public Vgoitem op7day(Integer op7day) {
        this.op7day = op7day;
        return this;
    }

    public void setOp7day(Integer op7day) {
        this.op7day = op7day;
    }

    public Integer getOp30day() {
        return op30day;
    }

    public Vgoitem op30day(Integer op30day) {
        this.op30day = op30day;
        return this;
    }

    public void setOp30day(Integer op30day) {
        this.op30day = op30day;
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
        Vgoitem vgoitem = (Vgoitem) o;
        if (vgoitem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vgoitem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vgoitem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", op7day=" + getOp7day() +
            ", op30day=" + getOp30day() +
            "}";
    }
}
