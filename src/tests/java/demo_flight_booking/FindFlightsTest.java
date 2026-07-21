package tests.java.demo_flight_booking;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FindFlightsTest {
    private Connection connection;
    private static DemoDbConfig config;

    @BeforeAll
    static void loadConfig() {
        config = ConfigFactory.create(DemoDbConfig.class, System.getProperties());
    }

    @BeforeEach
    void setup() throws SQLException {
        connection = DriverManager.getConnection(
                config.dbUrl(),
                config.dbUser(),
                config.dbPassword());
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null)
            connection.close();
    }

    @Test
    void allFlightCount() throws SQLException {
        String countAllFlights = "SELECT count(flight_id) FROM bookings.flights";
        try (Statement stmn = connection.createStatement();
                ResultSet rs = stmn.executeQuery(countAllFlights)) {
            if (rs.next()) {
                int n = rs.getInt(1);
                System.out.println("Total flights " + n);
                assertTrue(n > 0);
            }

        }
    }

    @Test
    void getFlightById() throws SQLException {
        String getFlight = "SELECT route_no, status FROM bookings.flights WHERE flight_id = ?";
        int flightId = 12136;
        try (PreparedStatement prStmn = connection.prepareStatement(getFlight)) {
            prStmn.setInt(1, flightId);
            try (ResultSet rs = prStmn.executeQuery()) {
                assertTrue(rs.next(), "flight not found");

                String routeNo = rs.getString("route_no");
                String status = rs.getString("status");
                assertNotNull(routeNo);
                assertNotNull(status);
            }
        }
    }

    @Test
    void getFlightsFromLed() throws SQLException {
        String getflight = """
                select count(f.flight_id )
                from bookings.flights f
                join bookings.routes r
                on f.route_no = r.route_no
                where r.departure_airport = ?
                                """;
        String airportCode = "LED";
        try (PreparedStatement prStmn = connection.prepareStatement(getflight)) {
            prStmn.setString(1, airportCode);
            try (ResultSet rs = prStmn.executeQuery()) {
                if (rs.next()) {
                    int flightCount = rs.getInt(1);
                    assertTrue(flightCount > 0);
                }
            }
        }
    }

    @Test
    void getPassangerList() throws SQLException {
        String sql = """
                select t.passenger_name
                from bookings.flights f
                join bookings.segments s
                on f.flight_id = s.flight_id
                join bookings.tickets t
                on s.ticket_no = t.ticket_no
                where f.flight_id = ?
                                """;
        int flightId = 72;
        try (PreparedStatement prStmn = connection.prepareStatement(sql)) {
            prStmn.setInt(1, flightId);
            try (ResultSet rs = prStmn.executeQuery()) {
                List<String> passengerList = new ArrayList<>();
                while (rs.next()) {
                    passengerList.add(rs.getString("passenger_name"));
                }
                assertTrue(passengerList.size() > 0);
            }
        }
    }

    @Test
    void compareTicketsBookingAmount() throws SQLException {
        String getTotalTicketsPrice = """
                select t.book_ref , sum(s.price)
                from bookings.segments s
                join bookings.tickets t
                on s.ticket_no = t.ticket_no
                where t.book_ref = ?
                group by t.book_ref
                                """;

        String getBookingAmount = """
                select b.total_amount from
                bookings.bookings b
                where b.book_ref = ?
                                """;

        String bookingNo = "7UU4BE";
        BigDecimal ticketPrice;
        BigDecimal bookingAmount;

        try (PreparedStatement prstmn1 = connection.prepareStatement(getTotalTicketsPrice)) {
            prstmn1.setString(1, bookingNo);
            try (ResultSet rs1 = prstmn1.executeQuery()) {
                assertTrue(rs1.next());
                ticketPrice = rs1.getBigDecimal(2);
            }
        }
        assertNotNull(ticketPrice);

        try (PreparedStatement prstmn2 = connection.prepareStatement(getBookingAmount)) {
            prstmn2.setString(1, bookingNo);
            try (ResultSet rs2 = prstmn2.executeQuery()) {
                assertTrue(rs2.next());
                bookingAmount = rs2.getBigDecimal(1);
            }
        }
        assertNotNull(bookingAmount);

        assertTrue(ticketPrice.compareTo(bookingAmount) == 0, "amounts aren't equal");
    }
}
