package com.example.cse441_project.Model;

public class OrderDetail {
    private String orderId;        // MaGM
    private String itemFoodID;     // MaThucDon
    private int quantity;          // SoLuong

    // Default constructor required for calls to DataSnapshot.getValue(OrderDetail.class)
    public OrderDetail() {
    }

    // Constructor with parameters
    public OrderDetail(String orderId, String itemFoodID, int quantity) {
        this.orderId = orderId;
        this.itemFoodID = itemFoodID;
        this.quantity = quantity;
    }

    // Getter and setter for orderId (MaGM)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // Getter and setter for menuItemId (MaThucDon)
    public String getMenuItemId() {
        return itemFoodID;
    }

    public void setMenuItemId(String menuItemId) {
        this.itemFoodID = itemFoodID;
    }

    // Getter and setter for quantity (SoLuong)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

