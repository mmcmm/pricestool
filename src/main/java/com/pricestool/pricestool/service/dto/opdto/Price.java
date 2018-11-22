package com.pricestool.pricestool.service.dto.opdto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {
    private int op7day;
    private int op30day;

    @JsonCreator
    public Price(@JsonProperty("op_7_day") int op7day, @JsonProperty("op_30_day") int op30day) {
        this.op7day = op7day;
        this.op30day = op30day;
    }

    public int getOp7Day() { return op7day; }
    public void setOp7Day(int value) { this.op7day = value; }

    public int getOp30Day() { return op30day; }
    public void setOp30Day(int value) { this.op30day = value; }
}