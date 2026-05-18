package com.tranquility.servlets;

import com.tranquility.dao.ReservationDAO;
import com.tranquility.models.Reservation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/UpdateReservationServlet")
public class UpdateReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");

        String reservationId = req.getParameter("reservationId");
        ReservationDAO dao   = new ReservationDAO();
        Reservation existing = dao.getById(reservationId);

        if (existing == null) {
            res.getWriter().write("{\"success\":false,\"message\":\"Reservation not found\"}");
            return;
        }

        // Update only the fields that were sent
        String newRoomId      = req.getParameter("roomId");
        String newCheckin     = req.getParameter("checkinDate");
        String newCheckout    = req.getParameter("checkoutDate");

        if (newRoomId  != null) existing.setRoomId(newRoomId);
        if (newCheckin != null) existing.setCheckinDate(newCheckin);
        if (newCheckout!= null) existing.setCheckoutDate(newCheckout);

        // Recalculate nights if dates were changed
        if (newCheckin != null || newCheckout != null) {
            long nights = ChronoUnit.DAYS.between(
                    LocalDate.parse(existing.getCheckinDate()),
                    LocalDate.parse(existing.getCheckoutDate()));
            existing.setNights((int) nights);
        }

        boolean ok = dao.updateReservation(existing);
        res.getWriter().write("{\"success\":" + ok + "}");
    }
}