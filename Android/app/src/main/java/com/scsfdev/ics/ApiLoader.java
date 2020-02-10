package com.scsfdev.ics;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class ApiLoader extends AsyncTaskLoader<String> {

    private String url;
    public ApiLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.checkConn(url);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}