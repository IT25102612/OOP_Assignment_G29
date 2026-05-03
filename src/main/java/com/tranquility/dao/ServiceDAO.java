package com.tranquility.dao;

import com.tranquility.models.Service;
import com.tranquility.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDAO.java
 *
 * The ONLY class that talks to the services table and the
 * reservation_services table. All your servlets call methods
 * here instead of writing SQL themselves.
 *
 * Methods:
 *   addService()                 → INSERT into services
 *   getAllServices()              → SELECT all available services
 *   getByCategory()              → SELECT filtered by category
 *   getByServiceId()             → SELECT one service by ID
 *   getServicesByReservation()   → SELECT services linked to a booking
 *   linkServiceToReservation()   → INSERT into reservation_services
 *   updateService()              → UPDATE a service record
 *   deleteService()              → DELETE a service record
 *   generateServiceId()          → generates next ID e.g. SVC008
 */
public class ServiceDAO {

    // ----------------------------------------------------------------
    // CREATE — Add a brand new service
    // Called by: AddServiceServlet
    // ----------------------------------------------------------------
    public boolean addService(Service service) throws SQLException {

        String sql = "INSERT INTO services "
                   + "(service_id, name, price, category, available) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString (1, service.getServiceId());
            stmt.setString (2, service.getName());
            stmt.setDouble (3, service.getPrice());
            stmt.setString (4, service.getCategory());
            stmt.setBoolean(5, service.isAvailable());

            return stmt.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------
    // READ — Get all available services
    // Called by: ViewServicesServlet (no parameters)
    // ----------------------------------------------------------------
    public List<Service> getAllServices() throws SQLException {

        String sql = "SELECT * FROM services "
                   + "WHERE available = TRUE "
                   + "ORDER BY category, name";

        List<Service> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ----------------------------------------------------------------
    // READ — Get services filtered by category
    // Called by: ViewServicesServlet?category=Meals
    // This is what meal-options.html calls to load the meal cards
    // ----------------------------------------------------------------
    public List<Service> getByCategory(String category)
            throws SQLException {

        String sql = "SELECT * FROM services "
                   + "WHERE category = ? AND available = TRUE "
                   + "ORDER BY price";

        List<Service> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // ----------------------------------------------------------------
    // READ — Get one service by its ID
    // Called by: UpdateServiceServlet, DeleteServiceServlet
    // ----------------------------------------------------------------
    public Service getByServiceId(String serviceId)
            throws SQLException {

        String sql = "SELECT * FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null; // returns null if not found
    }

    // ----------------------------------------------------------------
    // READ — Get all services linked to a specific reservation
    // Called by: ViewServicesServlet?reservationId=RES001
    // Shows admin or guest which services are on their booking
    // ----------------------------------------------------------------
    public List<Service> getServicesByReservation(String reservationId)
            throws SQLException {

        String sql = "SELECT s.* FROM services s "
                   + "JOIN reservation_services rs "
                   + "  ON s.service_id = rs.service_id "
                   + "WHERE rs.reservation_id = ?";

        List<Service> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // ----------------------------------------------------------------
    // CREATE — Link a service to a reservation
    // Called by: LinkServiceServlet
    // Example: guest picks Breakfast → links SVC001 to RES001
    // ----------------------------------------------------------------
    public boolean linkServiceToReservation(String reservationId,
                                             String serviceId)
            throws SQLException {

        // First check it isn't already linked
        String checkSql = "SELECT COUNT(*) FROM reservation_services "
                        + "WHERE reservation_id = ? AND service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check =
                     conn.prepareStatement(checkSql)) {

            check.setString(1, reservationId);
            check.setString(2, serviceId);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // already linked, do not duplicate
                }
            }

            // Not linked yet — insert it
            String insertSql =
                "INSERT INTO reservation_services "
              + "(reservation_id, service_id) VALUES (?, ?)";

            try (PreparedStatement insert =
                         conn.prepareStatement(insertSql)) {
                insert.setString(1, reservationId);
                insert.setString(2, serviceId);
                return insert.executeUpdate() > 0;
            }
        }
    }

    // ----------------------------------------------------------------
    // UPDATE — Modify a service's details
    // Called by: UpdateServiceServlet
    // ----------------------------------------------------------------
    public boolean updateService(Service service) throws SQLException {

        String sql = "UPDATE services "
                   + "SET name = ?, price = ?, "
                   + "    category = ?, available = ? "
                   + "WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString (1, service.getName());
            stmt.setDouble (2, service.getPrice());
            stmt.setString (3, service.getCategory());
            stmt.setBoolean(4, service.isAvailable());
            stmt.setString (5, service.getServiceId());

            return stmt.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------
    // DELETE — Permanently remove a service
    // Called by: DeleteServiceServlet
    // ----------------------------------------------------------------
    public boolean deleteService(String serviceId) throws SQLException {

        String sql = "DELETE FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serviceId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------
    // HELPER — Generates the next service ID
    // e.g. if 7 services exist, returns "SVC008"
    // Called by: AddServiceServlet before inserting
    // ----------------------------------------------------------------
    public String generateServiceId() throws SQLException {

        String sql = "SELECT COUNT(*) FROM services";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1);
                return "SVC" + String.format("%03d", count + 1);
            }
        }
        return "SVC001";
    }

    // ----------------------------------------------------------------
    // HELPER — Converts one database row into a Service object
    // Used internally by every SELECT method above
    // ----------------------------------------------------------------
    private Service mapRow(ResultSet rs) throws SQLException {
        return new Service(
            rs.getString ("service_id"),
            rs.getString ("name"),
            rs.getDouble ("price"),
            rs.getString ("category"),
            rs.getBoolean("available")
        );
    }
}