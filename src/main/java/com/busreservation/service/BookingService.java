package com.busreservation.service;

import com.busreservation.database.DBConnection;
import com.busreservation.exceptions.SeatBookedException;
import com.busreservation.interfaces.Reservable;
import com.busreservation.model.Route;
import com.busreservation.model.Ticket;

import java.sql.*;
import java.time.LocalDate;

public class BookingService implements Reservable {
    private int selectedBusId;
    private int selectedSeat;
    private LocalDate selectedDate;

    @Override
    public synchronized void bookSeat(int busId, int seatNumber, LocalDate journeyDate)
            throws SeatBookedException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT ticket_id FROM tickets WHERE bus_id=? AND seat_number=? AND journey_date=? AND status='CONFIRMED'")) {
            ps.setInt(1, busId);
            ps.setInt(2, seatNumber);
            ps.setDate(3, Date.valueOf(journeyDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) throw new SeatBookedException("Seat " + seatNumber + " is already booked.");
            }
        } catch (SeatBookedException e) {
            throw e;
        } catch (SQLException e) {
            throw new SeatBookedException("Could not check seat availability: " + e.getMessage());
        }
        selectedBusId = busId;
        selectedSeat = seatNumber;
        selectedDate = journeyDate;
    }

    public synchronized Ticket confirmBooking(Route route, LocalDate date, int seat,
            String name, int age, String gender, String phone) throws Exception {
        if (name == null || name.isBlank() || phone == null || phone.isBlank())
            throw new IllegalArgumentException("Passenger name and phone are required.");
        if (age <= 0) throw new IllegalArgumentException("Age must be greater than zero.");
        if (seat < 1 || seat > route.getBus().getTotalSeats())
            throw new IllegalArgumentException("Invalid seat number.");

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String sql = """
                    INSERT INTO tickets
                    (bus_id, route_id, passenger_name, age, gender, phone,
                     seat_number, journey_date, ticket_price, status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')
                    """;
                long ticketId;
                try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, route.getBus().getBusId());
                    ps.setInt(2, route.getRouteId());
                    ps.setString(3, name.trim());
                    ps.setInt(4, age);
                    ps.setString(5, gender);
                    ps.setString(6, phone.trim());
                    ps.setInt(7, seat);
                    ps.setDate(8, Date.valueOf(date));
                    ps.setBigDecimal(9, route.getPrice());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Ticket ID was not generated.");
                        ticketId = keys.getLong(1);
                    }
                }
                con.commit();
                return new Ticket(ticketId, route.getBus().getBusId(), route.getRouteId(),
                        name.trim(), age, gender, phone.trim(), seat, date, route.getPrice(), "CONFIRMED");
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                if (e instanceof SQLIntegrityConstraintViolationException
                        || (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate"))) {
                    throw new SeatBookedException("That seat was just booked by another user. Select another seat.");
                }
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public void cancelSeat(long ticketId) throws Exception {
        CancellationService.cancel(ticketId);
    }
}
