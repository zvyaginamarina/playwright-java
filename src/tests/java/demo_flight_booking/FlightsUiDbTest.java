package tests.java.demo_flight_booking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.options.AriaRole;

import tests.java.BaseSetup;

public class FlightsUiDbTest extends BaseSetup {
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
    void findFlightFromUiInDb() throws SQLException {
        page().navigate("http://localhost:8080/");
        page().getByTestId("input-from").fill("Moscow");
        page().getByTestId("input-to").fill("Sochi");
        page().getByTestId("search-btn").click();

        String flightNo = page().getByTestId("flight-row").locator(".pill").nth(0).textContent();
        String departureDate = page().getByTestId("flight-row").getByRole(AriaRole.CELL).nth(3).textContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(departureDate, formatter);
        OffsetDateTime uiOffset = date.atOffset(ZoneOffset.UTC);

        String sql = "SELECT flight_id FROM bookings.flights WHERE route_no = ? AND scheduled_departure = ?";

        try (PreparedStatement prStmn = connection.prepareStatement(sql)) {
            prStmn.setString(1, flightNo);
            prStmn.setObject(2, uiOffset);
            try (ResultSet rs = prStmn.executeQuery()) {
                assertTrue(rs.next());
            }
        }

    }

}
