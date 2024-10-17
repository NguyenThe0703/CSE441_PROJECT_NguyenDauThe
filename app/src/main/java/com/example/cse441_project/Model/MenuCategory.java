package com.example.cse441_project.Model;

public class MenuCategory {
    private String categoryId;   // MaLoai
    private String categoryName; // TenLoai


    public MenuCategory() {
    }

    // Constructor with parameters
    public MenuCategory(String categoryId, String categoryName) {
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
}
