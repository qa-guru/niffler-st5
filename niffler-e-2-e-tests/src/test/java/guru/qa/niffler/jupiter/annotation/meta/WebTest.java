package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.spend.HttpSpendExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.category.HttpCategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        HttpCategoryExtension.class,
        HttpSpendExtension.class,
        UserQueueExtension.class
})
public @interface WebTest {
}
