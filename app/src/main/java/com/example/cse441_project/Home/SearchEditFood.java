package com.example.cse441_project.Home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchEditFood extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditAdapter adapter;
    private List<FoodItem> foodItemList;
    EditText searchEditText ;
    private Button searchButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_edit_food);
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.search_edt);
        searchButton = findViewById(R.id.search_btn);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        searchButton.setOnClickListener(v -> {
            String keyword = searchEditText.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchFoodItems(keyword);
            } else {
                Toast.makeText(SearchEditFood.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
        loadFoodItemsFromFirestore();
        if (savedInstanceState == null) {
            addFragment(new HomeFragment());
        }
        foodItemList = new ArrayList<>();
        adapter = new EditAdapter(SearchEditFood.this,foodItemList);
        recyclerView.setAdapter(adapter);

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
                            foodItemList.add(foodItem);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SearchEditFood.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SearchEditFood.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void searchFoodItems(String keyword) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn với tên trường bạn muốn tìm kiếm, ví dụ "name"
        db.collection("FoodItem")
                .whereGreaterThanOrEqualTo("foodName", keyword)
                .whereLessThanOrEqualTo("foodName", keyword + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        foodItemList.clear(); // Xóa danh sách cũ để cập nhật dữ liệu mới
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SearchEditFood.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SearchEditFood.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_fragment, fragment);
        fragmentTransaction.commit();
    }
}
