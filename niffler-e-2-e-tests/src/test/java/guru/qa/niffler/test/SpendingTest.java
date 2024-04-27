package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.extensions.DesktopCapabilities;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.PrepareTestData;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pageObjects.LoginPage;
import guru.qa.niffler.pageObjects.MainPage;
import guru.qa.niffler.retrofit.categoriesEndpoint.CategoriesClient;
import guru.qa.niffler.retrofit.spendsEndpoint.SpendsClient;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


@ExtendWith(DesktopCapabilities.class)
@ExtendWith(PrepareTestData.class)
public class SpendingTest {

    public static final String CENTER = "{block: \"center\"}";


//    @BeforeEach
//    void setData () {
//        new CategoriesClient().addNewCategory();
//        new SpendsClient().createSpend();
//    }


    @Test
    void spendingShouldBeDeletedAfterTableAction() {
        open("http://127.0.0.1:3000/", LoginPage.class)
                .doLogin()
                .at(MainPage.class)
                .selectCategory("Тачка")
                .deleteCategory();
    }
}
