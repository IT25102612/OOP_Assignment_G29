package com.tranquility.servlets;

import com.tranquility.dao.RoomDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateRoomServlet")
public class UpdateRoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    public void init() throws ServletException {
        try { roomDAO.loadAllRoomsIntoBST(); }
        catch (SQLException e) { throw new ServletException(e.getMessage(), e); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roomId = request.getParameter("roomId");
        if (roomId == null || roomId.trim().isEmpty()) {
            JsonHelper.err(response, "roomId is required.");
            return;
        }

        try {
            boolean updated = roomDAO.updateRoom(
                    roomId,
                    request.getParameter("suiteName"),
                    request.getParameter("pricePerNight"),
                    request.getParameter("beds"),
                    request.getParameter("baths"),
                    request.getParameter("bedType"),
                    request.getParameter("status"),
                    request.getParameter("balcony"),
                    request.getParameter("privatePool"),
                    request.getParameter("jointRooms")
            );
            if (updated) JsonHelper.ok(response, "Room " + roomId + " updated successfully.");
            else         JsonHelper.err(response, "Room " + roomId + " not found.");
        } catch (SQLException e) {
            JsonHelper.err(response, "Database error: " + e.getMessage());
        }
    }
}