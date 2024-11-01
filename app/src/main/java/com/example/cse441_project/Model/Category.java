package com.example.cse441_project.Model;

public class Category {
    private String categoryId;   // MaLoai
    private String categoryName; // TenLoai
    private String categoryImage;

    public Category() {
    }

    // Constructor with parameters
    
    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    // Getter and setter for categoryName (TenLoai)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getImage() {
        return categoryImage;
    }
}
