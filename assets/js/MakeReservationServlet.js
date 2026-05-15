public class MakeReservationServlet {

    public static void main(String[] args) {

        Reservation reservation =
                new Reservation(
                        "R001",
                        "C001",
                        "RM101",
                        "2026-05-20",
                        "2026-05-25",
                        "ACTIVE"
                );

        ReservationDAO dao = new ReservationDAO();

        dao.addReservation(reservation);
    }
}
