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

    public OpskinsItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}