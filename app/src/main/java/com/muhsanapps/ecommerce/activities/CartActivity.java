package com.muhsanapps.ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.muhsanapps.ecommerce.adapters.CartAdapter;
import com.muhsanapps.ecommerce.databinding.ActivityCartBinding;
import com.muhsanapps.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();

        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()){
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                products.add(product);
        }

        //products.add(new Product("Product1","___","123",45,45,45,296));
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {// quantity
                binding.subtotal.setText(String.format("PRK %.2f", cart.getTotalPrice()));

            }
        });

        binding.cartList.setLayoutManager(new LinearLayoutManager(this));
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("PRK %.2f", cart.getTotalPrice()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.continueBtn.setOnClickListener(view -> {
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}