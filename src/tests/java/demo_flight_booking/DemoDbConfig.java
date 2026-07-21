package tests.java.demo_flight_booking;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Key;

@Config.Sources({ "classpath:demo.db.properties" })
interface DemoDbConfig extends Config {
    @Key("demo.db.url")
    String dbUrl();

    @Key("demo.db.user")
    String dbUser();

    @Key("demo.db.password")
    String dbPassword();
}
