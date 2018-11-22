package com.pricestool.pricestool.service.dto.opdto;

import java.io.Serializable;
import java.util.Map;

/**
 * A DTO for the opskins response.
 */
public class OpskinsDTO implements Serializable {

    private String note;
    private long status;
    private long time;
    private Map<String, Price> response;

    public String getNote() {
        return note;
    }

    public void setNote(String value) {
        this.note = value;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long value) {
        this.status = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long value) {
        this.time = value;
    }

    public Map<String, Price> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Price> value) {
        this.response = value;
    }
}
