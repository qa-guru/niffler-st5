package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    static SpendRepository getInstance() {
        if ("sjdbc".equals(System.getProperty("repository"))) {
            return new SpendRepositorySpringJdbc();
        }
        if ("jpa".equals(System.getProperty("repository"))) {
            return new SpendRepositoryHibernate();
        }
        return new SpendRepositoryJdbc();
    }

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findUserCategoryByName(String username, String category);

    void removeCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    void removeSpend(SpendEntity spend);

}
