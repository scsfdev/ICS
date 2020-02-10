package com.scsfdev.ics;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.densowave.bhtsdk.barcode.BarcodeDataReceivedEvent;
import com.densowave.bhtsdk.barcode.BarcodeDataReceivedEvent.BarcodeData;
import com.densowave.bhtsdk.barcode.BarcodeException;
import com.densowave.bhtsdk.barcode.BarcodeManager;
import com.densowave.bhtsdk.barcode.BarcodeManager.BarcodeManagerListener;
import com.densowave.bhtsdk.barcode.BarcodeScanner;
import com.densowave.bhtsdk.barcode.BarcodeScanner.BarcodeDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockOutActivity extends Activity
        implements BarcodeManagerListener, BarcodeDataListener, LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "ICS-STK-OUT";
    private StockAdapter mAdapter;

    EditText etOrderNo;
    EditText etLocId;
    EditText etPartNo;
    EditText etQty;
    ListView lstView;

    Stock myStock = new Stock();
    private BarcodeManager barManager;
    private BarcodeScanner barScanner;
    private boolean resumed;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);

        Bundle myBundle = getIntent().getExtras();
        url = myBundle.getString("URL");

        setTitle(R.string.stock_out);

        try {
            BarcodeManager.create(this, this);
        } catch (BarcodeException e) {
            Log.e(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
        }

        etOrderNo = findViewById(R.id.etSOOrderNo);
        etLocId = findViewById(R.id.etSOLoc);
        etPartNo = findViewById(R.id.etSOPNo);
        etQty = findViewById(R.id.etSOQty);

        lstView = findViewById(R.id.lstView);

        etOrderNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() > 0)
                    etOrderNo.setError(null);
            }
        });

        etLocId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() > 0)
                    etLocId.setError(null);
            }
        });

        etLocId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    getLoaderManager().restartLoader(1, null, StockOutActivity.this);
                }
            }
        });

        etPartNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() > 0)
                    etPartNo.setError(null);
            }
        });

        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etQty.getText().toString().trim().length() > 0 && etQty.getText().toString().trim().replace("0","").length() > 0)
                    etQty.setError(null);
            }
        });

        etQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    saveForm(null);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barScanner != null) {
            try {
                barScanner.destroy();
            } catch (BarcodeException e) {
                Log.e(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
            }

            barScanner = null;
        }

        if (barManager != null) {
            barManager.destroy();
            barManager = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (barScanner != null) {
            try {
                barScanner.addDataListener(this);
                barScanner.claim();

            } catch (BarcodeException e) {
                Log.e(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
            }
        }

        resumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (barScanner != null) {
            try {
                barScanner.close();
                barScanner.removeDataListener(this);
            } catch (BarcodeException e) {
                Log.e(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
            }
        }

        resumed = false;
    }

    @Override
    public void onBarcodeManagerCreated(BarcodeManager barcodeManager) {
        barManager = barcodeManager;
        try {
            List<BarcodeScanner> lstScanner = barManager.getBarcodeScanners();
            barScanner = lstScanner.get(0);     // 0 is default scanner.

            if (resumed) {
                barScanner.addDataListener(this);
                barScanner.claim();
            }


        } catch (BarcodeException e) {
            Log.e(TAG, "--> BarcodeException" + "\n\r" + "Err Msg: " + e.getMessage() + "\n\r" + "Err Code: " + e.getErrorCode());
        }
    }

    @Override
    public void onBarcodeDataReceived(BarcodeDataReceivedEvent barcodeDataReceivedEvent) {
        List<BarcodeData> lstBarData = barcodeDataReceivedEvent.getBarcodeData();

        for (BarcodeData barData : lstBarData) {
            final String data = barData.getData();
            Log.i(TAG, "Data: " + data);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (etOrderNo.isFocused()) {
                        etOrderNo.setText(data);
                        etLocId.requestFocus();
                    } else if (etLocId.isFocused()) {
                        etLocId.setText(data);
                        etPartNo.requestFocus();
                    } else if (etPartNo.isFocused()) {
                        etPartNo.setText(data);
                        etQty.requestFocus();
                    }
                }
            });

        }
    }

    public void clearForm(View view) {
        initView(true);
    }

    private boolean errorFree()
    {
        boolean noError = true;

        if(etOrderNo.getText().toString().trim().length() <=0){
            etOrderNo.setError("Enter Order No!");
            noError = false;
        }
        else
            etOrderNo.setError(null);

        if(etLocId.getText().toString().trim().length() <=0)
        {
            etLocId.setError("Enter Location ID!");
            noError=  false;
        }
        else
            etLocId.setError(null);

        if(etPartNo.getText().toString().trim().length() <=0) {
            etPartNo.setError("Enter Part No!");
            noError = false;
        }
        else
            etPartNo.setError(null);

        if(etQty.getText().toString().trim().length() <=0 || etQty.getText().toString().trim().replace("0","").length() <= 0) {
            etQty.setError(("Invalid Qty!"));
            noError = false;
        }
        else
            etQty.setError(null);

        return noError;
    }

    public void saveForm(View view) {
        if (!errorFree())
        {
            Toast.makeText(this, "Errors in input!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update to server through JSON and WCF web service.
        myStock.setOrderNo(etOrderNo.getText().toString());
        myStock.setLocation(etLocId.getText().toString());
        myStock.setPartNo(etPartNo.getText().toString());
        myStock.setQty(Integer.parseInt(etQty.getText().toString()));
        myStock.setStockIn(false);

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public android.content.Loader<String> onCreateLoader(int id, Bundle args) {
        if(id == 0)
            return new StockLoader(this, url, myStock);
        else {
            if(!TextUtils.isEmpty(etLocId.getText().toString()))
                return new ProductLoader(this, url,"", "", "","", etLocId.getText().toString());
            else
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader<String> loader, String data) {
        try {
            if (data == null)
                return;

            if(loader.getId() == 0) {
                // Return result from Saving Stock Out.
                JSONObject jsonObj = new JSONObject(data);
                String msg = jsonObj.getString("Msg");
                boolean isOk = jsonObj.getBoolean("IsOk");

                if (isOk) {
                    // Save successfully.
                    // Clear screen.
                    Toast.makeText(this, "Stock out successfully.", Toast.LENGTH_SHORT).show();

                    initView(false);
                } else {
                    Toast.makeText(this, "Error: " + msg, Toast.LENGTH_LONG).show();
                }
            }
            else{
                // Get Product list for given Location ID.
                List<Stock>  lstStk = new ArrayList<>();
                JSONArray jsonAry = new JSONArray(data);

                for (int i=0; i< jsonAry.length(); i++) {
                    JSONObject jsonObj =  jsonAry.getJSONObject(i);

                    Stock stk = new Stock();
                    stk.setLocation(jsonObj.getString("LocID"));
                    stk.setPartNo(jsonObj.getString("PartNo"));
                    stk.setQty(jsonObj.getInt("Qty"));
                    lstStk.add(stk);
                }

                mAdapter = new StockAdapter(this, lstStk);
                lstView.setAdapter(mAdapter);
            }


        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<String> loader) {

    }

    private void initView(boolean initAll) {
        myStock = new Stock();
        etQty.setText(null);
        etPartNo.setText(null);

        if (!initAll) {
            etPartNo.requestFocus();
            getLoaderManager().restartLoader(1, null, StockOutActivity.this);
        } else {
            if(mAdapter != null) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }

            etLocId.setText(null);
            etOrderNo.setText(null);
            etOrderNo.requestFocus();
        }
    }
}
