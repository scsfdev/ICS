package com.scsfdev.ics;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

public class ShipLoader extends AsyncTaskLoader<String> {
    private Shipment ship;
    private String url;
    public ShipLoader(Context context, String url, Shipment ship) {
        super(context);
        this.ship = ship;
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        if(TextUtils.isEmpty(ship.getOrderNo()))
            return NetworkUtils.saveIncoming(url, ship);
        else
            return NetworkUtils.saveOutgoing(url, ship);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}