package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import guru.qa.niffler.model.CategoryJson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Collections;
import java.util.List;

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
		em.remove(em.merge(category));
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
	public void removeSpendByCategoryIdOfUser(CategoryJson category) {
		String hql = "DELETE FROM spend WHERE category_id= :id and username= :user";
		Query query = em.createQuery(hql, SpendEntity.class);
		query.setParameter("id", category.id());
		query.setParameter("user", category.username());
	}

	@Override
	public List<SpendEntity> findAllByUsername(String username) {
		return Collections.singletonList(em.find(SpendEntity.class, username));
	}

	@Override
	public CategoryEntity findCategory(String category) {
		return em.find(CategoryEntity.class, category);
	}

}
