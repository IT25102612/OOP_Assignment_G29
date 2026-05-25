package com.tranquility.models;

public class Reservation {

    // Private fields — ENCAPSULATION
    private String reservationId;
    private String customerId;
    private String roomId;
    private String checkinDate;
    private String checkoutDate;
    private int nights;
    private String status;

    // Constructor
    public Reservation(String reservationId, String customerId, String roomId,
                       String checkinDate, String checkoutDate, int nights, String status) {
        this.reservationId = reservationId;
        this.customerId    = customerId;
        this.roomId        = roomId;
        this.checkinDate   = checkinDate;
        this.checkoutDate  = checkoutDate;
        this.nights        = nights;
        this.status        = status;
    }

    // Getters
    public String getReservationId() { return reservationId; }
    public String getCustomerId()    { return customerId; }
    public String getRoomId()        { return roomId; }
    public String getCheckinDate()   { return checkinDate; }
    public String getCheckoutDate()  { return checkoutDate; }
    public int    getNights()        { return nights; }
    public String getStatus()        { return status; }

    // Setters
    public void setRoomId(String roomId)           { this.roomId = roomId; }
    public void setCheckinDate(String checkinDate) { this.checkinDate = checkinDate; }
    public void setCheckoutDate(String d)          { this.checkoutDate = d; }
    public void setNights(int nights)              { this.nights = nights; }
    public void setStatus(String status)           { this.status = status; }

    // Save one reservation as one line in the txt file
    public String toFileString() {
        return reservationId + "," + customerId + "," + roomId + ","
                + checkinDate + "," + checkoutDate + "," + nights + "," + status;
    }

    // Read one line from the txt file back into an object
    public static Reservation fromFileString(String line) {
        String[] p = line.split(",");
        return new Reservation(p[0], p[1], p[2], p[3], p[4],
                Integer.parseInt(p[5]), p[6]);
    }
}