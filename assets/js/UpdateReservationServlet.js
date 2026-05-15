public class UpdateReservationServlet {

    public static void main(String[] args) {

        ReservationDAO dao = new ReservationDAO();

        dao.updateReservation(
                "R001",
                "RM500",
                "2026-06-01",
                "2026-06-04"
        );
    }
}
