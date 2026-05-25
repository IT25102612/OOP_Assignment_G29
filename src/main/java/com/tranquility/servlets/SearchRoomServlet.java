package com.tranquility.servlets;

import com.tranquility.dao.RoomDAO;
import com.tranquility.models.Room;
import com.tranquility.utils.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/SearchRoomServlet")
public class SearchRoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    public void init() throws ServletException {
        try {
            roomDAO.loadAllRoomsIntoBST();
        } catch (SQLException e) {
            throw new ServletException("Failed to load rooms into BST: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roomId = request.getParameter("roomId");

        // Mode 1: single room lookup by ID
        if (roomId != null && !roomId.trim().isEmpty()) {
            Room room = roomDAO.getRoomById(roomId.trim());
            if (room == null) {
                JsonHelper.err(response, "Room " + roomId + " not found.");
            } else {
                JsonHelper.send(response, "{\"success\":true,\"room\":" + room.toJson() + "}");
            }
            return;
        }

        // Mode 2: filtered search
        String country   = request.getParameter("country");
        String city      = request.getParameter("city");
        String beds      = request.getParameter("beds");
        String baths     = request.getParameter("baths");
        String balcony   = request.getParameter("balcony");
        String pool      = request.getParameter("pool");
        String joint     = request.getParameter("joint");
        String suiteType = request.getParameter("suiteType");
        String checkin   = request.getParameter("checkin");
        String checkout  = request.getParameter("checkout");

        try {
            List<Room> rooms = roomDAO.getFilteredRooms(
                    country, city, beds, baths, balcony, pool, joint, suiteType, checkin, checkout
            );
            StringBuilder json = new StringBuilder();
            json.append("{\"success\":true,\"rooms\":[");
            for (int i = 0; i < rooms.size(); i++) {
                json.append(rooms.get(i).toJson());
                if (i < rooms.size() - 1) json.append(",");
            }
            json.append("]}");
            JsonHelper.send(response, json.toString());
        } catch (SQLException e) {
            JsonHelper.err(response, "Database error: " + e.getMessage());
        }
    }
}