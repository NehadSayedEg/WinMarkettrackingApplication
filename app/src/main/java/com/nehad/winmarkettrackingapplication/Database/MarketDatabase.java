package com.nehad.winmarkettrackingapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {  Market.class }, version = 1 , exportSchema = false)
public  abstract  class MarketDatabase extends RoomDatabase {
    public static MarketDatabase assetsDatabase ;
    public static synchronized MarketDatabase getMarketDatabase(Context context){
        if(assetsDatabase == null){
            assetsDatabase = Room.databaseBuilder(context , MarketDatabase.class , "market_db").fallbackToDestructiveMigration()
                    .build();
        }
        return assetsDatabase;
    }

    public abstract MarketDao marketDao();
}
