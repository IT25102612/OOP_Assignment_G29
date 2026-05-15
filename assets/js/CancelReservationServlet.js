public class CancelReservationServlet {

    public static void main(String[] args) {

        ReservationDAO dao = new ReservationDAO();

        dao.cancelReservation("R001");
    }
}
