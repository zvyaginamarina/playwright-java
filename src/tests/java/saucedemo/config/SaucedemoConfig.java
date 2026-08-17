package tests.java.saucedemo.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources({ "classpath:saucedemo.properties" })
public interface SaucedemoConfig extends Config {

    public static final SaucedemoConfig INSTANCE = ConfigFactory.create(SaucedemoConfig.class);

    @Key("baseUrl")
    String baseUrl();

    @Key("password")
    String password();
}
