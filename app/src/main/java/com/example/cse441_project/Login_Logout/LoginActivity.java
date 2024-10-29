package com.example.cse441_project.Login_Logout;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse441_project.Home.AddFoodActivity;
import com.example.cse441_project.Home.HomeActivity;
import com.example.cse441_project.Model.Employee;
import com.example.cse441_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser,edtPass;
    private  String UserName,PassWorld;
    private  String userDb,passDb;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        autoLogin();
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        // Khởi tạo nút đăng nhập và thiết lập sự kiện click
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy username và password từ EditText
                UserName = edtUser.getText().toString().trim();
                PassWorld = edtPass.getText().toString().trim();

                // Kiểm tra xem username và password có rỗng không
                if (UserName.isEmpty() || PassWorld.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username và Password không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkLoginCredentials();
            }
        });

    }
    private void checkLoginCredentials() {
        // Tạo tham chiếu tới Cloud Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả người dùng từ collection "employees"
        db.collection("Employees")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isValidUser = false;
                        // Duyệt qua các tài liệu trong collection
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class);
                            if (employee != null) {
                                String usernameFromDb = employee.getUsername(); // Lấy username từ đối tượng Employee
                                String passwordFromDb = employee.getPassword(); // Lấy password từ đối tượng Employee
                                Toast.makeText(LoginActivity.this, usernameFromDb + passwordFromDb, Toast.LENGTH_SHORT).show();
                                if (UserName.equals(usernameFromDb) && PassWorld.equals(passwordFromDb)) {
                                    isValidUser = true;

                                    // Lưu thông tin đăng nhập vào SharedPreferences
                                    saveLoginCredentials(UserName, PassWorld);

                                    break;
                                }
                            }
                        }
                        if (isValidUser) {
                            // Chuyển đến activity tiếp theo hoặc thực hiện hành động đăng nhập thành công
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AddFoodActivity.class);
                            startActivity(intent);
                        } else {
                            // Thông báo đăng nhập thất bại
                            Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý lỗi khi truy vấn không thành công
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi lấy dữ liệu từ Firestore thất bại
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Phương thức để lưu thông tin đăng nhập
    private void saveLoginCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
    private void autoLogin() {

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        if(username == null|| password==null)   return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Employees")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isValidUser = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class);
                            if (employee != null) {
                                String usernameFromDb = employee.getUsername(); // Lấy username từ đối tượng Employee
                                String passwordFromDb = employee.getPassword(); // Lấy password từ đối tượng Employee

                                if (username.equals(usernameFromDb) && password.equals(passwordFromDb)) {
                                    isValidUser = true;
                                    saveLoginCredentials(UserName, PassWorld);
                                    break;
                                }
                            }
                        }
                        if (isValidUser) {

                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AddFoodActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }



}