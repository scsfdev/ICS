package com.scsfdev.ics;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity
                implements LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = "ICS-SHIP-MAIN";
    private String url = "";
    TextView tvMode;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button btnIncoming = findViewById(R.id.btnIncoming);
        Button btnOutgoing = findViewById(R.id.btnOutgoing);
        Button btnStockIn = findViewById(R.id.btnStockIn);
        Button btnStockOut = findViewById(R.id.btnStockOut);

        url = PreferenceManager.getDefaultSharedPreferences(this).getString("api_url","");

        tvMode = findViewById(R.id.txtMode);

        btnIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IncomingActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        btnOutgoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OutgoingActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        btnStockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StockInActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        btnStockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StockOutActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        // Test Connection.
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public android.content.Loader<String> onCreateLoader(int id, Bundle args) {
        return new ApiLoader(this, url);
    }

    @Override
    public void onLoadFinished(android.content.Loader<String> loader, String data) {
        try {
            if (data == null)
                return;


            JSONObject jsonObj = new JSONObject(data);
            String msg = jsonObj.getString("Msg");
            boolean isOk = jsonObj.getBoolean("IsOk");

            if (!isOk) {
                Toast.makeText(this, "Error: " + msg, Toast.LENGTH_LONG).show();
            }
            else
            {
                Boolean mode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("mode",false);
                if(mode)
                    tvMode.setText("Mode: Online. IP: " + url);
                else
                    tvMode.setText("Mode: Offline");
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<String> loader) {

    }
}
