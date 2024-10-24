package com.example.cse441_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse441_project.Model.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Tham chiếu tới GridLayout từ XML
        gridLayout = findViewById(R.id.grCategory);

        // Khởi tạo Category để lấy dữ liệu
        Category category = new Category();
        category.getAllCategories(new Category.FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Category> categoryList) {
                displayCategories(categoryList);
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi
            }
        });
    }

    // Hiển thị danh mục lên giao diện
    private void displayCategories(ArrayList<Category> categoryList) {
        for (Category category : categoryList) {
            // Tạo một LinearLayout mới cho mỗi danh mục
            LinearLayout categoryLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.category, null);
            TextView categoryName = categoryLayout.findViewById(R.id.txtcategoryName);
            categoryName.setText(category.getCategoryName());

            // Thêm categoryLayout vào GridLayout
            gridLayout.addView(categoryLayout);
        }
    }
}

