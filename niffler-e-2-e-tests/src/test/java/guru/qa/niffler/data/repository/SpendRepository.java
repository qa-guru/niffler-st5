package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.List;

public interface SpendRepository {

    static SpendRepository getInstance() {
        String repo = System.getProperty("repo");
        if ("sjdbc".equals(repo)) {
            return new SpendRepositorySpringJdbc();
        }
        if ("jdbc".equals(repo)) {
            return new SpendRepositoryJdbc();
        }
        return new SpendRepositoryHibernate();
    }

    CategoryEntity createCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);

    List<SpendEntity> findAllByUsername(String username);

    CategoryEntity findByUsernameAndCategory(String username, String category);
}
