package com.muhsanapps.ecommerce.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.muhsanapps.ecommerce.R;
import com.muhsanapps.ecommerce.databinding.ItemCartBinding;
import com.muhsanapps.ecommerce.databinding.QuantityDialogBinding;
import com.muhsanapps.ecommerce.model.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<Product> products;
    CartListener cartListener;

    Cart cart;
    public interface CartListener {
        public void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener) {
        this.context = context;
        this.cartListener = cartListener;
        this.products = products;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.imageView2);

        holder.binding.productName.setText(product.getName());
        holder.binding.price.setText("PKR " + product.getPrice());
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        holder.itemView.setOnClickListener(view -> {
            QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(quantityDialogBinding.getRoot())
                    .create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));


            quantityDialogBinding.productNametv.setText(product.getName());
            quantityDialogBinding.productStock.setText("Stock: " + product.getStock());
            quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));

            int stock = product.getStock();

            quantityDialogBinding.itemsPlus.setOnClickListener(view1 -> {
                int quantity = product.getQuantity();
                quantity++;
                if (quantity > product.getStock()){
                    Toast.makeText(context, "Max stock available:  " + product.getStock(), Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    product.setQuantity(quantity);
                    quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                }
                notifyDataSetChanged();
                cart.updateItem(product, product.getQuantity());
                cartListener.onQuantityChanged();
            });

            quantityDialogBinding.itemsSubtraction.setOnClickListener(view1 -> {
                int quantity = product.getQuantity();
                if (quantity > 2)
                    quantity--;
                product.setQuantity(quantity);
                quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                notifyDataSetChanged();
                cart.updateItem(product, product.getQuantity());
                cartListener.onQuantityChanged();
            });

            quantityDialogBinding.SaveItems.setOnClickListener(view1 -> {

                dialog.dismiss();
//                notifyDataSetChanged();
//                cart.updateItem(product, product.getQuantity());
//                cartListener.onQuantityChanged();

            });


            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =ItemCartBinding.bind(itemView);
        }
    }
}
