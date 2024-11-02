package com.example.cse441_project.FoodItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.Adapter.HomeAdapter;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopSaleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<FoodItem> foodItemList = new ArrayList<>();
    private Map<String, Integer> foodSalesCount = new HashMap<>();
    private ImageView imgBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_sale_activity);

        imgBack = findViewById(R.id.img_back);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeAdapter(foodItemList);
        fetchTopSellingItems();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        imgBack.setOnClickListener(v->{
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        });

    }

    public void fetchTopSellingItems() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OrderDetail")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String itemFoodID = document.getString("itemFoodID");
                        int quantity = document.getLong("quantity").intValue();
                        foodSalesCount.put(itemFoodID, foodSalesCount.getOrDefault(itemFoodID, 0) + quantity);
                    }
                    List<String> topSellingFoodIds = foodSalesCount.entrySet().stream()
                            .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                            .limit(6)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
                    fetchFoodDetails(topSellingFoodIds);
                })
                .addOnFailureListener(e -> Log.e("TopSaleActivity", "Error fetching order details", e));
    }

    private void fetchFoodDetails(List<String> topSellingFoodIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("FoodItem")
                .whereIn("itemFoodID", topSellingFoodIds)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        FoodItem foodItem = document.toObject(FoodItem.class);
                        // Thêm món ăn vào danh sách và in ra số lượng đã bán
                        foodItemList.add(foodItem);
                        Toast.makeText(TopSaleActivity.this, "1", Toast.LENGTH_SHORT).show();
                        Log.d("TopSaleActivity", "Food Item: " + foodItem.getFoodName() + ", Sold Quantity: " +
                                foodSalesCount.get(foodItem.getItemFoodID()));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("TopSaleActivity", "Error fetching food item details", e));
    }
}
