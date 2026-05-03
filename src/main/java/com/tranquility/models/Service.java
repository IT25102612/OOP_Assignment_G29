package com.tranquility.models;

/**
 * Service.java
 * Represents one hotel service or facility.
 * Maps directly to one row in the services table.
 *
 * Examples of services:
 *   Breakfast, Spa Treatment, Airport Transfer, Laundry
 */
public class Service {

    private String  serviceId;   // e.g. SVC001
    private String  name;        // e.g. Breakfast
    private double  price;       // per person per night for meals,
                                 // flat fee for others
    private String  category;    // Meals | Wellness | Transport |
                                 // Housekeeping
    private boolean available;   // true = offered, false = removed

    // ---- Constructor ----
    public Service(String serviceId, String name, double price,
                   String category, boolean available) {
        this.serviceId = serviceId;
        this.name      = name;
        this.price     = price;
        this.category  = category;
        this.available = available;
    }

    /**
     * Converts this Service into a JSON string.
     * Called by ViewServicesServlet when building the response.
     *
     * Example output:
     * {
     *   "serviceId":  "SVC001",
     *   "name":       "Breakfast",
     *   "price":      35.0,
     *   "category":   "Meals",
     *   "available":  true
     * }
     */
    public String toJson() {
        return "{"
            + "\"serviceId\":\""  + esc(serviceId) + "\","
            + "\"name\":\""       + esc(name)       + "\","
            + "\"price\":"        + price            + ","
            + "\"category\":\""   + esc(category)   + "\","
            + "\"available\":"    + available
            + "}";
    }

    // Prevents broken JSON if name contains quote characters
    private String esc(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    // ---- Getters ----
    public String  getServiceId() { return serviceId; }
    public String  getName()      { return name; }
    public double  getPrice()     { return price; }
    public String  getCategory()  { return category; }
    public boolean isAvailable()  { return available; }

    // ---- Setters ----
    public void setName(String name)          { this.name = name; }
    public void setPrice(double price)        { this.price = price; }
    public void setCategory(String category)  { this.category = category; }
    public void setAvailable(boolean available){ this.available = available; }
}