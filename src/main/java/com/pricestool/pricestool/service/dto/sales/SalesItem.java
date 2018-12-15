package com.pricestool.pricestool.service.dto.sales;

import java.io.Serializable;

public class SalesItem implements Serializable {
    private long id;
    private int amount;
    private double wear;
    private long timestamp;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public int getAmount() { return amount; }
    public void setAmount(int value) { this.amount = value; }

    public double getWear() { return wear; }
    public void setWear(double value) { this.wear = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long value) { this.timestamp = value; }
}
