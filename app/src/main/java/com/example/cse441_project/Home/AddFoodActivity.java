package com.example.cse441_project.Home;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;

import com.example.cse441_project.Login_Logout.IntroActivity;
import com.example.cse441_project.Login_Logout.LoginActivity;
import com.example.cse441_project.MainActivity;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.Model.MenuCategory;
import com.example.cse441_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddFoodActivity extends AppCompatActivity {
    private AutoCompleteTextView listCategoryName;
    private EditText price,foodName;
    private Button addFood;
    private Button cancel;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;
    List<MenuCategory> category = new ArrayList<>();
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
        GetDateFormFirebase();
        getLastItemFoodId();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebaseStorage(imageUri);

            }
        });


    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(AddFoodActivity.this, "Vui lòng chọn ảnh trước khi thêm món ăn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khai báo Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Tạo tham chiếu cho hình ảnh
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        // Tải lên hình ảnh
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Nhận URL của hình ảnh đã tải lên
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Khi nhận được URL, lưu món ăn vào database
                                saveFoodToDatabase(downloadUrl.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi tải lên
                        Toast.makeText(AddFoodActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveFoodToDatabase(String imageUrl) {
        String selectedCategory = listCategoryName.getText().toString();
        String foodItemName = foodName.getText().toString();
        String priceString = price.getText().toString();

        // Kiểm tra thông tin đã nhập
        if (selectedCategory.isEmpty() || foodItemName.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(AddFoodActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double foodPrice = Double.parseDouble(priceString);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference("FoodItem");

            double id = Double.parseDouble(lastID);
            // Tạo ID cho món ăn mới

            FoodItem foodItem = new FoodItem(lastID, selectedCategory, foodPrice, findCategoryIdByName(selectedCategory), imageUrl);

            // Lưu món ăn vào database
            myRef.child(lastID).setValue(foodItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddFoodActivity.this, "Món ăn đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFoodActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFoodActivity.this, "Lỗi khi lưu món ăn!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(AddFoodActivity.this, "Giá tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }




    private void GetDateFormFirebase() {
        // Khai báo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MenuCategory");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa danh sách cũ để đảm bảo không bị trùng dữ liệu
                category.clear();

                // Duyệt qua tất cả các node con của "MenuCategory"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy đối tượng MenuCategory từ snapshot
                    MenuCategory menuCategory = snapshot.getValue(MenuCategory.class);
                    if (menuCategory != null) {
                        // Thêm tên danh mục vào danh sách
                        category.add(menuCategory);
                        categoryNames.add(menuCategory.getCategoryName());
                    }
                }

                // Sau khi lấy dữ liệu, gán nó vào AutoCompleteTextView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
                listCategoryName.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu việc lấy dữ liệu từ Firebase thất bại
                Log.e("Firebase", "Error retrieving data", databaseError.toException());
            }
        });

    }
    public String findCategoryIdByName(String categoryName) {
        for (MenuCategory menuCategory : category) {
            if (menuCategory.getCategoryName().equals(categoryName)) {
                return menuCategory.getCategoryId();
            }
        }
        return null;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                imageUri = data.getData(); // Lấy URI của hình ảnh đã chọn
                image.setImageURI(imageUri); // Hiển thị hình ảnh lên ImageView
            } else if (resultCode == RESULT_CANCELED) {
                // Người dùng đã hủy việc chọn hình ảnh
                Toast.makeText(this, "Bạn đã hủy chọn hình ảnh", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý trường hợp khác, chẳng hạn như không có dữ liệu
                Toast.makeText(this, "Có lỗi xảy ra khi chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastItemFoodId() {
        // Khai báo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foodItemsRef = database.getReference("FoodItem");

        // Truy vấn lấy itemFood cuối cùng
        foodItemsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String lastItemFoodId = snapshot.getKey();  // Lấy ra itemFoodID cuối cùng
                        lastID =  lastItemFoodId;

                    }
                } else {
                    lastID = "01";
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving last itemFoodId", databaseError.toException());
            }
        });
    }


}
