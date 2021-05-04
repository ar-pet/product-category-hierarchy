package com.mediamarktsaturn.productdomain.repositories;

import com.mediamarktsaturn.productdomain.models.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final EntityManager entityManager;

    public CategoryRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Category getCategoryByIdAndFetchProducts(Long categoryId) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
        Root<Category> rootCategory = criteriaQuery.from(Category.class);

        rootCategory.fetch("products", JoinType.INNER);
        criteriaQuery.select(rootCategory).distinct(true);

        criteriaQuery.select(rootCategory)
                .where(criteriaBuilder.equal(rootCategory.get("categoryId"), categoryId));

        TypedQuery<Category> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }
}
