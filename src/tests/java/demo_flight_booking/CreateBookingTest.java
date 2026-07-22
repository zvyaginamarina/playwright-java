package tests.java.demo_flight_booking;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.datafaker.Faker;
import tests.java.BaseSetup;

public class CreateBookingTest extends BaseSetup {
    private Connection connection;
    private static DemoDbConfig config;

    @BeforeAll
    static void configSetUp() {
        config = ConfigFactory.create(DemoDbConfig.class, System.getProperties());
    }

    @BeforeEach
    void dbSetUp() throws SQLException {
        connection = DriverManager.getConnection(
                config.dbUrl(),
                config.dbUser(),
                config.dbPassword());
    }

    @AfterEach
    void dbTearDown() throws SQLException {
        if (connection != null)
            connection.close();
    }

    @Disabled
    @Test
    void createBooking() throws SQLException {
        Faker faker = new Faker();

        String passengerName = faker.name().fullName();
        page().navigate("http://localhost:8080/");
        page().getByTestId("input-from").fill("Moscow");
        page().getByTestId("input-to").fill("Sochi");
        page().getByTestId("search-btn").click();
        page().getByTestId("flight-row").nth(0).getByTestId("book-btn").click();

        page().getByTestId("input-passenger-name").fill(passengerName);
        page().getByTestId("confirm-booking-btn").click();

        String bookingNo = page().getByTestId("booking-ref").textContent();
        String ticketPrice = page().getByTestId("booking-price").textContent();
        BigDecimal price = new BigDecimal(ticketPrice);
        String ticketClass = page().getByTestId("booking-fare").textContent();
        String ticketNo = page().getByTestId("booking-ticket-no").textContent();

        String sql = """
                select b.book_ref, t.ticket_no, t.passenger_name, s.price, s.fare_conditions
                from bookings.bookings b
                join bookings.tickets t
                on b.book_ref = t.book_ref
                join bookings.segments s
                on t.ticket_no = s.ticket_no
                where b.book_ref = ?
                    """;

        try (PreparedStatement prStmn = connection.prepareStatement(sql)) {
            prStmn.setString(1, bookingNo);
            try (ResultSet rs = prStmn.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(bookingNo, rs.getString(1));
                assertEquals(ticketNo, rs.getString(2));
                assertEquals(passengerName, rs.getString(3));
                assertTrue(price.compareTo(rs.getBigDecimal(4)) == 0);
                assertEquals(ticketClass, rs.getString(5));
            }
        } finally {
            cleanUp(bookingNo, ticketNo);
        }

        String sqlCleanUp = "select b.book_ref from bookings.bookings b where b.book_ref = ?";

        try (PreparedStatement p = connection.prepareStatement(sqlCleanUp)) {
            p.setString(1, bookingNo);
            try (ResultSet rs = p.executeQuery()) {
                assertFalse(rs.next());
            }
        }

    }

    void cleanUp(String bookingNo, String ticketNo) throws SQLException {
        String deleteSegment = "delete from bookings.segments where ticket_no = ?";
        String deleteTicket = "delete from bookings.tickets where book_ref = ?";
        String deleteBooking = "delete from bookings.bookings where book_ref = ?";

        connection.setAutoCommit(false);
        try {
            if (ticketNo != null && !ticketNo.isEmpty()) {
                try (PreparedStatement prstmn1 = connection.prepareStatement(deleteSegment)) {
                    prstmn1.setString(1, ticketNo);
                    prstmn1.executeUpdate();

                }
            }

            try (PreparedStatement prstmn2 = connection.prepareStatement(deleteTicket)) {
                prstmn2.setString(1, bookingNo);
                prstmn2.executeUpdate();

            }
            try (PreparedStatement prstmn3 = connection.prepareStatement(deleteBooking)) {
                prstmn3.setString(1, bookingNo);
                prstmn3.executeUpdate();

            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
