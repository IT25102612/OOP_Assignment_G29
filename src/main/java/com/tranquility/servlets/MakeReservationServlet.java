package com.tranquility.servlets;

import com.tranquility.dao.ReservationDAO;
import com.tranquility.models.Reservation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/MakeReservationServlet")
public class MakeReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");

        String customerId   = req.getParameter("customerId");
        String roomId       = req.getParameter("roomId");
        String checkinDate  = req.getParameter("checkinDate");
        String checkoutDate = req.getParameter("checkoutDate");

        // Basic validation
        if (customerId == null || roomId == null || checkinDate == null || checkoutDate == null) {
            res.getWriter().write("{\"success\":false,\"message\":\"Missing fields\"}");
            return;
        }

        ReservationDAO dao = new ReservationDAO();

        // Check availability before booking
        if (!dao.isRoomAvailable(roomId, checkinDate, checkoutDate)) {
            res.getWriter().write("{\"success\":false,\"message\":\"Room not available for those dates\"}");
            return;
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(
                LocalDate.parse(checkinDate),
                LocalDate.parse(checkoutDate));

        // Create and save the reservation
        String id = dao.generateId();
        Reservation r = new Reservation(id, customerId, roomId,
                checkinDate, checkoutDate, (int) nights, "confirmed");
        dao.addReservation(r);

        res.getWriter().write("{\"success\":true,\"reservationId\":\"" + id + "\"}");
    }
}