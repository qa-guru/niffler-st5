package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

public interface SpendRepository {

    static SpendRepository getInstance(){
        if ("sjdbs".equals(System.getProperty("repo"))){
            return new SpendRepositoryJdbc();
        }

            return new SpendRepositoryJdbc();
    }

    CategoryEntity createCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);
}
