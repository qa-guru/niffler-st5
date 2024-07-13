package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.ui.UiBot;

@WebTest
public class BaseWebTest {

    protected final UiBot ui = new UiBot();

    static {
        Configuration.browserSize = "1920x1080";
    }

}
