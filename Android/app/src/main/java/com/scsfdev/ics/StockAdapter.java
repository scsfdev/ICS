package com.scsfdev.ics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StockAdapter extends ArrayAdapter<Stock>
{
    private Context mContext;
    private List<Stock> stockList;

    public StockAdapter(Context context, List<Stock> objects)
    {
        super(context, 0, objects);
        mContext = context;
        stockList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.customlist,parent,false);

        Stock currentStock = stockList.get(position);

        TextView tvPartNo = listItem.findViewById(R.id.tvPartNo);
        tvPartNo.setText(currentStock.getPartNo());

        TextView tvQty = listItem.findViewById(R.id.tvQty);
        tvQty.setText(String.valueOf(currentStock.getQty()));

        return listItem;
    }
}
