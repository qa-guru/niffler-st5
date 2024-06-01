package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CreateUserExtension.class);

    abstract UserJson createUser(UserJson user);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                TestUser.class
        ).ifPresent(
                user -> {
                    UserJson tempUserJson =  new UserJson(
                            null,
                            Faker.instance().name().name(),
                            Faker.instance().name().firstName(),
                            Faker.instance().name().lastName(),
                            CurrencyValues.RUB,
                            null,
                            null,
                            null,
                            new TestData(
                                    "12345"
                            )
                    );
                    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createUser(tempUserJson));
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}
