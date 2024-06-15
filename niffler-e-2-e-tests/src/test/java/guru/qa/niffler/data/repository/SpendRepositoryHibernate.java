package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SpendRepositoryHibernate implements SpendRepository {

    // Создаем экземпляр EntityManager, используя провайдер EmProvider
    private final EntityManager em = EmProvider.entityManager(DataBase.SPEND);

    @Override
    // Создаем новую категорию
    public CategoryEntity createCategory(CategoryEntity category) {
        // Persist - сохраняем категорию в базе данных
        em.persist(category);
        return category;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        // Merge - обновляем существующую категорию
        return em.merge(category);
    }

    @Override
    // Нахождение категории по имени и имени пользователя
    public CategoryEntity findCategory(String category, String username) {
        // Создаем запрос к базе данных, используя EntityManager.
        // В запросе мы указываем, что хотим найти категорию (CategoryEntity) по условию,
        // где category равен переданному параметру (:category) и username равен переданному параметру (:username).
        return em.createQuery("FROM CategoryEntity WHERE category = :category and username = :username", CategoryEntity.class)
                // Устанавливаем параметры запроса
                .setParameter("category", category)
                .setParameter("username", username)
                // Получаем результат запроса.  В этом случае мы ожидаем, что будет возвращен только один результат.
                .getSingleResult();
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        em.remove(category);
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        em.persist(spend);
        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        return em.merge(spend);
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        em.remove(spend);
    }

    @Override
    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return List.of();
    }
}
