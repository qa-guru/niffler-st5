package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.Database;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SpendRepositoryHibernate implements SpendRepository {

    private final EntityManager em = EmProvider.entityManager(Database.SPEND);

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        em.persist(spend);
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        em.persist(category);
        return category;
    }

    @Override
    public List<CategoryEntity> findAllByCategoryName(String categoryName) {
        return em.createQuery("FROM category WHERE CategoryEntity = :categoryName",
                        CategoryEntity.class)
                .setParameter("categoryName", categoryName)
                .getResultList();
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return em.createQuery(
                        "FROM SpendEntity WHERE username = :username",
                        SpendEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public SpendEntity findAByUsernameAndDescription(String username, String description) {
        return em.createQuery(
                        "FROM SpendEntity WHERE username = :username AND description = :description",
                        SpendEntity.class)
                .setParameter("username", username)
                .setParameter("description", description)
                .getSingleResult();
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        return em.merge(category);
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        em.merge(spend);
        return spend;
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        em.remove(em.contains(spend) ? spend : em.merge(spend));
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        em.remove(em.contains(category) ? category : em.merge(category));
    }

}