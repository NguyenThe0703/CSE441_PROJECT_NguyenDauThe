package com.example.cse441_project.Home;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cse441_project.R;

import com.example.cse441_project.Model.FoodItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<FoodItem> foodItemList;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        imgMenu = findViewById(R.id.menu_img);

        loadFoodItemsFromFirestore();

        foodItemList = new ArrayList<>();
        adapter = new HomeAdapter(foodItemList);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            addFragment(new HomeFragment());
        }
        imgMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_manage_food) {
                    View view = findViewById(R.id.nav_manage_food);
                    PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
                    popupMenu.getMenuInflater().inflate(R.menu.sub_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem submenuItem) {
                            if (submenuItem.getItemId() == R.id.add) {
                                startActivity(new Intent(HomeActivity.this, AddFoodActivity.class));
                                return true;
                            } else if (submenuItem.getItemId() == R.id.edit) {
                                startActivity(new Intent(HomeActivity.this, SearchEditFood.class));

                                return true;
                            } else if (submenuItem.getItemId() == R.id.delete) {
                                startActivity(new Intent(HomeActivity.this, DeleteFood.class));
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    popupMenu.show();

                } else if (item.getItemId() == R.id.nav_manage_food_type) {
                    startActivity(new Intent(HomeActivity.this, AddFoodActivity.class));
                } else if (item.getItemId() == R.id.nav_manage_table) {
                    startActivity(new Intent(HomeActivity.this, AddFoodActivity.class));
                } else if (item.getItemId() == R.id.nav_top_selling_items) {
                    startActivity(new Intent(HomeActivity.this, TopSaleActivity.class));
                } else if (item.getItemId() == R.id.nav_revenue_statistics) {
                    startActivity(new Intent(HomeActivity.this, RevenueActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Item không xác định", Toast.LENGTH_SHORT).show();
                }

//                drawerLayout.closeDrawer(GravityCompat.START); // Đóng ngăn kéo sau khi chọn
                return true; // Trả về true để xác nhận sự kiện đã được xử lý
            }
        });

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


