package com.example.cse441_project.Model;

public class FoodItem {
    private String itemFoodID;       // MaThucDon
    private String foodName;     // TenThucDon
    private double price;         // GiaTien
    private String categoryId;    // MaLoai
    private String imageUrl;      // Anh

    // Default constructor required for calls to DataSnapshot.getValue(MenuItem.class)
    public FoodItem() {
    }

    // Constructor with parameters
    public FoodItem(String itemFoodID, String foodName, double price, String categoryId, String imageUrl) {
        this.itemFoodID = itemFoodID;
        this.foodName = foodName;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    public String getItemFoodID() {
        return itemFoodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setItemFoodID(String itemFoodID) {
        this.itemFoodID = itemFoodID;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
