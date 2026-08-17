package tests.java.saucedemo;

public enum User {
    STANDARD_USER("standard_user"),
    LOCKED_OUT_USER("locked_out_user"),
    PROBLEM_USER("problem_user"),
    PERFORMANCE_GLITCH_USER("performance_glitch_user"),
    ERROR_USER("error_user"),
    VISUAL_USER("visual_user");

    private final String username;

    User(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

}
