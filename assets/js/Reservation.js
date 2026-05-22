public class Reservation {

    private String reservationId;
    private String customerId;
    private String roomId;
    private String checkinDate;
    private String checkoutDate;
    private String status;

    public Reservation(String reservationId, String customerId,
                       String roomId, String checkinDate,
                       String checkoutDate, String status) {

        this.reservationId = reservationId;
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return reservationId + "," +
                customerId + "," +
                roomId + "," +
                checkinDate + "," +
                checkoutDate + "," +
                status;
    }
}
