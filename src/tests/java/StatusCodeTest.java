package tests.java;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import config.EnvironmentConfig;

public class StatusCodeTest extends BaseSetup {
    private EnvironmentConfig config;

    @BeforeEach
    public void setupConfig() {
        config = ConfigFactory.create(EnvironmentConfig.class, System.getenv());
    }

    @Test
    void statusCodeTest() {
        System.out.println("env = " + System.getenv("env"));
        page().navigate(config.baseUrl() + "/status_codes/200");

    }

}
