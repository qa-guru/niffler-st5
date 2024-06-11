package guru.qa.niffler.test;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(AllureJunit5.class)
public class EmptyTest {
    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll");
    }

    @AllureId("1234")
    @Test
    void emptyTest() {
        Allure.addAttachment("example", "example");
    }

    @Test
    void emptyTest1() {
        Allure.addAttachment("example", "example");
        Assertions.assertTrue(false);
    }
}
