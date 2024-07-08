package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        ApiCategoryExtension.class,
        ApiSpendExtension.class,
        UserQueueExtension.class,
        ApiLoginExtension.class
})
public @interface WebTest {
}
