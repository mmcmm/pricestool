package com.pricestool.pricestool.service.dto.sales;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

/**
 * A DTO for the opskins response.
 */
public class SalesDTO implements Serializable {

    private int status;

    private SalesItem[] response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SalesItem[] getResponse() {
        return response;
    }

    public void setResponse(SalesItem[] response) {
        this.response = response;
    }
}