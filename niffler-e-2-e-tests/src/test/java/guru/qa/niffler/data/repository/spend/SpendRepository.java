package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.RepositoryType;

import java.util.List;

public interface SpendRepository {

    static SpendRepository getInstance(RepositoryType type) {
        switch (type) {
            case JDBC -> {
                return new SpendRepositoryJdbc();
            }
            case SPRING_JDBC -> {
                return new SpendRepositorySpringJdbc();
            }
            case HIBERNATE -> {
                return new SpendRepositoryHibernate();
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    SpendEntity createSpend(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    List<CategoryEntity> findAllByCategoryName(String categoryName);

    List<SpendEntity> findAllByUsername(String username);

    SpendEntity findAByUsernameAndDescription(String username, String description);

    CategoryEntity editCategory(CategoryEntity category);

    SpendEntity editSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

    void removeCategory(CategoryEntity category);

}