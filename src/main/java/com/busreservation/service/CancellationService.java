package com.busreservation.service;

import com.busreservation.database.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public final class CancellationService {
    private CancellationService() {}

    public static BigDecimal cancel(long ticketId) throws Exception {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                BigDecimal price;
                String status;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT ticket_price, status FROM tickets WHERE ticket_id=? FOR UPDATE")) {
                    ps.setLong(1, ticketId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Ticket ID not found.");
                        price = rs.getBigDecimal("ticket_price");
                        status = rs.getString("status");
                    }
                }
                if (!"CONFIRMED".equalsIgnoreCase(status))
                    throw new IllegalStateException("This ticket is already cancelled.");
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE tickets SET status='CANCELLED' WHERE ticket_id=?")) {
                    ps.setLong(1, ticketId);
                    ps.executeUpdate();
                }
                con.commit();
                BigDecimal fee = price.multiply(new BigDecimal("0.10"));
                return price.subtract(fee).setScale(2, java.math.RoundingMode.HALF_UP);
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
