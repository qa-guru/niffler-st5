package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.user.DbCreateUserExtension;
import guru.qa.niffler.ui.UiBot;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        BrowserExtension.class,
        DbCreateUserExtension.class})
public class BaseWebTest {

    protected final UiBot ui = new UiBot();

}
