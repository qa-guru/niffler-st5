package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    final ElementsCollection spendings = $(".spendings-table tbody").$$("tr");

    public MainPage spendingSizeShouldBe(int size) {
        spendings.should(CollectionCondition.size(size));
        return this;
    }

    public MainPage assertSpendOnPage(int spendIndex, Date date, Double amount, CurrencyValues currency, String categoryName, String spendDescription) {
        Assertions.assertEquals(new SimpleDateFormat("dd MMM yy", new Locale("en")).format(date)
                        + "\n" + amount.intValue() + "\n" + currency.toString() + "\n" + categoryName + "\n" + spendDescription,
                spendings.get(spendIndex).getText());
        return this;
    }
}
