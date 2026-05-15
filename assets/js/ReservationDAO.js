import java.io.*;
import java.util.ArrayList;

public class ReservationDAO {

    private static final String FILE_NAME = "reservations.txt";

    // CREATE
    public void addReservation(Reservation reservation) {

        try {
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(FILE_NAME, true));

            writer.write(reservation.toString());
            writer.newLine();

            writer.close();

            System.out.println("Reservation Added!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // READ
    public ArrayList<Reservation> getAllReservations() {

        ArrayList<Reservation> reservations = new ArrayList<>();

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Reservation reservation = new Reservation(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]
                );

                reservations.add(reservation);
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // UPDATE
    public void updateReservation(String reservationId,
                                  String newRoomId,
                                  String newCheckin,
                                  String newCheckout) {

        ArrayList<Reservation> reservations = getAllReservations();

        try {

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(FILE_NAME));

            for (Reservation r : reservations) {

                if (r.getReservationId().equals(reservationId)) {

                    r.setRoomId(newRoomId);
                    r.setCheckinDate(newCheckin);
                    r.setCheckoutDate(newCheckout);
                }

                writer.write(r.toString());
                writer.newLine();
            }

            writer.close();

            System.out.println("Reservation Updated!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void cancelReservation(String reservationId) {

        ArrayList<Reservation> reservations = getAllReservations();

        try {

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(FILE_NAME));

            for (Reservation r : reservations) {

                if (!r.getReservationId().equals(reservationId)) {

                    writer.write(r.toString());
                    writer.newLine();
                }
            }

            writer.close();

            System.out.println("Reservation Cancelled!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
