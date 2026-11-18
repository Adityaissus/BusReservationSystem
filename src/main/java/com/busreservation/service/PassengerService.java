package com.busreservation.service;

import com.busreservation.database.DBConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;

public class PassengerService {
    public DefaultTableModel getPassengers(int busId, LocalDate date) throws SQLException {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Ticket ID", "Passenger", "Age", "Gender", "Phone", "Seat", "Price", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        String sql = """
            SELECT ticket_id, passenger_name, age, gender, phone, seat_number,
                   ticket_price, status
            FROM tickets WHERE bus_id=? AND journey_date=?
            ORDER BY seat_number
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, busId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getLong("ticket_id"), rs.getString("passenger_name"),
                            rs.getInt("age"), rs.getString("gender"), rs.getString("phone"),
                            rs.getInt("seat_number"), rs.getBigDecimal("ticket_price"),
                            rs.getString("status")
                    });
                }
            }
        }
        return model;
    }
}
