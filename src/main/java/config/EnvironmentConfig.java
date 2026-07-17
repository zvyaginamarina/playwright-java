package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Key;

@Config.Sources({ "classpath:config-${ENV}.properties" })
public interface EnvironmentConfig extends Config {
    @Key("baseUrl")
    String baseUrl();
}
