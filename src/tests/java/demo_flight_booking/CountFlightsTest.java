package tests.java.demo_flight_booking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import tests.java.BaseSetup;

public class CountFlightsTest extends BaseSetup {

    private Connection connection;
    private static DemoDbConfig config;

    @BeforeAll
    static void configSetUp() {
        config = ConfigFactory.create(DemoDbConfig.class, System.getProperties());
    }

    @BeforeEach
    void dbSetup() throws SQLException {
        connection = DriverManager.getConnection(
                config.dbUrl(),
                config.dbUser(),
                config.dbPassword());
    }

    @AfterEach
    void dbTearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Disabled
    @Test
    void countFlights() throws SQLException {
        String cityFrom = "Moscow";
        String cityTo = "Helsinki";

        String sql = """
                select count(f.route_no)
                from bookings.flights f
                join bookings.routes r
                on f.route_no = r.route_no
                join bookings.airports_data ad
                on r.departure_airport = ad.airport_code
                join bookings.airports_data ad2
                on r.arrival_airport = ad2.airport_code
                where ad.city ->> 'en' = ?
                and ad2.city ->> 'en' = ?
                    """;
        int dbCount;

        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, cityFrom);
            p.setString(2, cityTo);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                dbCount = rs.getInt(1);
            }
        }

        assertTrue(dbCount != 0);

        page().navigate("http://localhost:8080/");
        page().getByTestId("input-from").fill(cityFrom);
        page().getByTestId("input-to").fill(cityTo);
        page().getByTestId("search-btn").click();

        Locator rows = page().getByTestId("flight-row");

        assertThat(rows).hasCount(dbCount);

    }

}
