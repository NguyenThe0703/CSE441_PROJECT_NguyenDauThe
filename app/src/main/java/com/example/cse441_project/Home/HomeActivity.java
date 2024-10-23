package com.example.cse441_project.Home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
    private AutoCompleteTextView searchEdt;
    private List<String> listFoodItemName = new ArrayList<>();
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);
        searchEdt = findViewById(R.id.edt_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        btnSearch = findViewById(R.id.btn_search);

        foodItemList = new ArrayList<>();
        loadFoodItemsFromFirestore();


        adapter = new HomeAdapter(foodItemList);
        recyclerView.setAdapter(adapter);
        ArrayAdapter<String> names = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listFoodItemName);
        searchEdt.setAdapter(names);
        btnSearch.setOnClickListener(v -> searchFood(searchEdt.getText().toString()));
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
        foodItemList.clear();
        listFoodItemName.clear();
        // Lấy dữ liệu từ collection "FoodItem"
        db.collection("FoodItem")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem);
                            listFoodItemName.add(foodItem.getFoodName());
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HomeActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void searchFood(String foodName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Clear the current list before adding new search results
        foodItemList.clear();

        // Tìm các tên món ăn chứa từ khóa nhập vào (không phân biệt chữ hoa/thường)
        db.collection("FoodItem")
                .orderBy("foodName")
                .startAt(foodName)
                .endAt(foodName + "\uf8ff")  // Sử dụng ký tự lớn nhất có thể để bao phủ tất cả kết quả liên quan
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem);  // Add the matched items to the list
                        }

                        adapter.notifyDataSetChanged();  // Notify the adapter about data changes
                    } else {
                        Toast.makeText(HomeActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}


