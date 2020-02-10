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
import android.widget.TextView;
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
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


public class IncomingActivity extends Activity
        implements BarcodeManagerListener, BarcodeDataListener, LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "ICS-SHIP-IN";
    private StockAdapter mAdapter;

    EditText etShNo;
    EditText etSupNo;
    EditText etPartNo;
    EditText etQty;
    TextView tvDate;
    ListView lstView;

    Shipment myShip = new Shipment();
    private BarcodeManager barManager;
    private BarcodeScanner barScanner;
    private boolean resumed;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle myBundle = getIntent().getExtras();
        url = myBundle.getString("URL");

        setContentView(R.layout.activity_incoming);

        setTitle(R.string.incoming);

        try {
            BarcodeManager.create(this, this);
        } catch (BarcodeException e) {
            Log.d(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
        }

        etShNo = findViewById(R.id.etInSHNO);
        etSupNo = findViewById(R.id.etInSupNo);
        etPartNo = findViewById(R.id.etInPNO);
        etQty = findViewById(R.id.etInQty);
        tvDate = findViewById(R.id.tvOutDate);
        LocalDate myD = LocalDate.now();
        DateTimeFormatter dtF = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        tvDate.setText(myD.format(dtF));

        lstView = findViewById(R.id.lstView);


        etSupNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                getLoaderManager().restartLoader(1, null, IncomingActivity.this);
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
                Log.d(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
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
                Log.d(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
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
                Log.d(TAG, "--> BarcodeException" + "\n\r" + "Error Msg: " + e.getMessage() + "\n\r" + "BarcodeManager Error code: " + e.getErrorCode());
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
            Log.d(TAG, "--> BarcodeException" + "\n\r" + "Err Msg: " + e.getMessage() + "\n\r" + "Err Code: " + e.getErrorCode());
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
                    if (etShNo.isFocused()) {
                        etShNo.setText(data);
                        etSupNo.requestFocus();
                    } else if (etSupNo.isFocused()) {
                        etSupNo.setText(data);
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
        myShip.setShipmentNo(etShNo.getText().toString());
        myShip.setSupplierCode(etSupNo.getText().toString());
        myShip.setPartNo(etPartNo.getText().toString());
        myShip.setQty(Integer.parseInt(etQty.getText().toString()));

        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public android.content.Loader<String> onCreateLoader(int id, Bundle args) {
        if(id == 0)
            return new ShipLoader(this, url, myShip);
        else
        {
            if(!TextUtils.isEmpty(etShNo.getText().toString()) && !TextUtils.isEmpty(etSupNo.getText().toString()))
                return new ProductLoader(this, url,etShNo.getText().toString(), etSupNo.getText().toString(), "IN","", "");
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
                JSONObject jsonObj = new JSONObject(data);
                String msg = jsonObj.getString("Msg");
                boolean isOk = jsonObj.getBoolean("IsOk");

                if (isOk) {
                    // Save successfully.
                    Toast.makeText(this, "Shipment is saved successfully.", Toast.LENGTH_SHORT).show();

                    // Clear screen.
                    initView(false);
                } else {
                    Toast.makeText(this, "Error: " + msg, Toast.LENGTH_LONG).show();
                }
            }
            else{
                // Get Product list for given Ship No + Supplier No.
                List<Stock>  lstStk = new ArrayList<>();
                JSONArray jsonAry = new JSONArray(data);

                for (int i=0; i< jsonAry.length(); i++) {
                    JSONObject jsonObj =  jsonAry.getJSONObject(i);

                    Stock stk = new Stock();
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
        myShip = new Shipment();
        LocalDate myD = LocalDate.now();
        DateTimeFormatter dtF = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        tvDate.setText(myD.format(dtF));

        etQty.setText(null);
        etPartNo.setText(null);

        if (initAll) {
            if(mAdapter != null) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
            etSupNo.setText(null);
            etShNo.setText(null);
            etShNo.requestFocus();
        } else {
            getLoaderManager().restartLoader(1, null, IncomingActivity.this);
            etPartNo.requestFocus();
        }
    }
}
