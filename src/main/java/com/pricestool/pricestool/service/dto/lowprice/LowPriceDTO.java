package com.pricestool.pricestool.service.dto.lowprice;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

/**
 * A DTO for the opskins response.
 */
public class LowPriceDTO implements Serializable {

    private int status;

    private HashMap<String, LowPriceItem> response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, LowPriceItem> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, LowPriceItem> response) {
        this.response = response;
    }
}