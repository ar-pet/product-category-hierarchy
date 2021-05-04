package com.mediamarktsaturn.productdomain.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue
    private Long categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(
            name = "CATEGORY_PRODUCT",
            joinColumns = {@JoinColumn(name = "category_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    Set<Product> products = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "super_category_id")
    private Category superCategory;

    @OneToMany(mappedBy = "superCategory")
    private Set<Category> subCategories = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Category getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(Category superCategory) {
        this.superCategory = superCategory;
    }

    public Set<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeFromSuperCategory() {
        if(superCategory != null) {
            this.superCategory.getSubCategories().remove(this);
        }
    }
}
