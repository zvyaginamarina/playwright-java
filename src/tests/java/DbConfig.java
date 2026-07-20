package tests.java;

import org.aeonbits.owner.Config;

interface DbConfig extends Config {
    @Key("db.url")
    @DefaultValue("jdbc:postgresql://localhost:5432/test_db")
    String dbUrl();

    @Key("db.user")
    @DefaultValue("admin")
    String dbUser();

    @Key("db.password")
    @DefaultValue("secret")
    String dbPassword();
}
