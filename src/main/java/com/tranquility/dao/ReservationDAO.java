package com.tranquility.dao;

import com.tranquility.models.Reservation;
import com.tranquility.structures.ReservationSorter;

import java.io.*;
import java.util.ArrayList;

// Handles all file read/write for reservations.txt
public class ReservationDAO {

    // Path to the data file — FILE HANDLING
    private static final String FILE = "reservations.txt";

    // ── READ: load all reservations from file ─────────────────────────────
    public ArrayList<Reservation> getAllReservations() {
        ArrayList<Reservation> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return list;          // file not created yet — return empty

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(Reservation.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }
        return list;
    }

    // ── READ + SORT: returns all reservations sorted by check-in date ──────
    public ArrayList<Reservation> getAllSorted() {
        ArrayList<Reservation> list = getAllReservations();
        if (list.size() > 1) {
            new ReservationSorter().quickSort(list, 0, list.size() - 1);
        }
        return list;
    }

    // ── READ: get one reservation by ID ────────────────────────────────────
    public Reservation getById(String reservationId) {
        for (Reservation r : getAllReservations()) {
            if (r.getReservationId().equals(reservationId)) return r;
        }
        return null;
    }

    // ── READ: get all reservations for one customer ────────────────────────
    public ArrayList<Reservation> getByCustomer(String customerId) {
        ArrayList<Reservation> result = new ArrayList<>();
        for (Reservation r : getAllReservations()) {
            if (r.getCustomerId().equals(customerId)) result.add(r);
        }
        return result;
    }

    // ── CREATE: append a new reservation to the file ───────────────────────
    public void addReservation(Reservation r) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(r.toFileString());
            bw.newLine();
        }
    }

    // ── UPDATE: find reservation by ID and overwrite it ────────────────────
    public boolean updateReservation(Reservation updated) throws IOException {
        ArrayList<Reservation> list = getAllReservations();
        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getReservationId().equals(updated.getReservationId())) {
                list.set(i, updated);
                found = true;
                break;
            }
        }

        if (found) writeAll(list);
        return found;
    }

    // ── DELETE (soft): mark reservation as "cancelled" ─────────────────────
    public boolean cancelReservation(String reservationId) throws IOException {
        ArrayList<Reservation> list = getAllReservations();
        boolean found = false;

        for (Reservation r : list) {
            if (r.getReservationId().equals(reservationId)) {
                r.setStatus("cancelled");
                found = true;
                break;
            }
        }

        if (found) writeAll(list);
        return found;
    }

    // ── AVAILABILITY CHECK ─────────────────────────────────────────────────
    public boolean isRoomAvailable(String roomId, String checkin, String checkout) {
        for (Reservation r : getAllReservations()) {
            if (r.getRoomId().equals(roomId) && !r.getStatus().equals("cancelled")) {
                // Overlap: new checkin is before existing checkout AND new checkout is after existing checkin
                if (checkin.compareTo(r.getCheckoutDate()) < 0 &&
                        checkout.compareTo(r.getCheckinDate()) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // ── ID GENERATOR ───────────────────────────────────────────────────────
    public String generateId() {
        return "RES" + System.currentTimeMillis();
    }

    // ── PRIVATE HELPER: rewrite entire file ───────────────────────────────
    private void writeAll(ArrayList<Reservation> list) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, false))) {
            for (Reservation r : list) {
                bw.write(r.toFileString());
                bw.newLine();
            }
        }
    }
}