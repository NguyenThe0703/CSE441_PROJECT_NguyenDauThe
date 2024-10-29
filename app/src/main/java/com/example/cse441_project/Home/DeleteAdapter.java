package com.example.cse441_project.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import com.bumptech.glide.Glide;
import com.example.cse441_project.Model.FoodItem;
import com.example.cse441_project.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.List;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ViewHolder> {

    private List<FoodItem> foodItemList;
    private Context context;

    public DeleteAdapter(Context context, List<FoodItem> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false); // Đổi 'your_linear_layout_layout' với tên layout của bạn
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.textView1.setText(foodItem.getFoodName());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(foodItem.getPrice()) + "đ";

        holder.textView2.setText(formattedPrice);
        Glide.with(holder.imageView.getContext())
                .load(foodItem.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            // Tạo AlertDialog để xác nhận xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xóa món ăn")
                    .setMessage("Bạn có chắc chắn muốn xóa món ăn này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {

                        deleteFoodItem(foodItem.getItemFoodID(), position);
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
    private void deleteFoodItem(String itemFoodID, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("FoodItem").document(itemFoodID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã xóa món ăn thành công!", Toast.LENGTH_SHORT).show();
                    // Xóa item khỏi danh sách và cập nhật adapter
                    foodItemList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi khi xóa món ăn!", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myImageView);
            textView1 = itemView.findViewById(R.id.myTextView1);
            textView2 = itemView.findViewById(R.id.myTextView2);
        }
    }
}

