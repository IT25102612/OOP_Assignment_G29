package com.tranquility.servlets;

import com.tranquility.dao.ReservationDAO;
import com.tranquility.models.Reservation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;

@WebServlet("/ViewReservationsServlet")
public class ViewReservationsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");

        ReservationDAO dao     = new ReservationDAO();
        String reservationId   = req.getParameter("reservationId");
        String customerId      = req.getParameter("customerId");

        ArrayList<Reservation> list;

        if (reservationId != null) {
            // Return just one specific reservation
            list = new ArrayList<>();
            Reservation r = dao.getById(reservationId);
            if (r != null) list.add(r);
        } else if (customerId != null) {
            // Return all reservations for one customer
            list = dao.getByCustomer(customerId);
        } else {
            // Return ALL reservations sorted by check-in date (QuickSort)
            list = dao.getAllSorted();
        }

        // Build JSON response manually
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            json.append("{")
                    .append("\"reservationId\":\"").append(r.getReservationId()).append("\",")
                    .append("\"customerId\":\"").append(r.getCustomerId()).append("\",")
                    .append("\"roomId\":\"").append(r.getRoomId()).append("\",")
                    .append("\"checkinDate\":\"").append(r.getCheckinDate()).append("\",")
                    .append("\"checkoutDate\":\"").append(r.getCheckoutDate()).append("\",")
                    .append("\"nights\":").append(r.getNights()).append(",")
                    .append("\"status\":\"").append(r.getStatus()).append("\"")
                    .append("}");
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");

        res.getWriter().write(json.toString());
    }
}