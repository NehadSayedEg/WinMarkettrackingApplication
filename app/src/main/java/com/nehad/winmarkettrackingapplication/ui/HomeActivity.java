package com.nehad.winmarkettrackingapplication.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nehad.winmarkettrackingapplication.Database.Market;
import com.nehad.winmarkettrackingapplication.Database.MarketDatabase;
import com.nehad.winmarkettrackingapplication.R;
import com.nehad.winmarkettrackingapplication.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {





    private ActivityHomeBinding binding;
    public static final String TAG = HomeActivity.class.getSimpleName();
    private List<Market> assetModelList = new ArrayList<>();
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());



        binding.allAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
                startActivity(intent);
            }
        });

        binding.searchAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FindMarketItemctivity.class);
                startActivity(intent);
            }
        });



//        binding.exportFilesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(HomeActivity.this , ExportfilesActivity.class);
//                startActivity(intent);
//
//
//            }
//
//        });


        binding.clearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.AlertDialogTheme).setTitle(R.string.deleteTitle)
                        .setMessage(R.string.deleteMessage)
                        .setPositiveButton(R.string.deleteBtn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        clearAssetsDB();
//                                Toast.makeText(getApplicationContext(), "Bach to Main", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }).setNegativeButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }

        });

    }

    private void clearAssetsDB () {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Clear database Table ...");

            MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().DeleteAllMarket();

            Log.i("DatabaseSize",
                    MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().getAllMarket().size() + "");

        }).start();

    }


    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }



}

