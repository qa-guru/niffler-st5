package guru.qa.niffler.pageObjects;

import io.qameta.allure.Step;
import lombok.SneakyThrows;

public class BasePage<T extends BasePage<T>> {

    @SneakyThrows
    @Step("Перешли на страницу {0}")
    public <K extends BasePage> K at(Class<K> pageObject) {
        return pageObject.getDeclaredConstructor().newInstance();
    }

}
