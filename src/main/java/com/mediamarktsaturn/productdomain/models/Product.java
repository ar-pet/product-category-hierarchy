package com.mediamarktsaturn.productdomain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "long_description", length = 2000)
    private String longDescription;

    @Column(name = "online_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OnlineStatus status;

    @ManyToMany(mappedBy = "products")
    private Set<Category> categories = new HashSet<>();

    public Product() {
    }

    public Product(String name, OnlineStatus status, Set<Category> categories) {
        this.name = name;
        this.status = status;
        categories.forEach(this::addCategory);
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getProducts().remove(this);
    }

    public void removeAllCategories() {
        this.categories.forEach(category -> category.getProducts().remove(this));
        this.categories.clear();
    }
}
