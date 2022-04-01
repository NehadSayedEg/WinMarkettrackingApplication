package com.nehad.winmarkettrackingapplication.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParser;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.nehad.winmarkettrackingapplication.Database.Market;
import com.nehad.winmarkettrackingapplication.Database.MarketDatabase;
import com.nehad.winmarkettrackingapplication.R;
import com.nehad.winmarkettrackingapplication.databinding.ActivityAllProductsBinding;
import com.nehad.winmarkettrackingapplication.databinding.ActivityFindMarketItemctivityBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindMarketItemctivity extends AppCompatActivity {
    FindMarketItemViewModel marketItemViewModel;
    ImageView image;
    Context context;
    int gResId;
    String gText = " بسم الله الرحمن الرحيم  ";

    LinearLayout layout_print_view;


    private ActivityFindMarketItemctivityBinding binding;
    public static final String TAG = FindMarketItemctivity.class.getSimpleName();

    List<Market> assetslList = new ArrayList<>();
    String barcode;
    Activity activity;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String location;
    String Des;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFindMarketItemctivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        marketItemViewModel = new ViewModelProvider(this).get(FindMarketItemViewModel.class);

        Log.i("Hello from " , " Find Activity");


        binding.barcodeEt.requestFocus();
        binding.barcodeEt.setFocusable(true);

        // binding.firstPriceTX.setText(someString); // SomeString = your old price
        binding.firstPriceTX.setPaintFlags(binding.firstPriceTX.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        binding.barcodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.i("Hello from " , " before ");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("Hello from " , " on Change ");
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Hello from " , " after Change ");
                if (!s.toString().isEmpty()) {
                    Log.v("on after  Text  ", s.toString());

                    barcode = binding.barcodeEt.getText().toString();
                    Log.v(" barcode", barcode);

                    AssetsScan();
                }

            }


        });


        binding.firstPriceTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });


        binding.updatedPriceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogSalePrice();

            }
        });



        binding.printBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            printImage();
        } catch (EscPosConnectionException e) {
            e.printStackTrace();
        } catch (EscPosEncodingException e) {
            e.printStackTrace();
        } catch (EscPosBarcodeException e) {
            e.printStackTrace();
        } catch (EscPosParserException e) {
            e.printStackTrace();
        }

    }
});


    }

    private Bitmap ConvertLayoutToImage ( View view ){
        int height =  view.getHeight() /2;
        Log.e(" print height " , height+"");

        int width =  view.getWidth()/2;

        Log.e(" print width " , width+"");
        //Define a bitmap with the same size as the view
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
                //Bind a canvas to it
                Canvas canvas = new Canvas(returnedBitmap);
                //Get the view's background
                Drawable bgDrawable =view.getBackground();
                if (bgDrawable!=null) {
                    //has background drawable, then draw it on the canvas
                    bgDrawable.draw(canvas);
                }   else{
                    //does not have background drawable, then draw white background on the canvas
                    canvas.drawColor(Color.WHITE);
                }
                // draw the view on the canvas
                view.draw(canvas);
                //return the bitmap
        return returnedBitmap;
            }




    public void printImage() throws EscPosConnectionException, EscPosEncodingException, EscPosBarcodeException, EscPosParserException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, FindMarketItemctivity.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, FindMarketItemctivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, FindMarketItemctivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, FindMarketItemctivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            // Your code HERE

            Bitmap image = ConvertLayoutToImage(binding.printView);




            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            printer
                    .printFormattedText(
                            "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, image )+"</img>\n"


                    );
        }
    }




    private void AssetsScan() {
        executor.execute(() -> {
            MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().searchMarketByBarcode(barcode);

            assetslList.clear();
            Log.e(" from scam Method ", barcode);
            assetslList.addAll(marketItemViewModel.marketDao.searchMarketByBarcode(barcode));




            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");

                for (int i = 0 ; i< assetslList.size() ; i++ ) {
                    if(assetslList.size()>=0){

                        binding.assetItemLayout.setVisibility(View.VISIBLE);
                        binding.printView.setVisibility(View.VISIBLE);
                        binding.printBtn.setVisibility(View.VISIBLE);

                        String  barcodeTxt = assetslList.get(i).getBarcode() ;
                        binding.itemBarcode.setText(barcodeTxt);
                        Log.d( "AssetsScan: barcode " , barcodeTxt);

                        String  desTxt = assetslList.get(i).getDescription() ;
                        binding.itemDes.setText(desTxt);
                        Log.d( "AssetsScan: barcode " , desTxt);
                        binding.descriptionTv.setText(desTxt);

                        String  firstPrice = String.valueOf( assetslList.get(i).getPrice());
                        binding.firstPriceTX.setText(firstPrice);

                        String  updatePrice = String.valueOf( assetslList.get(i).getUpdatePrice());
                        binding.updatedPriceTv.setText(updatePrice);




                    }
                    else {
                        binding.notfoundtxt.setVisibility(View.VISIBLE);


                    }





                }

            });
        });

    }

    private void alertDialog(){
    LayoutInflater inflater = LayoutInflater.from(this);
    final View view = inflater.inflate(R.layout.dialog_layout, null);
    AlertDialog alertDialog = new AlertDialog.Builder(FindMarketItemctivity.this).create();
    alertDialog.setTitle(R.string.enter_price_update);   //"Enter price Update"
    alertDialog.setIcon(R.drawable.tag);
    alertDialog.setCancelable(false);
   // alertDialog.setMessage("Your Message Here");


    final EditText priceEt = (EditText) view.findViewById(R.id.etComments);

    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.change_price_btn) , new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
               double price = Double.parseDouble(priceEt.getText().toString());
            executor.execute(() -> {
                MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().updatePrice(barcode , price);

                assetslList.clear();
                Log.e(" from scam Method ", barcode);
                assetslList.addAll(marketItemViewModel.marketDao.searchMarketByBarcode(barcode));




                //Background work here
                handler.post(() -> {
                    //UI Thread work here
                    binding.barcodeEt.setText("");

                    for (int i = 0 ; i< assetslList.size() ; i++ ) {
                        if(assetslList.size()>=0){

                            binding.assetItemLayout.setVisibility(View.VISIBLE);

                            String  barcodeTxt = assetslList.get(i).getBarcode() ;
                            binding.itemBarcode.setText(barcodeTxt);
                            Log.d( "AssetsScan: barcode " , barcodeTxt);

                            String  desTxt = assetslList.get(i).getDescription() ;
                            binding.itemDes.setText(desTxt);
                            Log.d( "AssetsScan: barcode " , desTxt);
                            binding.descriptionTv.setText(desTxt);

                            String  firstPrice = String.valueOf( assetslList.get(i).getPrice());
                            binding.firstPriceTX.setText(firstPrice);

                            String  updatePrice = String.valueOf( assetslList.get(i).getUpdatePrice());
                            binding.updatedPriceTv.setText(updatePrice);




                        }
                        else {
                            binding.notfoundtxt.setVisibility(View.VISIBLE);


                        }





                    }

                });
            });



        }
    });


    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,  getString(R.string.cancel_btn), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
        }
    });


    alertDialog.setView(view);
    alertDialog.show();
}

    private void alertDialogSalePrice(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_layout, null);
        AlertDialog alertDialog = new AlertDialog.Builder(FindMarketItemctivity.this).create();
        alertDialog.setTitle(R.string.enter_price_update);
        alertDialog.setIcon(R.drawable.tag);
        alertDialog.setCancelable(false);
        // alertDialog.setMessage("Your Message Here");


        final EditText priceEt = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.change_price_btn) , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double price = Double.parseDouble(priceEt.getText().toString());
                executor.execute(() -> {
                    MarketDatabase.getMarketDatabase(getApplicationContext()).marketDao().updateSalePrice(barcode , price);

                    assetslList.clear();
                    Log.e(" from scam Method ", barcode);
                    assetslList.addAll(marketItemViewModel.marketDao.searchMarketByBarcode(barcode));




                    //Background work here
                    handler.post(() -> {
                        //UI Thread work here
                        binding.barcodeEt.setText("");

                        for (int i = 0 ; i< assetslList.size() ; i++ ) {
                            if(assetslList.size()>=0){

                                binding.assetItemLayout.setVisibility(View.VISIBLE);

                                String  barcodeTxt = assetslList.get(i).getBarcode() ;
                                binding.itemBarcode.setText(barcodeTxt);
                                Log.d( "AssetsScan: barcode " , barcodeTxt);

                                String  desTxt = assetslList.get(i).getDescription() ;
                                binding.itemDes.setText(desTxt);
                                Log.d( "AssetsScan: barcode " , desTxt);
                                binding.descriptionTv.setText(desTxt);

                                String  firstPrice = String.valueOf( assetslList.get(i).getPrice());
                                binding.firstPriceTX.setText(firstPrice);

                                String  updatePrice = String.valueOf( assetslList.get(i).getUpdatePrice());
                                binding.updatedPriceTv.setText(updatePrice);




                            }
                            else {
                                binding.notfoundtxt.setVisibility(View.VISIBLE);


                            }





                        }

                    });
                });



            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,  getString(R.string.cancel_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(view);
        alertDialog.show();
    }






    public void print() throws EscPosConnectionException, EscPosEncodingException, EscPosBarcodeException, EscPosParserException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, FindMarketItemctivity.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, FindMarketItemctivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, FindMarketItemctivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, FindMarketItemctivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            // Your code HERE


            final Paint textPaint = new Paint() {
                {
                    setColor(Color.WHITE);
                    setTextAlign(Paint.Align.LEFT);
                    setTextSize(40f);
                    setAntiAlias(true);
                }
            };
            final Rect bounds = new Rect();
            textPaint.getTextBounds(gText, 0, gText.length(), bounds);

            final Bitmap bmp = Bitmap.createBitmap(500, 200, Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
            final Canvas canvas = new Canvas(bmp);
            canvas.drawText(gText, 0, 40f, textPaint);
            // FileOutputStream stream = new FileOutputStream(...); //create your FileOutputStream here
            // bmp.compress(Bitmap.CompressFormat.PNG, 85, stream);
            // bmp.recycle();


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] bitmapData = bytes.toByteArray();

            bmp.compress(Bitmap.CompressFormat.PNG, 80, bytes);
            // bmp.recycle();

            Uri uri = Uri.parse(bmp.toString());

//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "Title", null);
            Log.e("image uri" , uri.toString());
//            image.setImageBitmap(bmp);

            String oderNo = " رقم الفاتورة  ";
            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            printer
                    .printFormattedText(
                            "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, bmp )+"</img>\n" +
                                    "[L]\n" +

                                    "[C]<u><font size='big'>"+ oderNo +"</font></u>\n" +
                                    "[L]\n" +
                                    "[C]================================\n" +
                                    "[L]\n" +
                                    "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                                    "[L]  + Size : S\n" +
                                    "[L]\n" +
                                    "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                                    "[L]  + Size : 57/58\n" +
                                    "[L]\n" +
                                    "[C]--------------------------------\n" +
                                    "[R]TOTAL PRICE :[R]34.98e\n" +
                                    "[R]TAX :[R]4.23e\n" +
                                    "[L]\n" +
                                    "[C]================================\n" +
                                    "[L]\n" +
                                    "[L]<font size='tall'>Customer :</font>\n" +
                                    "[L]Raymond DUPONT\n" +
                                    "[L]5 rue des girafes\n" +
                                    "[L]31547 PERPETES\n" +
                                    "[L]Tel : +33801201456\n" +
                                    "[L]\n" +
                                    "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                                    "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
                    );
        }
     }
}