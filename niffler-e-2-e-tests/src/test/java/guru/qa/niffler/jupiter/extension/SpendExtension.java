package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class SpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        CategoryJson category = extensionContext.getStore(CategoryExtension.NAMESPACE).get(
                extensionContext.getUniqueId(),
                CategoryJson.class
        );

        List<Spend> potentialSpend = new ArrayList<>();

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spends.class
        ).ifPresentOrElse(
                spends -> potentialSpend.addAll(Arrays.stream(spends.value()).toList()),
                () -> AnnotationSupport.findAnnotation(
                        extensionContext.getRequiredTestMethod(),
                        Spend.class
                ).ifPresent(potentialSpend::add)
        );

        if (!potentialSpend.isEmpty()) {
            List<SpendJson> created = new ArrayList<>();
            for (Spend spend : potentialSpend) {
                SpendJson spendJson = new SpendJson(
                        null,
                        new Date(),
                        spend.amount(),
                        spend.currency(),
                        category.category(),
                        spend.description(),
                        category.username()
                );
                created.add(createSpend(spendJson));
            }
            extensionContext.getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), created);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) throws Exception {
        List spendings = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), List.class);
        if (spendings != null) {
            spendings.forEach(spend -> removeSpend((SpendJson) spend));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext.getParameter()
                .getType();
        return type.isAssignableFrom(SpendJson.class) || type.isAssignableFrom(SpendJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        List<SpendJson> createdSpends = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), List.class);

        return type.isAssignableFrom(SpendJson.class)
                ? createdSpends.getFirst()
                : createdSpends.toArray(SpendJson[]::new);
    }

    protected abstract SpendJson createSpend(SpendJson spend);

    protected abstract void removeSpend(SpendJson spend);
}
