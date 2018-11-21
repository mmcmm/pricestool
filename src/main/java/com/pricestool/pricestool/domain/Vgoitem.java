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
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "category", length = 255, nullable = false)
    private String category;

    @NotNull
    @Size(max = 255)
    @Column(name = "rarity", length = 255, nullable = false)
    private String rarity;

    @NotNull
    @Size(max = 255)
    @Column(name = "jhi_type", length = 255, nullable = false)
    private String type;

    @Size(max = 20)
    @Column(name = "color", length = 20)
    private String color;

    @Size(max = 255)
    @Column(name = "image_300_px", length = 255)
    private String image300px;

    @Size(max = 255)
    @Column(name = "image_600_px", length = 255)
    private String image600px;

    @NotNull
    @Column(name = "suggested_price", nullable = false)
    private Integer suggestedPrice;

    @Column(name = "suggested_price_7_day")
    private Integer suggestedPrice7day;

    @Column(name = "suggested_price_30_day")
    private Integer suggestedPrice30day;

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

    public String getCategory() {
        return category;
    }

    public Vgoitem category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRarity() {
        return rarity;
    }

    public Vgoitem rarity(String rarity) {
        this.rarity = rarity;
        return this;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getType() {
        return type;
    }

    public Vgoitem type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public Vgoitem color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage300px() {
        return image300px;
    }

    public Vgoitem image300px(String image300px) {
        this.image300px = image300px;
        return this;
    }

    public void setImage300px(String image300px) {
        this.image300px = image300px;
    }

    public String getImage600px() {
        return image600px;
    }

    public Vgoitem image600px(String image600px) {
        this.image600px = image600px;
        return this;
    }

    public void setImage600px(String image600px) {
        this.image600px = image600px;
    }

    public Integer getSuggestedPrice() {
        return suggestedPrice;
    }

    public Vgoitem suggestedPrice(Integer suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
        return this;
    }

    public void setSuggestedPrice(Integer suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public Integer getSuggestedPrice7day() {
        return suggestedPrice7day;
    }

    public Vgoitem suggestedPrice7day(Integer suggestedPrice7day) {
        this.suggestedPrice7day = suggestedPrice7day;
        return this;
    }

    public void setSuggestedPrice7day(Integer suggestedPrice7day) {
        this.suggestedPrice7day = suggestedPrice7day;
    }

    public Integer getSuggestedPrice30day() {
        return suggestedPrice30day;
    }

    public Vgoitem suggestedPrice30day(Integer suggestedPrice30day) {
        this.suggestedPrice30day = suggestedPrice30day;
        return this;
    }

    public void setSuggestedPrice30day(Integer suggestedPrice30day) {
        this.suggestedPrice30day = suggestedPrice30day;
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
            ", category='" + getCategory() + "'" +
            ", rarity='" + getRarity() + "'" +
            ", type='" + getType() + "'" +
            ", color='" + getColor() + "'" +
            ", image300px='" + getImage300px() + "'" +
            ", image600px='" + getImage600px() + "'" +
            ", suggestedPrice=" + getSuggestedPrice() +
            ", suggestedPrice7day=" + getSuggestedPrice7day() +
            ", suggestedPrice30day=" + getSuggestedPrice30day() +
            "}";
    }
}
