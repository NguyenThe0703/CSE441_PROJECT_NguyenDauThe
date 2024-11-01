package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.R;

public class OrderRequestActivity extends AppCompatActivity {

    private TextView tvTableNumber, txtTTien;
    private Button btnCancel, btnConfirm;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processing_order_request);

        // Khởi tạo các thành phần giao diện
        tvTableNumber = findViewById(R.id.tvTableNumber);
        txtTTien = findViewById(R.id.txtTTien);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        imgBack = findViewById(R.id.imgBack);

        // Lấy dữ liệu từ Intent
        String tableNumber = "Bàn số: " + getIntent().getStringExtra("tableNumber"); // Bạn có thể truyền thêm dữ liệu khác
        tvTableNumber.setText(tableNumber);
        // Cập nhật tổng tiền tại đây (nếu cần)

        // Xử lý sự kiện quay lại
        imgBack.setOnClickListener(v -> finish()); // Để quay lại Activity trước đó

        // Xử lý sự kiện hủy đơn
        btnCancel.setOnClickListener(v -> {
            // Logic hủy đơn
            finish(); // Quay lại Activity trước
        });

        // Xử lý sự kiện xác nhận đơn
        btnConfirm.setOnClickListener(v -> {
            // Logic xác nhận đơn
            // Có thể chuyển sang Activity khác nếu cần
        });
    }
}
