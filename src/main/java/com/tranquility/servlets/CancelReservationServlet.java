package com.tranquility.servlets;

import com.tranquility.dao.ReservationDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/CancelReservationServlet")
public class CancelReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");

        String reservationId = req.getParameter("reservationId");

        if (reservationId == null || reservationId.isEmpty()) {
            res.getWriter().write("{\"success\":false,\"message\":\"Missing reservationId\"}");
            return;
        }

        ReservationDAO dao = new ReservationDAO();
        boolean ok = dao.cancelReservation(reservationId);

        res.getWriter().write("{\"success\":" + ok + "}");
    }
}