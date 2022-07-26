package com.muhsanapps.ecommerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muhsanapps.ecommerce.R;
import com.muhsanapps.ecommerce.activities.CategoryActivity;
import com.muhsanapps.ecommerce.databinding.ItemCategoniesBinding;
import com.muhsanapps.ecommerce.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categonies,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category = categories.get(position);
        holder.binding.label.setText(Html.fromHtml(category.getName()));

        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);

        // "#fd000
        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("catId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoniesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoniesBinding.bind(itemView);
        }
    }
}
