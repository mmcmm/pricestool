package com.pricestool.pricestool.service.dto.opdto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

/**
 * A DTO for the opskins response.
 */
public class OpskinsDTO implements Serializable {

    private int status;

    private long time;

    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, OpskinsItem>>>> response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, OpskinsItem>>>> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, HashMap<String, HashMap<String, HashMap<String, OpskinsItem>>>> response) {
        this.response = response;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long value) {
        this.time = value;
    }
}