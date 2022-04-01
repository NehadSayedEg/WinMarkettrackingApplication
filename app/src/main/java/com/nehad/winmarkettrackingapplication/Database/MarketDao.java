package com.nehad.winmarkettrackingapplication.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMarketList(Market market);

    @Query("SELECT * FROM  market_table")
    List<Market> getAllMarket();


    @Query("SELECT * from market_table WHERE barcode = :barcode ")
    List<Market> searchMarketByBarcode(String barcode );


    @Query("UPDATE market_table  SET  price =:price WHERE  barcode  = :barcode  ")
    void  updatePrice(String barcode  , double price);

    @Query("UPDATE market_table  SET  updateprice =:salePrice WHERE  barcode  = :barcode  ")
    void  updateSalePrice(String barcode  , double salePrice);


    @Query("DELETE  FROM  market_table")
    public void DeleteAllMarket();



    @Query("SELECT * FROM  market_table")
    LiveData<List<Market> > getAllMarketPrices();




}

