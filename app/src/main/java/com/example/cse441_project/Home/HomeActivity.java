package com.example.cse441_project.Home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<FoodItem> foodItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        loadFoodItemsFromFirestore();

        foodItemList = new ArrayList<>();
        adapter = new HomeAdapter(foodItemList);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            addFragment(new HomeFragment());
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_fragment, fragment);
        fragmentTransaction.commit();
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
                        Toast.makeText(HomeActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}


