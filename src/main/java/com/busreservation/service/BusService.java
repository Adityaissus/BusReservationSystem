package com.busreservation.service;

import com.busreservation.database.DBConnection;
import com.busreservation.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BusService {
    public List<Route> searchRoutes(String source, String destination, LocalDate date) throws SQLException {
        String sql = """
            SELECT r.route_id, r.source, r.destination, r.departure_time, r.arrival_time,
                   r.price, b.bus_id, b.bus_name, b.bus_type, b.total_seats,
                   (SELECT COUNT(*) FROM tickets t
                    WHERE t.bus_id = b.bus_id AND t.journey_date = ?
                    AND t.status = 'CONFIRMED') AS booked
            FROM routes r JOIN buses b ON b.bus_id = r.bus_id
            WHERE LOWER(r.source) = LOWER(?) AND LOWER(r.destination) = LOWER(?)
            ORDER BY r.departure_time
            """;
        List<Route> routes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, source.trim());
            ps.setString(3, destination.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bus bus = createBus(rs.getInt("bus_id"), rs.getString("bus_name"),
                            rs.getString("bus_type"), rs.getInt("total_seats"));
                    routes.add(new Route(
                            rs.getInt("route_id"), bus, rs.getString("source"),
                            rs.getString("destination"), rs.getTime("departure_time").toLocalTime(),
                            rs.getTime("arrival_time").toLocalTime(), rs.getBigDecimal("price")
                    ));
                }
            }
        }
        return routes;
    }

    public List<Integer> getAvailableSeats(int busId, int totalSeats, LocalDate date) throws SQLException {
        boolean[] booked = new boolean[totalSeats + 1];
        String sql = "SELECT seat_number FROM tickets WHERE bus_id=? AND journey_date=? AND status='CONFIRMED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, busId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int seat = rs.getInt(1);
                    if (seat >= 1 && seat <= totalSeats) booked[seat] = true;
                }
            }
        }
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) if (!booked[i]) available.add(i);
        return available;
    }

    private Bus createBus(int id, String name, String type, int seats) {
        return switch (type.toUpperCase()) {
            case "AC" -> new ACBus(id, name, seats);
            case "NON_AC" -> new NonACBus(id, name, seats);
            case "SLEEPER" -> new SleeperBus(id, name, seats);
            default -> new Bus(id, name, seats);
        };
    }
}
