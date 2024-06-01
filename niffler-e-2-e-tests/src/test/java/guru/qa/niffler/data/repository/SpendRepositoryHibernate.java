package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

public class SpendRepositoryHibernate implements SpendRepository {

    private final EntityManager em = EmProvider.entityManager(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        em.persist(category);
        return category;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        return em.merge(category);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        em.remove(category);
    }

    @Override
    public CategoryEntity getCategory(String categoryName) {
        return em.createQuery("from CategoryEntity WHERE category=:categoryName", CategoryEntity.class)
                .setParameter("categoryName", categoryName)
                .getSingleResult();
    }


    @Override
    public SpendEntity createSpend(SpendEntity category) {
        em.persist(category);
        return category;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        return em.merge(spend);
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        em.remove(spend);
    }
}
