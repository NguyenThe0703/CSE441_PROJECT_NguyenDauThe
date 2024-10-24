package com.example.cse441_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class Category {
    private String categoryId; // Mã danh mục
    private String categoryName;  // Tên danh mục
    private String categoryImage; // URL/đường dẫn hình ảnh

    // Khởi tạo đối tượng Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Constructor mặc định
    public Category() {
    }

    // Constructor với ba tham số
    public Category(String categoryId, String categoryName, String categoryImage) {
        this.categoryId = categoryId; // Khởi tạo mã danh mục
        this.categoryName = categoryName; // Khởi tạo tên danh mục
        this.categoryImage = categoryImage; // Khởi tạo thuộc tính hình ảnh
    }

    // Constructor với hai tham số
    public Category(String categoryName, String categoryImage) {
        this.categoryName = categoryName; // Khởi tạo tên danh mục
        this.categoryImage = categoryImage; // Khởi tạo thuộc tính hình ảnh
    }

    // Getter và Setter cho các thuộc tính
    public String getCategoryId() {
        return categoryId; // Lấy mã danh mục
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId; // Thiết lập mã danh mục
    }

    public String getCategoryName() {
        return categoryName; // Lấy tên danh mục
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName; // Thiết lập tên danh mục
    }

    public String getCategoryImage() {
        return categoryImage; // Lấy thuộc tính hình ảnh
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage; // Thiết lập thuộc tính hình ảnh
    }

    // Lấy danh sách danh mục từ Firestore
    public void getAllCategories(final FirestoreCallback callback) {
        db.collection("categories") // Lấy dữ liệu từ collection "categories"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Category> categoryList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("categoryName"); // Lấy tên danh mục
                            String image = document.getString("categoryImage"); // Lấy hình ảnh từ Firestore
                            categoryList.add(new Category(name, image)); // Thêm danh mục vào danh sách
                        }
                        // Gọi callback khi dữ liệu được lấy thành công
                        callback.onCallback(categoryList);
                    } else {
                        // Xử lý lỗi
                        callback.onError(task.getException());
                    }
                });
    }

    // Interface callback để xử lý dữ liệu trả về
    public interface FirestoreCallback {
        void onCallback(ArrayList<Category> categoryList); // Gọi callback với danh sách danh mục
        void onError(Exception e); // Gọi callback khi có lỗi xảy ra
    }
}
