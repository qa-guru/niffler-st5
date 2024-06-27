package guru.qa.niffler.ui;

import guru.qa.niffler.ui.page.MainPage;
import guru.qa.niffler.ui.page.StartPage;

public class UiBot {

    public StartPage startPage() {
        return new StartPage();
    }

    public MainPage mainPage() {
        return new MainPage();
    }

}