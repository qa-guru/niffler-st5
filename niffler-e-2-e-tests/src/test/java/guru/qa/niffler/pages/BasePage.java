package guru.qa.niffler.pages;

public abstract class BasePage<T extends BasePage> {
    public abstract T waitForPageLoaded();
    public abstract T checkPage();

}
