package com.example.cse441_project.Order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderFoodAdapter extends RecyclerView.Adapter<OrderFoodAdapter.ViewHolder> {

    private List<FoodItem> foodItemList;

    public OrderFoodAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_order_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);

        // Thiết lập tên và giá món ăn
        holder.textView1.setText(foodItem.getFoodName());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(foodItem.getPrice()) + "đ";
        holder.textView2.setText(formattedPrice);

        // Hiển thị hình ảnh món ăn
        Glide.with(holder.imageView.getContext())
                .load(foodItem.getImageUrl())
                .into(holder.imageView);

        // Thiết lập số lượng ban đầu là 1
        holder.editTextQuantity.setText("1");

        // Xử lý sự kiện nút trừ
        holder.buttonMinus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                holder.editTextQuantity.setText(String.valueOf(quantity));
            }
        });

        // Xử lý sự kiện nút cộng
        holder.buttonPlus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            quantity++;
            holder.editTextQuantity.setText(String.valueOf(quantity));
        });

        // Xử lý sự kiện nút Gọi món
        holder.BtnGoiMon.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OrderRequestActivity.class); // Thay YourNewActivity bằng Activity mới bạn muốn mở
            intent.putExtra("foodName", foodItem.getFoodName()); // Truyền dữ liệu nếu cần
            intent.putExtra("quantity", holder.editTextQuantity.getText().toString());
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2;
        Button buttonMinus, buttonPlus;
        EditText editTextQuantity;
        Button BtnGoiMon;

        public ViewHolder(View itemView) {
            super(itemView);
            // Sử dụng ID từ layout activity_order_food
            imageView = itemView.findViewById(R.id.myImageView);
            textView1 = itemView.findViewById(R.id.myTextView1);
            textView2 = itemView.findViewById(R.id.myTextView2);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
            editTextQuantity = itemView.findViewById(R.id.edittext_quantity);
            BtnGoiMon = itemView.findViewById(R.id.btnGoiMon);
        }
    }
}
