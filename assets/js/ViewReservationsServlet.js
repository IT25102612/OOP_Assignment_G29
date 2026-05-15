import java.util.ArrayList;

public class ViewReservationsServlet {

    public static void main(String[] args) {

        ReservationDAO dao = new ReservationDAO();

        ArrayList<Reservation> reservations =
                dao.getAllReservations();

        ReservationSorter sorter =
                new ReservationSorter();

        sorter.quickSort(
                reservations,
                0,
                reservations.size() - 1
        );

        for (Reservation r : reservations) {

            System.out.println(
                    r.getReservationId() + " " +
                    r.getCheckinDate()
            );
        }
    }
}
