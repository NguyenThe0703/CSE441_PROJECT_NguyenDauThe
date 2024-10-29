package com.example.cse441_project.Login_Logout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse441_project.Home.AddFoodActivity;
import com.example.cse441_project.Home.HomeActivity;
import com.example.cse441_project.Model.Category;
import com.example.cse441_project.Model.Employee;
import com.example.cse441_project.Model.Order;
import com.example.cse441_project.Model.OrderDetail;
import com.example.cse441_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
        autoLogin();
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        uploadSampleData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
//    private void Test2() {
//        // Khởi tạo FirebaseFirestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Tạo đối tượng MenuCategory với ID tự động tạo bằng phương thức .document()
//        DocumentReference newID = db.collection("Category").document();
//        Category menuCategory = new Category(newID.getId(), "Đồ ăn vặt");
//
//
//        newID.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // Dữ liệu đã tồn tại
//                    Log.d("Firestore", "Data already exists, not pushing");
//                } else {
//                    // Dữ liệu chưa tồn tại, đẩy dữ liệu lên Firestore
//                    newID.set(menuCategory)
//                            .addOnSuccessListener(aVoid -> {
//                                Log.d("Firestore", "Data pushed successfully");
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e("Firestore", "Error pushing data", e);
//                            });
//                }
//            } else {
//                Log.e("Firestore", "Error checking document existence", task.getException());
//            }
//        });
//    }
//    public void uploadSampleData() {
//        // Tạo dữ liệu mẫu cho Order và OrderDetail
//        List<Order> orders = new ArrayList<>();
//        List<OrderDetail> orderDetails = new ArrayList<>();
//
//        for (int i = 1; i <= 5; i++) {
//            String orderId = "order" + i;
//
//            // Tạo một Order
//            Order order = new Order();
//            order.setOrderId(orderId);
//            order.setEmployeeId("employee" + i);
//            order.setOrderDate("2023-10-2" + i);
//            order.setOrderStatus("Đã thanh toán");
//            order.setPaymentStatus("Đã trả");
//            order.setTableId("table" + i);
//            order.setTotalAmount(500000 + i * 10000);
//            orders.add(order);
//
//            // Tạo chi tiết cho từng Order (kết nối qua orderId)
//            for (int j = 1; j <= 3; j++) {
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrderId(orderId);
//                orderDetail.setMenuItemId("foodItem" + j);
//                orderDetail.setQuantity(j);
//                orderDetails.add(orderDetail);
//            }
//        }
//
//        // Đẩy dữ liệu lên Firestore
//        uploadOrdersToFirestore(orders, orderDetails);
//    }
//    private void uploadOrdersToFirestore(List<Order> orders, List<OrderDetail> orderDetails) {
//        FirebaseFirestore db;
//        db = FirebaseFirestore.getInstance();
//        for (Order order : orders) {
//            db.collection("Order").document(order.getOrderId())
//                    .set(order)
//                    .addOnSuccessListener(aVoid -> {
//                        System.out.println("Order added: " + order.getOrderId());
//                    })
//                    .addOnFailureListener(e -> {
//                        System.err.println("Error adding order: " + e.getMessage());
//                    });
//        }
//
//        for (OrderDetail orderDetail : orderDetails) {
//            db.collection("OrderDetail").document()
//                    .set(orderDetail)
//                    .addOnSuccessListener(aVoid -> {
//                        System.out.println("OrderDetail added for Order ID: " + orderDetail.getOrderId());
//                    })
//                    .addOnFailureListener(e -> {
//                        System.err.println("Error adding order detail: " + e.getMessage());
//                    });
//        }
//    }

//public void addEmployeeToFirestore(Employee employee) {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    // Tạo document với employeeId làm tên document
//    db.collection("Employees").document(employee.getEmployeeId())
//            .set(employee.toMap())
//            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    // Thành công
//                    Log.d("Firestore", "Employee added successfully");
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    // Thất bại
//                    Log.w("Firestore", "Error adding employee", e);
//                }
//            });
//}
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

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class);
                            if (employee != null) {
                                String usernameFromDb = employee.getUsername(); // Lấy username từ đối tượng Employee
                                String passwordFromDb = employee.getPassword(); // Lấy password từ đối tượng Employee

                                if (username.equals(usernameFromDb) && password.equals(passwordFromDb)) {

                                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, HomeActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                            }
                        }
                    }
                });
    }
}
