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

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser,edtPass;
    private  String UserName,PassWorld;
    private  String userDb,passDb;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        // Khởi tạo EditText

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

                // Gọi phương thức để kiểm tra thông tin đăng nhập
                checkLoginCredentials();
            }
        });

    }

    private void checkLoginCredentials() {
        // Tạo tham chiếu tới Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Employee");

        // Lắng nghe dữ liệu tại nhánh "Employee"
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidUser = false;

                // Duyệt qua các dữ liệu con
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu và chuyển đổi thành đối tượng Employee
                    Employee employee = snapshot.getValue(Employee.class);
                    if (employee != null) {
                        String usernameFromDb = employee.getUsername(); // Thay bằng phương thức thực tế lấy username
                        String passwordFromDb = employee.getPassword(); // Thay bằng phương thức thực tế lấy password

                        // Kiểm tra thông tin đăng nhập
                        if (UserName.equals(usernameFromDb) && PassWorld.equals(passwordFromDb)) {
                            isValidUser = true;

                            // Lưu thông tin đăng nhập vào SharedPreferences
                            saveLoginCredentials(usernameFromDb, passwordFromDb);

                            break;
                        }
                    }
                }
                if (isValidUser) {
                    // Chuyển đến activity tiếp theo hoặc thực hiện hành động đăng nhập thành công
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    // Thông báo đăng nhập thất bại
                    Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không đúng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi truy vấn bị hủy
                Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply(); // Lưu thông tin vào SharedPreferences
    }


}
