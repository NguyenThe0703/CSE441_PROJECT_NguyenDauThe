package com.example.cse441_project.Login_Logout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse441_project.Model.Employee;
import com.example.cse441_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.intro_activity);

        // Khởi tạo FirebaseAuth và FirebaseFirestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        registerUser();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser() {
        // Thay thế với email và mật khẩu hợp lệ
        firebaseAuth.createUserWithEmailAndPassword("admin@gmail.com", "admin1234")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Tạo thông tin người dùng
                        Employee employee = new Employee("001", "admin", "admin", "admin123", "Nguyen Van A", "Nam", "27/09/2000", "0293712334");

                        // Lấy UID của người dùng đã đăng ký
                        String uid = firebaseAuth.getCurrentUser().getUid();

                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("NhanVien");

                        reference.child(uid).setValue(employee)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(IntroActivity.this, "Đăng ký thành công và đã lưu dữ liệu!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(IntroActivity.this, "Lỗi khi lưu thông tin!", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Xử lý lỗi đăng ký
                        Toast.makeText(IntroActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
