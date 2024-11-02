package com.example.cse441_project.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.cse441_project.Model.Table;
import com.example.cse441_project.Adapter.TableAdapter;
import com.example.cse441_project.R;
import com.example.cse441_project.FoodItem.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableListActivity extends AppCompatActivity {
    private GridView tableGridView;
    private List<Table> tableList;
    private FirebaseFirestore db;
    private TableAdapter adapter;
    private TextView statusMessageTextView;
    private ImageView backButton;
    private Button addTableButton, orderButton, viewInvoiceButton;
    private LinearLayout actionButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        tableGridView = findViewById(R.id.tableGridView);
        statusMessageTextView = findViewById(R.id.statusMessageTextView);
        backButton = findViewById(R.id.backButton);
        addTableButton = findViewById(R.id.addTableButton);
        actionButtonsLayout = findViewById(R.id.actionButtonsLayout);
        orderButton = findViewById(R.id.orderButton);
        viewInvoiceButton = findViewById(R.id.viewInvoiceButton);

        tableList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        adapter = new TableAdapter(this, tableList);
        tableGridView.setAdapter(adapter);

        loadTables();

        tableGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Table selectedTable = tableList.get(position);
                updateStatusMessage(selectedTable.getStatus());

                // Hiển thị các nút hành động nếu bàn chưa thanh toán hoặc có khách
                if ("reserved".equals(selectedTable.getStatus()) || "busy".equals(selectedTable.getStatus()) || "available".equals(selectedTable.getStatus()) ) {
                    actionButtonsLayout.setVisibility(View.VISIBLE);
                } else {
                    actionButtonsLayout.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý nút Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút Thêm bàn
        addTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddTableFragment addTableFragment = new AddTableFragment();
                addTableFragment.show(fragmentManager, "AddTableFragment");
            }
        });

        // Xử lý nút Gọi món
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TableListActivity.this, "Gọi món", Toast.LENGTH_SHORT).show();
                // Thêm logic gọi món tại đây
            }
        });

        // Xử lý nút Xem hóa đơn
        viewInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableListActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void loadTables() {
        db.collection("Tables")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tableList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tableId = document.getString("tableId");
                            String tableName = document.getString("tableName");
                            String status = document.getString("status");
                            tableList.add(new Table(tableId, tableName, status));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TableListActivity.this, "Không thể tải dữ liệu từ Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateStatusMessage(String status) {
        switch (status) {
            case "available":
                statusMessageTextView.setText("Bàn trống");
                break;
            case "reserved":
                statusMessageTextView.setText("Bàn chưa thanh toán");
                break;
            case "busy":
                statusMessageTextView.setText("Bàn đang có khách");
                break;
            default:
                statusMessageTextView.setText("");
                break;
        }
    }
}
