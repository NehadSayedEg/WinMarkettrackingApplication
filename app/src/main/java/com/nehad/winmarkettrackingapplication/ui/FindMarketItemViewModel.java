package com.nehad.winmarkettrackingapplication.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nehad.winmarkettrackingapplication.Database.Market;
import com.nehad.winmarkettrackingapplication.Database.MarketDao;
import com.nehad.winmarkettrackingapplication.Database.MarketDatabase;

import java.util.List;

public class FindMarketItemViewModel extends ViewModel {
    MarketDao marketDao ;



    public FindMarketItemViewModel(){
        marketDao = MarketDatabase.assetsDatabase.marketDao();

    }



    public void SearchItem(String barcode) {marketDao.searchMarketByBarcode(barcode); }
//    public void insertLocation(LocationEntity locationEntity) { assetsDao.insertLocation(locationEntity); }
//    public void insertDes(AddDescription addDescription) { assetsDao.insertDes(addDescription); }
//
//    public void insertAsset(AssetModel assetModel) { assetsDao.insertAsset(assetModel); }
//

}

