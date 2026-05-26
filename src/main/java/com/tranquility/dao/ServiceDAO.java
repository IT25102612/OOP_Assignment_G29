package com.tranquility.dao;

import com.tranquility.models.Service;
import com.tranquility.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public boolean addService(Service service)
            throws SQLException {

        String sql = "INSERT INTO services " + "(service_id, name, price, " + " category, available) " + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); 
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString (1, service.getId());
            stmt.setString (2, service.getName());
            stmt.setDouble (3, service.getPrice());
            stmt.setString (4, service.getCategory());
            stmt.setBoolean(5, service.isAvailable());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Service> getAllServices() throws SQLException {

        String sql = "SELECT * FROM services " + "WHERE available = TRUE " + "ORDER BY category, name";

        List<Service> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Service s = mapRow(rs);
                list.add(s); 
            }
        }

        return list;
    }

    public List<Service> getByCategory(String category)
            throws SQLException {

        String sql = "SELECT * FROM services " + "WHERE category = ? " + "AND available = TRUE " + "ORDER BY price";

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

    public Service getByServiceId(String serviceId)
            throws SQLException {

        String sql = "SELECT * FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs); 
                }
            }
        }

        return null; 
    }

    public List<Service> getServicesByReservation(
            String reservationId) throws SQLException {

        String sql = "SELECT s.* FROM services s " + "JOIN reservation_services rs " + "  ON s.service_id = rs.service_id " + "WHERE rs.reservation_id = ?";

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

    public boolean linkServiceToReservation(
            String reservationId,
            String serviceId) throws SQLException {

        String checkSql = "SELECT COUNT(*) FROM reservation_services " + "WHERE reservation_id = ? AND service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql)) {

            check.setString(1, reservationId);
            check.setString(2, serviceId);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // already linked
                }
            }

            String insertSql = "INSERT INTO reservation_services " + "(reservation_id, service_id) " + "VALUES (?, ?)";

            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setString(1, reservationId);
                insert.setString(2, serviceId);
                return insert.executeUpdate() > 0;
            }
        }
    }

    public boolean updateService(Service service)
            throws SQLException {

        String sql = "UPDATE services " + "SET name = ?, price = ?, " + "    category = ?, available = ? " + "WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString (1, service.getName());
            stmt.setDouble (2, service.getPrice());
            stmt.setString (3, service.getCategory());
            stmt.setBoolean(4, service.isAvailable());
            stmt.setString (5, service.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteService(String serviceId)
            throws SQLException {

        String sql = "DELETE FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serviceId);
            return stmt.executeUpdate() > 0;
        }
    }

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