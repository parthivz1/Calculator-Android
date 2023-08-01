package com.example.calculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapters extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> arrayList, arrayList1;

    public MyAdapters(Activity context, ArrayList<String> arrayList, ArrayList<String> arrayList1){
        super(context, R.layout.list_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item,null);

        TextView input = (TextView) rowView.findViewById(R.id.l1);
        TextView output = (TextView) rowView.findViewById(R.id.l2);

        input.setText(arrayList.get(position));
        output.setText(arrayList1.get(position));

        return rowView;

    }

}