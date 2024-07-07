package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;

@WebTest
abstract public class BaseWebTest {

    protected static final Config CFG = Config.getInstance();
}
