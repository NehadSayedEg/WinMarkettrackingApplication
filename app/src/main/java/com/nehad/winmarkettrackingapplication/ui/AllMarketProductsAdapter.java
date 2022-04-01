package com.nehad.winmarkettrackingapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.nehad.winmarkettrackingapplication.Database.Market;
import com.nehad.winmarkettrackingapplication.R;

import java.util.List;

public class AllMarketProductsAdapter extends RecyclerView.Adapter<AllMarketProductsAdapter.MarketViewHolder> {
    private List<Market> marketList ;

    public  AllMarketProductsAdapter(List<Market> marketModelList) {
        this.marketList = marketModelList;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MarketViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.market_item, parent ,false ));
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        holder.setAssetItem(marketList.get(position));

    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static  class MarketViewHolder extends RecyclerView.ViewHolder {
        TextView barcodeTxt ,  desTxt , locationTxt , statusTxt ;

        MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            barcodeTxt = itemView.findViewById(R.id.itemBarcode);
            desTxt = itemView.findViewById(R.id.itemDes);
            locationTxt = itemView.findViewById(R.id.itemLoc);
        }

        void setAssetItem(Market market){
            barcodeTxt.setText(market.getBarcode());
            desTxt.setText(market.getDescription());
            String price = String.valueOf(market.getPrice());
            locationTxt.setText(price);

        }
    }
}
