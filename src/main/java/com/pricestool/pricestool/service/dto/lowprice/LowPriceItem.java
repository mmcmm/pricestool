package com.pricestool.pricestool.service.dto.lowprice;

import java.io.Serializable;

/**
 * A DTO for the opskins response item.
 */
public class LowPriceItem implements Serializable {

    private int price;

    private int quantity;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}