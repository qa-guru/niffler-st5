package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;
import guru.qa.niffler.model.CategoryJson;

public class DbCategoryExtension extends CategoryExtension {

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    @Override
    protected CategoryJson createCategory(CategoryJson spend) {
        return null;
    }

    @Override
    protected void removeCategory(CategoryJson spend) {

    }
}
