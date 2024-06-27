package guru.qa.niffler.ui.component;

import com.codeborne.selenide.SelenideElement;

public class BaseComponent<T extends BaseComponent<?>> {

    protected final SelenideElement self;

    protected BaseComponent(SelenideElement  self){
        this.self = self;
    }

}
