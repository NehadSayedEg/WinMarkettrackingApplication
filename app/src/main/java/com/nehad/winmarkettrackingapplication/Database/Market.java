package com.nehad.winmarkettrackingapplication.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;



        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.room.ColumnInfo;
        import androidx.room.Entity;
        import androidx.room.PrimaryKey;

        import java.io.Serializable;



@Entity(tableName = "market_table")
public class Market implements Serializable {


    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "barcode")
    private String barcode ;


    @ColumnInfo(name = "description")
    private String description ;
    @ColumnInfo(name = "price")
    private double price ;

    @ColumnInfo(name = "updateprice")
    private double updatePrice ;


    @ColumnInfo(name = "status")
    private boolean status ;


    @NonNull
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(@NonNull String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUpdatePrice() {
        return updatePrice;
    }

    public void setUpdatePrice(double updatePrice) {
        this.updatePrice = updatePrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Nullable
    @Override
    public String toString() {
        return "Market{" +
                "barcode='" + barcode + '\'' +
                ", location='" + description + '\'' +
                ", status=" + status +
                ", price=" + price +
                ", updateprice=" + updatePrice +

                '}';
    }
}
