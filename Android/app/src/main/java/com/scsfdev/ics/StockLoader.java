package com.scsfdev.ics;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class StockLoader extends AsyncTaskLoader<String> {
    private Stock stock;
    private String url;

    public StockLoader(Context context, String url, Stock stock) {
        super(context);
        this.stock = stock;
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        if(stock.isStockIn())
            return NetworkUtils.saveStockIn(url, stock);
        else
            return NetworkUtils.saveStockOut(url, stock);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}