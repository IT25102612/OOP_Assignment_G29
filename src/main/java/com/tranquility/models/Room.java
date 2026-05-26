package com.tranquility.models;

public class Room {

    private String roomId;
    private String suiteName;
    private double pricePerNight;
    private int beds;
    private int baths;
    private String bedType;
    private String status;
    private String building;
    private int floor;
    private int roomNumber;
    private String country;
    private String city;
    private String balcony;
    private String privatePool;
    private String jointRooms;

    public Room() {
    }

    public Room(String roomId, String suiteName, double pricePerNight,
                int beds, int baths, String bedType, String status,
                String building, int floor, int roomNumber,
                String country, String city,
                String balcony, String privatePool, String jointRooms) {
        this.roomId = roomId;
        this.suiteName = suiteName;
        this.pricePerNight = pricePerNight;
        this.beds = beds;
        this.baths = baths;
        this.bedType = bedType;
        this.status = status;
        this.building = building;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.country = country;
        this.city = city;
        this.balcony = balcony;
        this.privatePool = privatePool;
        this.jointRooms = jointRooms;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String v) {
        this.roomId = v;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String v) {
        this.suiteName = v;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double v) {
        this.pricePerNight = v;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int v) {
        this.beds = v;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int v) {
        this.baths = v;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String v) {
        this.bedType = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        this.status = v;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String v) {
        this.building = v;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int v) {
        this.floor = v;
    }

    ;

}
