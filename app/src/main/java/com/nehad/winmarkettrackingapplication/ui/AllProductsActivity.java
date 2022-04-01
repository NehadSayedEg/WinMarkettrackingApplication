package com.nehad.winmarkettrackingapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.nehad.winmarkettrackingapplication.Database.Market;
import com.nehad.winmarkettrackingapplication.Database.MarketDatabase;
import com.nehad.winmarkettrackingapplication.R;
import com.nehad.winmarkettrackingapplication.databinding.ActivityAllProductsBinding;
import com.nehad.winmarkettrackingapplication.databinding.ActivityHomeBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllProductsActivity extends AppCompatActivity {

    private ActivityAllProductsBinding binding;
    public static final String TAG = AllProductsActivity.class.getSimpleName();
    private List<Market> assetModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_all_products);


        binding = ActivityAllProductsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");

            assetModelList =
                    MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().getAllMarket();

        }).start();





        getAllAssets();


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().getAllMarket();
//            Log.i("DatabaseSize",
//                    assetModelList.get(0).getLocation()
//                            + "");
//
//


            //Background work here
            handler.post(() -> {
                //UI Thread work here

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);

                binding.allProductsRV.setLayoutManager(layoutManager );
                AllMarketProductsAdapter adapter = new AllMarketProductsAdapter(assetModelList);
                binding.allProductsRV.setAdapter(adapter);


            });
        });

    }


    private void getAllAssets() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: get All assets from Assets Table ...");


//            Log.i("DatabaseSize",
//                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
//                            + "");
            assetModelList =
                    MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().getAllMarket();






//
//            Log.i("Database string",
//                    assetModelList.get(0).getDescription()+ "");
//            Log.i("Database string",
//                    assetModelList.get(1).getBarcode()+ "");

        }).start();

    }}

