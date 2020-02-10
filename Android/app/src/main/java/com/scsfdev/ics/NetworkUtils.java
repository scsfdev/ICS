package com.scsfdev.ics;

import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

//    private static final String Api_Url = "http://10.72.137.98/aics/WcfSvcICS.svc/";
    private static final String Api_Url = "[URL]/aics/WcfSvcICS.svc/";

    static String checkConn(String url){
        HttpURLConnection urlCon = null;
        BufferedReader reader = null;
        String partJsonString = null;

        String localURL = Api_Url.replace("[URL]", url) + "CheckConn";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.connect();

            InputStream inputStream = urlCon.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }

    static String getAllParts(String url) {
        HttpURLConnection urlCon = null;
        BufferedReader reader = null;
        String result = null;

        String localURL = Api_Url.replace("[URL]", url) + "GetProducts";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.connect();

            InputStream inputStream = urlCon.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            result = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return result;
    }

    static String getPartByLoc(String url, String locId) {
        HttpURLConnection urlCon = null;
        BufferedReader reader = null;
        String partJsonString = null;
        String localURL = Api_Url.replace("[URL]", url) + "GetProductByLoc/" + locId;

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();


            StringBuilder builder ;

            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }

    static String getShipmentByInput(String url, String input1, String input2, String inOut) {
        HttpURLConnection urlCon = null;
        BufferedReader reader = null;
        String partJsonString = null;
        String localURL = Api_Url.replace("[URL]", url) + "GetShipmentByInput/" + input1 + "/" + input2 + "/" + inOut;

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();

            StringBuilder builder ;

            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }

    static String getPartInfo(String url, String partNo) {
        HttpURLConnection urlCon = null;
        BufferedReader reader = null;
        String partJsonString = null;
        String localURL = Api_Url.replace("[URL]", url) + "GetProduct/" + partNo;

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.connect();

            InputStream inputStream = urlCon.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }

    static String saveIncoming(String url, Shipment inShip) {
        HttpURLConnection urlCon = null;
        BufferedWriter writer = null;
        String partJsonString;

        String localURL = Api_Url.replace("[URL]", url) + "ShipmentIO/";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();

            OutputStream outputStream = urlCon.getOutputStream();

            writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            JsonWriter jsonWriter = new JsonWriter(writer);

            jsonWriter.setIndent(" ");
            // Writing 1 obj into JSON.
            jsonWriter.beginObject();
            jsonWriter.name("ShipNo").value(inShip.getShipmentNo());
            jsonWriter.name("SupplierNo").value(inShip.getSupplierCode());
            jsonWriter.name("CustomerNo").value(inShip.getCustomerCode());
            jsonWriter.name("OrderNo").value(inShip.getOrderNo());
            jsonWriter.name("PartNo").value(inShip.getPartNo());
            jsonWriter.name("Qty").value(inShip.getQty());
            jsonWriter.name("ShipIn").value(1);
            jsonWriter.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        BufferedReader reader = null;
        StringBuilder builder = null;

        try {
            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }


        partJsonString = builder.toString();


        return partJsonString;
    }

    static String saveOutgoing(String url, Shipment outShip) {
        HttpURLConnection urlCon = null;
        BufferedWriter writer = null;
        String partJsonString = null;

        String localURL = Api_Url.replace("[URL]", url) + "ShipmentIO/";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();

            OutputStream outputStream = urlCon.getOutputStream();

            writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            JsonWriter jsonWriter = new JsonWriter(writer);

            // Writing 1 obj into JSON.
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("ShipNo").value(outShip.getShipmentNo());
            jsonWriter.name("SupplierNo").value(outShip.getSupplierCode());
            jsonWriter.name("CustomerNo").value(outShip.getCustomerCode());
            jsonWriter.name("OrderNo").value(outShip.getOrderNo());
            jsonWriter.name("PartNo").value(outShip.getPartNo());
            jsonWriter.name("Qty").value(outShip.getQty());
            jsonWriter.name("ShipIn").value(false);
            jsonWriter.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        BufferedReader reader = null;
        StringBuilder builder = null;

        try {
            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }


        partJsonString = builder.toString();


        return partJsonString;
    }

    static String saveStockIn(String url, Stock inStock) {
        HttpURLConnection urlCon = null;
        BufferedWriter writer = null;
        String partJsonString = null;

        String localURL = Api_Url.replace("[URL]", url) + "StockIO/";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();

            OutputStream outputStream = urlCon.getOutputStream();

            writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            JsonWriter jsonWriter = new JsonWriter(writer);

            // Writing 1 obj into JSON.
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("OrderNo").value("");
            jsonWriter.name("LocID").value(inStock.getLocation());
            jsonWriter.name("PartNo").value(inStock.getPartNo());
            jsonWriter.name("Qty").value(inStock.getQty());
            jsonWriter.name("StockIn").value(true);
            jsonWriter.endObject();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        BufferedReader reader = null;
        StringBuilder builder = null;

        try
        {
            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }

    static String saveStockOut(String url, Stock outStock) {
        HttpURLConnection urlCon = null;
        BufferedWriter writer = null;
        String partJsonString = null;

        String localURL = Api_Url.replace("[URL]", url) + "StockIO/";

        try {
            URL requestURL = new URL(localURL);
            urlCon = (HttpURLConnection) requestURL.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.connect();

            OutputStream outputStream = urlCon.getOutputStream();

            writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            JsonWriter jsonWriter = new JsonWriter(writer);

            // Writing 1 obj into JSON.
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("OrderNo").value(outStock.getOrderNo());
            jsonWriter.name("LocID").value(outStock.getLocation());
            jsonWriter.name("PartNo").value(outStock.getPartNo());
            jsonWriter.name("Qty").value(outStock.getQty());
            jsonWriter.name("StockIn").value(false);
            jsonWriter.endObject();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        BufferedReader reader = null;
        StringBuilder builder = null;

        try
        {
            int responseCode = urlCon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection fail: " + String.valueOf(responseCode));
                return null;
            }

            InputStream inputStream = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0)
                return null;

            partJsonString = builder.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        return partJsonString;
    }
}
