package com.pricestool.pricestool.service.dto.opdto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * A DTO for the opskins response item.
 */
public class OpskinsItem implements Serializable {

    private String name;
    private String category;
    private String rarity;
    private String type;
    private String color;
    private Map<String, String> image;
    private long suggestedPrice;

    public OpskinsItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String value) {
        this.category = value;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String value) {
        this.rarity = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String value) {
        this.color = value;
    }

    public Map<String, String> getImage() {
        return image;
    }

    public void setImage(Map<String, String> value) {
        this.image = value;
    }

    public long getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(long value) {
        this.suggestedPrice = value;
    }
}