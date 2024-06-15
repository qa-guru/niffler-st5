package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public interface SpendRepository {

    String repoType = "hibernate";

    // Статический метод getInstance() возвращает экземпляр SpendRepository
    // в зависимости от значения системного свойства "repo"
    static SpendRepository getInstance() {

        System.setProperty("repo", repoType);

        // Если свойство "repo" равно "sjdbс", возвращается экземпляр SpendRepositorySpringJdbc
        if ("sjdbс".equals(System.getProperty("repo"))) {
            System.out.println("SPRING_JDBС");
            return new SpendRepositorySpringJdbc();
        }
        // Если свойство "repo" равно "hibernate", возвращается экземпляр SpendRepositoryHibernate
        if ("hibernate".equals(System.getProperty("repo"))) {
            System.out.println("HIBERNATE");
            return new SpendRepositoryHibernate();
        }
        // Если ни одно из предыдущих условий не выполнено, возвращается экземпляр SpendRepositoryJdbc
        System.out.println("JDBC");
        return new SpendRepositoryJdbc();
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
