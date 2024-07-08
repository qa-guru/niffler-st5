package guru.qa.niffler.test.parallelWeb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Profile")
@Tag("ParallelWebTests")
@ExtendWith(DbCreateUserExtension.class)
public class ProfileTest extends BaseWebTest {

    @BeforeEach
    public void openLoginPage(){
        open(CFG.frontUrl());
        welcomePage.goToLogin();
    }

    @Test
    @TestUser
    @DisplayName("Проверка загрузки аватара")
    void setNewAvatarTest(UserJson userJson) {
        loginPage.login(userJson.username(), userJson.testData().password());
        mainPage.waitForPageLoaded();

        open(ProfilePage.URL);
        String src = profilePage.uploadAvatar("upload/photo.jpeg");

        step("Проверка: загружено изображение профиля", () -> {
            assertTrue(src.startsWith("data:image/jpeg"), "Ошибка загрузки изображения");
            Allure.addAttachment(
                    "Screen on test end",
                    new ByteArrayInputStream(
                            Objects.requireNonNull(
                                    Selenide.screenshot(OutputType.BYTES)
                            )
                    )
            );
        });

    }

    @Test
    @TestUser
    @DisplayName("Проверка создания категории")
    void createCategoryTest(UserJson userJson) {

        CategoryJson category = spendApiClient.createCategory(new CategoryJson(
                null, "Обучение", userJson.username()));

        spendApiClient.createSpend(new SpendJson(
                null, new Date(), category.category(), CurrencyValues.RUB,
                60000.00, "SpendDescription", userJson.username()));

        loginPage.login(userJson.username(), userJson.testData().password());
        mainPage.waitForPageLoaded();
        open(ProfilePage.URL);

        profilePage.createCategory("Обучение_2")
                .checkToasterMessage("New category added");

        profilePage.shouldHaveCategories("Обучение", "Обучение_2"); // api created + ui created
    }

    @Test
    @TestUser
    @DisplayName("Проверка обновления данных профайла")
    void updateProfileTest(UserJson userJson) {
        loginPage.login(userJson.username(), userJson.testData().password());

        open(ProfilePage.URL, ProfilePage.class)
                .setName("Den")
                .submit()
                .checkToasterMessage("Profile successfully updated")
                .checkName("Den")
                .setSurname("Dorko")
                .submit()
                .checkToasterMessage("Profile successfully updated")
                .checkSurname("Dorko");
    }



}
