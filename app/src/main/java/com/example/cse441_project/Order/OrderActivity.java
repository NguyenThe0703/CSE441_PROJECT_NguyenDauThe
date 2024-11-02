package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.Model.OrderDetail;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderFoodAdapter adapter;
    private List<FoodItem> foodItemList;
    private DrawerLayout drawerLayout;
    private Button datmon;
    private ImageView searchImageView;
    private TextView tvTableId; // Display table ID

    private String tableId; // To store the tableId received from Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_food);

        // Retrieve the tableId from the Intent
        tableId = getIntent().getStringExtra("tableId");

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchImageView = findViewById(R.id.imageView3);
        datmon = findViewById(R.id.btnGoiMon);
        tvTableId = findViewById(R.id.tvTableId); // Initialize table ID TextView

        // Display the table ID
        if (tableId != null) {
            tvTableId.setText("Table: " + tableId);
        } else {
            tvTableId.setText("Table ID not found");
        }

        // Set up GridLayout for the food item list
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        foodItemList = new ArrayList<>();
        adapter = new OrderFoodAdapter(foodItemList);
        recyclerView.setAdapter(adapter);

        // Load data from Firestore
        loadFoodItemsFromFirestore();

        // Order button click event
        datmon.setOnClickListener(v -> {
            if (tableId == null) {
                Toast.makeText(OrderActivity.this, "Table ID not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the list of OrderDetail from the adapter
            List<OrderDetail> orderDetails = adapter.getOrderDetails(tableId);

            if (orderDetails.isEmpty()) {
                Toast.makeText(OrderActivity.this, "Please select at least one item.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an Intent and pass tableId, orderDetails, and foodItems to OrderRequestActivity
            Intent intent = new Intent(OrderActivity.this, OrderRequestActivity.class);
            intent.putExtra("tableId", tableId); // Pass the tableId to OrderRequestActivity
            intent.putExtra("orderDetails", new ArrayList<>(orderDetails)); // Pass OrderDetail list
            intent.putExtra("foodItems", new ArrayList<>(foodItemList)); // Pass FoodItem list
            startActivity(intent);
        });
    }

    private void loadFoodItemsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("FoodItem")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert document to FoodItem object
                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem); // Add to list
                        }
                        adapter.notifyDataSetChanged(); // Update adapter
                    } else {
                        Toast.makeText(OrderActivity.this, "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(OrderActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
