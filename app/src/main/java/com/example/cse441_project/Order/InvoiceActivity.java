package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.cse441_project.Home.HomeActivity;
import com.example.cse441_project.R;

public class InvoiceActivity extends AppCompatActivity {
    private Button printInvoiceButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        printInvoiceButton = findViewById(R.id.printInvoiceButton);

        printInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị dialog thông báo thành công
                FragmentManager fragmentManager = getSupportFragmentManager();
                SuccessDialogFragment successDialog = new SuccessDialogFragment();
                successDialog.show(fragmentManager, "SuccessDialog");
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, TableListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
