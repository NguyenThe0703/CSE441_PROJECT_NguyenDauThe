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
    public FoodItem(String itemFoodID, String menuName, double price, String categoryId, String imageUrl) {
        this.itemFoodID = itemFoodID;
        this.foodName = menuName;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    // Getter and setter for menuId (MaThucDon)
    public String getMenuId() {
        return itemFoodID;
    }

    public void setMenuId(String menuId) {
        this.itemFoodID = menuId;
    }

    // Getter and setter for menuName (TenThucDon)
    public String getMenuName() {
        return foodName;
    }

    public void setMenuName(String menuName) {
        this.foodName = menuName;
    }

    // Getter and setter for price (GiaTien)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter and setter for categoryId (MaLoai)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    // Getter and setter for imageUrl (Anh)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
