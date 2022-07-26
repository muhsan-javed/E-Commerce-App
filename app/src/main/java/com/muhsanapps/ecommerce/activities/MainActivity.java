package com.muhsanapps.ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.muhsanapps.ecommerce.R;
import com.muhsanapps.ecommerce.adapters.CategoryAdapter;
import com.muhsanapps.ecommerce.adapters.ProductAdapter;
import com.muhsanapps.ecommerce.databinding.ActivityMainBinding;
import com.muhsanapps.ecommerce.model.Category;
import com.muhsanapps.ecommerce.model.Product;
import com.muhsanapps.ecommerce.utils.Constants;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProducts();
        initSlider();


    }

    void  getRecentOffers() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {

                JSONObject object  = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for (int i = 0; i< offerArray.length(); i++) {
                        JSONObject childObj = offerArray.getJSONObject(i);
                        binding.carousel.addData(new CarouselItem(
                                Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("title")
                        ));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {});

        queue.add(request);

    }

    private void initSlider() {
//        binding.carousel.addData(new CarouselItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSks0bLtfhB-QpuYvaXXDZa_yByzchfAyNpeQ&usqp=CAU","Some Description"));
//        binding.carousel.addData(new CarouselItem("https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","Some Description"));
//        binding.carousel.addData(new CarouselItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSks0bLtfhB-QpuYvaXXDZa_yByzchfAyNpeQ&usqp=CAU","Some Description"));

        getRecentOffers();
    }

    void getCategories() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e("err", response);

                try {
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for (int i =0; i < categoriesArray.length(); i++){
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")

                            );

                            categories.add(category);
                        }

                        categoryAdapter.notifyDataSetChanged();

                    }else {
                        // DO nothing
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=20";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray productArray = object.getJSONArray("products");
                    for (int i =0; i<productArray.length(); i++) {
                        JSONObject childObj = productArray.getJSONObject(i);

                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );

                        products.add(product);
                    }

                    productAdapter.notifyDataSetChanged();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        queue.add(request);
    }

    void initCategories(){
        categories = new ArrayList<>();
//        categories.add(new Category("Sports & Outdoor","","#FF03DAC5","Some Description it",1));
//        categories.add(new Category("Live","","#FF03DAC5","Some Description it",2));
//        categories.add(new Category("Sports","","#FF018786","Some Description it",3));
//        categories.add(new Category("Table","","#FFAC2C2C","Some Description it",4));
//        categories.add(new Category("Sports","","#FF9C27B0","Some Description it",5));
//        categories.add(new Category("New Category","","#FF673AB7","Some Description it",6));
//        categories.add(new Category("Mobile","","#FF03A9F7","Some Description it",7));
//        categories.add(new Category("Computer","","#FFFFEB3B","Some Description it",8));
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void initProducts(){

        products = new ArrayList<>();
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,1));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,2));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,3));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,4));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,5));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,6));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,7));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,8));
//        products.add(new Product("Korean Loose Short Cowboy Outwear","https://firebasestorage.googleapis.com/v0/b/fir-testing-myscholars.appspot.com/o/play-solid.png?alt=media&token=bd194016-a698-441c-a246-ecfee072dd65","",12,5,1,9));
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

    }
}