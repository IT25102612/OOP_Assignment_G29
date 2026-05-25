package com.tranquility.models;

public class Service extends BaseEntity {

    private String  name;
    private double  price;
    private String  category;
    private boolean available;

    public Service(String serviceId, String name, double price, String category, boolean available) {
        super(serviceId); 
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    @Override
    public String toJson() {
        return "{"
            + "\"serviceId\":\""  + esc(getId())  + "\","
            + "\"name\":\""       + esc(name)      + "\","
            + "\"price\":"        + price           + ","
            + "\"category\":\""   + esc(category)  + "\","
            + "\"available\":"    + available
            + "}";
    }

    @Override
    public String toString() {
        return "Service["
            + "id="       + getId()   + ", "
            + "name="     + name      + ", "
            + "price="    + price     + ", "
            + "category=" + category  + ", "
            + "available=" + available
            + "]";
    }

    private String esc(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    public String  getName() { return name; }
    public double  getPrice() { return price; }
    public String  getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setAvailable(boolean available){ this.available = available; }
}