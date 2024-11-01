package com.example.cse441_project.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.Home.HomeActivity;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.Model.Category;
import com.example.cse441_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class  AddFoodActivity extends AppCompatActivity {
    private AutoCompleteTextView listCategoryName;
    private EditText price, foodName;
    private Button addFood;
    private Button cancel;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;
    List<Category> category = new ArrayList<>();
    List<String> categoryNames = new ArrayList<>();
    private Uri imageUri;
    private String lastID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_activity);
        listCategoryName = findViewById(R.id.listCategoryname);
        price = findViewById(R.id.price);
        foodName = findViewById(R.id.foodName);
        addFood = findViewById(R.id.addFood);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.imageFood);

        GetCategoriesFromFirestore();  // Lấy dữ liệu từ Firestore
        getLastItemFoodId();  // Lấy ID món ăn cuối cùng
        image.setOnClickListener(v -> openGallery());

        addFood.setOnClickListener(v -> uploadImageToFirebaseStorage(imageUri));
    }

    // Hàm tải hình ảnh lên Firebase Storage
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(AddFoodActivity.this, "Vui lòng chọn ảnh trước khi thêm món ăn!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    // Lưu URL của hình ảnh vào Firestore
                    saveFoodToFirestore(downloadUrl.toString());
                }))
                .addOnFailureListener(e -> Toast.makeText(AddFoodActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    // Hàm lưu món ăn vào Firestore
    private void saveFoodToFirestore(String imageUrl) {
        String selectedCategory = listCategoryName.getText().toString();
        String foodItemName = foodName.getText().toString();
        String priceString = price.getText().toString();

        if (selectedCategory.isEmpty() || foodItemName.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(AddFoodActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double foodPrice = Double.parseDouble(priceString);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FoodItem foodItem = new FoodItem(lastID, foodItemName, foodPrice, findCategoryIdByName(selectedCategory), imageUrl);

            // Lưu món ăn vào Firestore
            db.collection("FoodItem").document(lastID).set(foodItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddFoodActivity.this, "Món ăn đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddFoodActivity.this, HomeActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddFoodActivity.this, "Lỗi khi lưu món ăn!", Toast.LENGTH_SHORT).show());
        } catch (NumberFormatException e) {
            Toast.makeText(AddFoodActivity.this, "Giá tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }


    // Lấy danh mục từ Firestore
    private void GetCategoriesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Category").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    category.clear();
                    categoryNames.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Category menuCategory = document.toObject(Category.class);
                        if (menuCategory != null) {
                            category.add(menuCategory);
                            categoryNames.add(menuCategory.getCategoryName());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
                    listCategoryName.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error retrieving data", e));
    }

    // Tìm ID của danh mục theo tên
    public String findCategoryIdByName(String categoryName) {
        for (Category category : this.category) {
            if (category.getCategoryName().equals(categoryName)) {
                return category.getCategoryId();
            }
        }
        return null;
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Lấy URI của hình ảnh được chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                imageUri = data.getData();
                image.setImageURI(imageUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Bạn đã hủy chọn hình ảnh", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lấy ID món ăn cuối cùng từ Firestore
// Lấy ID món ăn cuối cùng từ Firestore
    private void getLastItemFoodId() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn Firestore, sắp xếp theo trường "itemFoodID" giảm dần và lấy phần tử đầu tiên (lớn nhất)
        db.collection("FoodItem").orderBy("itemFoodID", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Lấy tài liệu đầu tiên
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Lấy giá trị của trường "itemFoodID" từ tài liệu
                        String lastItemFoodId = document.getString("itemFoodID");

                        try {
                            // Chuyển đổi ID thành số nguyên
                            int id = Integer.parseInt(lastItemFoodId);
                            id += 1;  // Tăng giá trị ID lên 1
                            lastID = String.format("%03d", id); // Định dạng ID với 3 chữ số (có thể điều chỉnh tùy theo nhu cầu)

                        } catch (NumberFormatException e) {
                            Log.e("Firestore", "Error parsing last itemFoodId: " + lastItemFoodId, e);
                            lastID = "001";  // Gán giá trị mặc định nếu có lỗi
                        }
                    } else {
                        lastID = "001";  // Gán giá trị mặc định nếu không có tài liệu nào
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error retrieving last itemFoodId", e));
    }

}
