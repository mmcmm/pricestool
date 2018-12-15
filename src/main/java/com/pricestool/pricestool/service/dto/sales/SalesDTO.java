package com.pricestool.pricestool.service.dto.sales;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

/**
 * A DTO for the opskins response.
 */
public class SalesDTO implements Serializable {

    private long status;
    private long time;
    private SalesItem[] response;

    public long getStatus() { return status; }
    public void setStatus(long value) { this.status = value; }

    public long getTime() { return time; }
    public void setTime(long value) { this.time = value; }

    public SalesItem[] getResponse() { return response; }
    public void setResponse(SalesItem[] value) { this.response = value; }
}
 
