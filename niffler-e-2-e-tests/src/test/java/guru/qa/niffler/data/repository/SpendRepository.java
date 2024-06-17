package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public interface SpendRepository {

    // Статический метод getInstance() возвращает экземпляр SpendRepository
    // в зависимости от значения системного свойства "repo"
    static SpendRepository getInstance() {
        // Если свойство "repo" равно "sjdbс", возвращается экземпляр SpendRepositorySpringJdbc
        // Если свойство "repo" равно "hibernate", возвращается экземпляр SpendRepositoryHibernate
        // Если ни одно из предыдущих условий не выполнено, возвращается экземпляр SpendRepositoryJdbc
        return switch (System.getProperty("repo")) {
            case "sjdbс" -> new SpendRepositorySpringJdbc();
            case "hibernate" -> new SpendRepositoryHibernate();
            default -> new SpendRepositoryJdbc();
        };
    }

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    CategoryEntity findCategory(String category, String username);

    void removeCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity category);

    SpendEntity editSpend(SpendEntity category);

    void removeSpend(SpendJson category);

    List<SpendEntity> findAllSpendsByUsername(String username);
}
