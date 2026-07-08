package tests.java;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests extends BaseSetup {

    @Test
    void testLoginPage() {

        System.out.println("Test start in thread: " + Thread.currentThread().getName() + " at: " + LocalTime.now());

        page().navigate("https://the-internet.herokuapp.com/login");
        assertThat(page()).hasTitle("The Internet");
        assertThat(page().getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(2)))
                .containsText("Login Page");

    }

    @Test
    void testAddRemoveElements() {

        System.out.println("Test start in thread: " + Thread.currentThread().getName() + " at: " + LocalTime.now());

        page().navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add Element")).click();
        assertThat(page().getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete"))).isVisible();

    }

}