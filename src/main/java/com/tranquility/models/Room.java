package com.tranquility.models;

public class Room {

    private String  roomId;
    private String  suiteName;
    private double  pricePerNight;
    private int     beds;
    private int     baths;
    private String  bedType;
    private String  status;
    private String  building;
    private int     floor;
    private int     roomNumber;
    private String  country;
    private String  city;
    private String  balcony;
    private String  privatePool;
    private String  jointRooms;

    public Room() {}

    public Room(String roomId, String suiteName, double pricePerNight,
                int beds, int baths, String bedType, String status,
                String building, int floor, int roomNumber,
                String country, String city,
                String balcony, String privatePool, String jointRooms) {
        this.roomId        = roomId;
        this.suiteName     = suiteName;
        this.pricePerNight = pricePerNight;
        this.beds          = beds;
        this.baths         = baths;
        this.bedType       = bedType;
        this.status        = status;
        this.building      = building;
        this.floor         = floor;
        this.roomNumber    = roomNumber;
        this.country       = country;
        this.city          = city;
        this.balcony       = balcony;
        this.privatePool   = privatePool;
        this.jointRooms    = jointRooms;
    }

    public String getRoomId()               { return roomId; }
    public void   setRoomId(String v)       { this.roomId = v; }
    public String getSuiteName()            { return suiteName; }
    public void   setSuiteName(String v)    { this.suiteName = v; }
    public double getPricePerNight()        { return pricePerNight; }
    public void   setPricePerNight(double v){ this.pricePerNight = v; }
    public int    getBeds()                 { return beds; }
    public void   setBeds(int v)            { this.beds = v; }
    public int    getBaths()                { return baths; }
    public void   setBaths(int v)           { this.baths = v; }
    public String getBedType()              { return bedType; }
    public void   setBedType(String v)      { this.bedType = v; }
    public String getStatus()               { return status; }
    public void   setStatus(String v)       { this.status = v; }
    public String getBuilding()             { return building; }
    public void   setBuilding(String v)     { this.building = v; }
    public int    getFloor()                { return floor; }
    public void   setFloor(int v)           { this.floor = v; }
    public int    getRoomNumber()           { return roomNumber; }
    public void   setRoomNumber(int v)      { this.roomNumber = v; }
    public String getCountry()              { return country; }
    public void   setCountry(String v)      { this.country = v; }
    public String getCity()                 { return city; }
    public void   setCity(String v)         { this.city = v; }
    public String getBalcony()              { return balcony; }
    public void   setBalcony(String v)      { this.balcony = v; }
    public String getPrivatePool()          { return privatePool; }
    public void   setPrivatePool(String v)  { this.privatePool = v; }
    public String getJointRooms()           { return jointRooms; }
    public void   setJointRooms(String v)   { this.jointRooms = v; }

    public String toJson() {
        return "{"
                + "\"roomId\":"        + "\"" + esc(roomId)      + "\","
                + "\"suiteName\":"     + "\"" + esc(suiteName)   + "\","
                + "\"pricePerNight\":" + pricePerNight            + ","
                + "\"beds\":"          + beds                     + ","
                + "\"baths\":"         + baths                    + ","
                + "\"bedType\":"       + "\"" + esc(bedType)      + "\","
                + "\"status\":"        + "\"" + esc(status)       + "\","
                + "\"building\":"      + "\"" + esc(building)     + "\","
                + "\"floor\":"         + floor                    + ","
                + "\"roomNumber\":"    + roomNumber               + ","
                + "\"country\":"       + "\"" + esc(country)      + "\","
                + "\"city\":"          + "\"" + esc(city)         + "\","
                + "\"balcony\":"       + "\"" + esc(balcony)      + "\","
                + "\"privatePool\":"   + "\"" + esc(privatePool)  + "\","
                + "\"jointRooms\":"    + "\"" + esc(jointRooms)   + "\""
                + "}";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}