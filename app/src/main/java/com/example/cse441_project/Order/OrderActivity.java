package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.Category.CategoryActivity;
import com.example.cse441_project.Home.AddFoodActivity;
import com.example.cse441_project.Home.CategoryAdapter;
import com.example.cse441_project.Model.Category;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;



public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView, rcvCategory;
    private OrderFoodAdapter adapter;
    private List<FoodItem> foodItemList;
    private DrawerLayout drawerLayout;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();

    private ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_food); // Đảm bảo layout này là order_food

        // Khởi tạo các thành phần giao diện
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawer_layout);
        rcvCategory = findViewById(R.id.rcv_category);
        searchImageView = findViewById(R.id.imageView3);

        // Thiết lập GridLayout cho danh sách món ăn
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Thiết lập LinearLayout cho danh sách thể loại
        rcvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodItemList = new ArrayList<>();
        adapter = new OrderFoodAdapter(foodItemList);
        recyclerView.setAdapter(adapter);

        categoryAdapter = new CategoryAdapter(categoryList);
        rcvCategory.setAdapter(categoryAdapter);

        loadFoodItemsFromFirestore();
        fetchCategoriesFromFirestore();

    }

    private void loadFoodItemsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu từ collection "FoodItem"
        db.collection("FoodItem")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Chuyển đổi tài liệu thành đối tượng FoodItem
                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem); // Thêm vào danh sách
                        }
                        adapter.notifyDataSetChanged(); // Cập nhật adapter
                    } else {
                        Toast.makeText(OrderActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchCategoriesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Category")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Category category = document.toObject(Category.class);
                        categoryList.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("OrderActivity", "Error fetching categories", e));
    }
}
