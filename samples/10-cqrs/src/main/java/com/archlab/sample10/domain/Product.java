package com.archlab.sample10.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String sku;
    private BigDecimal unitPrice;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
