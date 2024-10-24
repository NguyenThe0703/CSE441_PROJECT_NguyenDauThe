package com.example.cse441_project.Category;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cse441_project.Dialog.EditSuccessDialog;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CategoryEditActivity extends AppCompatActivity {

    private EditText editCategoryName; // Tên danh mục
    private ImageView addImageView; // Hình ảnh danh mục
    private Button buttonSave; // Nút Lưu
    private Button buttonCancel; // Nút Hủy
    private Button btnBack; // Nút Trở về
    private String categoryId; // Mã danh mục

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy thông tin từ Intent
        categoryId = getIntent().getStringExtra("categoryId");

        // Khởi tạo các view
        editCategoryName = findViewById(R.id.editCategoryName); // Đảm bảo rằng ID là chính xác
        addImageView = findViewById(R.id.addImageView);
        buttonSave = findViewById(R.id.button_save);
        buttonCancel = findViewById(R.id.button_cancel);
        btnBack = findViewById(R.id.btnBack); // Khởi tạo nút trở về

        // Tải thông tin danh mục từ Firestore
        loadCategoryData();

        // Thiết lập sự kiện cho nút lưu
        buttonSave.setOnClickListener(v -> saveCategory());

        // Thiết lập sự kiện cho nút hủy
        buttonCancel.setOnClickListener(v -> finish()); // Đóng Activity

        // Thiết lập sự kiện cho nút trở về
        btnBack.setOnClickListener(v -> finish()); // Đóng Activity khi nhấn nút trở về
    }

    private void loadCategoryData() {
        db.collection("Category").document(categoryId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy dữ liệu từ document
                        String categoryName = documentSnapshot.getString("categoryName");
                        String categoryImage = documentSnapshot.getString("categoryImage");

                        // Cập nhật giao diện
                        editCategoryName.setText(categoryName);
                        // Sử dụng Glide để tải hình ảnh vào ImageView
                        Glide.with(this)
                                .load(categoryImage)
                                .placeholder(R.drawable.background_red) // Hình ảnh placeholder
                                .into(addImageView);
                    } else {
                        Log.d("CategoryEdit", "Danh mục không tồn tại");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting category", e);
                });
    }

    private void saveCategory() {
        String updatedName = editCategoryName.getText().toString();
        // Lấy đường dẫn hình ảnh nếu có (nếu không bạn có thể để lại một giá trị mặc định)
        String updatedImage = ""; // Thay đổi giá trị này nếu cần

        // Lưu lại thông tin đã chỉnh sửa vào Firestore
        db.collection("Category").document(categoryId)
                .update("categoryName", updatedName, "categoryImage", updatedImage) // Cập nhật cả tên và hình ảnh
                .addOnSuccessListener(aVoid -> {
                    // Hiển thị dialog thành công
                    EditSuccessDialog dialog = new EditSuccessDialog(this, "Thông báo", "Sửa thư mục thành công");
                    dialog.show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating category", e);
                });
    }
}
