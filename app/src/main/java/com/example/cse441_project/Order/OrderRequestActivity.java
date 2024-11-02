package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.Model.Order;
import com.example.cse441_project.Model.OrderDetail;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderRequestActivity extends AppCompatActivity {
    private TextView tvTableNumber, txtTTien;
    private Button btnCancel, btnConfirm;
    private ImageView imgBack;
    private ArrayList<FoodItem> foodItems;
    private FirebaseFirestore db;
    private double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processing_order_request);

        db = FirebaseFirestore.getInstance();

        tvTableNumber = findViewById(R.id.tvTableNumber);
        txtTTien = findViewById(R.id.txtTTien);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        imgBack = findViewById(R.id.imgBack);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        Intent intent = getIntent();
        String tableId = intent.getStringExtra("tableId");
        foodItems = (ArrayList<FoodItem>) intent.getSerializableExtra("foodItems");
        ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) intent.getSerializableExtra("orderDetails");

        // Display table number
        tvTableNumber.setText("Bàn số: " + tableId);

        // Populate order details in table and calculate total amount
        if (orderDetails != null) {
            for (OrderDetail detail : orderDetails) {
                TableRow tableRow = new TableRow(this);

                TextView tvIndex = new TextView(this);
                tvIndex.setText(String.valueOf(orderDetails.indexOf(detail) + 1));
                tvIndex.setGravity(Gravity.CENTER);
                tvIndex.setPadding(8, 8, 8, 8);
                tableRow.addView(tvIndex);

                TextView tvItemName = new TextView(this);
                FoodItem foodItem = findFoodItemById(detail.getItemFoodID());
                if (foodItem != null) {
                    tvItemName.setText(foodItem.getFoodName());
                    double price = foodItem.getPrice();
                    totalAmount += price * detail.getQuantity();
                } else {
                    tvItemName.setText("N/A");
                }
                tvItemName.setGravity(Gravity.CENTER);
                tvItemName.setPadding(8, 8, 8, 8);
                tableRow.addView(tvItemName);

                TextView tvQuantity = new TextView(this);
                tvQuantity.setText(String.valueOf(detail.getQuantity()));
                tvQuantity.setGravity(Gravity.CENTER);
                tvQuantity.setPadding(8, 8, 8, 8);
                tableRow.addView(tvQuantity);

                TextView tvPrice = new TextView(this);
                if (foodItem != null) {
                    double price = foodItem.getPrice();
                    tvPrice.setText(String.format("%,.0f", price));
                }
                tvPrice.setGravity(Gravity.CENTER);
                tvPrice.setPadding(8, 8, 8, 8);
                tableRow.addView(tvPrice);

                tableLayout.addView(tableRow);
            }
        }

        txtTTien.setText(String.format("Tổng tiền: %,.0f", totalAmount));

        imgBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Xác nhận đang chạy...", Toast.LENGTH_SHORT).show();
            confirmOrder(orderDetails, totalAmount, tableId);
        });

    }

    private FoodItem findFoodItemById(String itemFoodID) {
        for (FoodItem item : foodItems) {
            if (item.getItemFoodID().equals(itemFoodID)) {
                return item;
            }
        }
        return null;
    }

    private void confirmOrder(ArrayList<OrderDetail> orderDetails, double totalAmount, String tableId) {
        String orderId = "order_" + System.currentTimeMillis();
        String employeeId = "employee1";
        String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String orderStatus = "Chưa hoàn thành";
        String paymentStatus = "Chưa thanh toán";

        Order order = new Order(orderId, employeeId, orderDate, orderStatus, paymentStatus, tableId, totalAmount);

        db.collection("Order")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    saveOrderDetails(orderId, orderDetails);
                    updateTableStatus(tableId, "busy");

                    // Show a success message and redirect to TableListActivity
                    Toast.makeText(this, "Đơn hàng đã được xác nhận!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrderRequestActivity.this, TableListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi xác nhận đơn hàng: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private void saveOrderDetails(String orderId, ArrayList<OrderDetail> orderDetails) {
        if (orderDetails != null) {
            for (OrderDetail detail : orderDetails) {
                OrderDetail orderDetail = new OrderDetail(orderId, detail.getItemFoodID(), detail.getQuantity());

                db.collection("Order").document(orderId).collection("OrderDetails")
                        .add(orderDetail)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Chi tiết đơn hàng đã được lưu!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Lỗi khi lưu chi tiết đơn hàng: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(this, "Không có chi tiết đơn hàng để lưu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTableStatus(String tableId, String status) {
        db.collection("Tables")
                .document(tableId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Trạng thái bàn đã được cập nhật thành " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi cập nhật trạng thái bàn: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
