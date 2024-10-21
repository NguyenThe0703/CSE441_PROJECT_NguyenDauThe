package com.example.cse441_project.Login_Logout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse441_project.Home.AddFoodActivity;
import com.example.cse441_project.Model.Employee;
import com.example.cse441_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Button startbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.intro_activity);

        // Khởi tạo FirebaseAuth và FirebaseFirestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        startbtn = findViewById(R.id.btnStart);
//        Test2();
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, AddFoodActivity.class);
                startActivity(intent);
            }
        });
        Employee newEmployee = new Employee("1", "Admin", "Admin", "admin", "Hoàng Quốc Huy", "Male", "1990-01-01", "123456789");
        addEmployeeToFirestore(newEmployee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
//    private void Test2() {
//        // Tạo đối tượng MenuCategory
//
//
//        // Lấy instance của Firebase Database
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
////        // Tham chiếu đến node "MenuCategory" trong database
//        DatabaseReference myRef = db.getReference("MenuCategory");
//        DatabaseReference newID = myRef.push();
//        // Đẩy dữ liệu lên Firebase, sử dụng setValue() để lưu đối tượng MenuCategory
//        MenuCategory menuCategory = new MenuCategory(newID.getKey(), "Đồ ăn vặt");
//        myRef.child(menuCategory.getCategoryId()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Dữ liệu đã tồn tại, không ghi đè
//                    Log.d("Firebase", "Data already exists, not pushing");
//                } else {
//                    // Dữ liệu chưa tồn tại, có thể đẩy dữ liệu mới lên
//                    myRef.child(menuCategory.getCategoryId()).setValue(menuCategory)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    // Xử lý khi đẩy dữ liệu thành công
//                                    Log.d("Firebase", "Data pushed successfully");
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra
//                Log.e("Firebase", "Error reading data", databaseError.toException());
//            }
//        });
//    }
public void addEmployeeToFirestore(Employee employee) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Tạo document với employeeId làm tên document
    db.collection("Employees").document(employee.getEmployeeId())
            .set(employee.toMap())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Thành công
                    Log.d("Firestore", "Employee added successfully");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Thất bại
                    Log.w("Firestore", "Error adding employee", e);
                }
            });
}
}
