package com.scsfdev.ics;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

public class ProductLoader extends AsyncTaskLoader<String> {
    private String partNo;
    private String locId;
    private String input1;
    private String input2;
    private String inOut;
    private String url;

    public ProductLoader(Context context, String url, String input1, String input2, String inOut, String partNo, String locId) {
        super(context);
        this.url = url;
        this.input1 = input1;
        this.input2 = input2;
        this.inOut = inOut;
        this.partNo = partNo;
        this.locId = locId;
    }

    @Override
    public String loadInBackground() {
        if (!TextUtils.isEmpty(partNo) && TextUtils.isEmpty(locId))
            return NetworkUtils.getPartInfo(url,partNo);
        else if(TextUtils.isEmpty(partNo) && !TextUtils.isEmpty(locId))
            return NetworkUtils.getPartByLoc(url, locId);
        else
            return NetworkUtils.getShipmentByInput(url, input1, input2, inOut);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
