package com.example.calculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<String> arrayList, arrayList1;

    public MyAdapter(Activity context, ArrayList<String> arrayList, ArrayList<String> arrayList1) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.input.setText(arrayList.get(position));
        holder.output.setText(arrayList1.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView input;
        TextView output;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            input = itemView.findViewById(R.id.l1);
            output = itemView.findViewById(R.id.l2);
        }
    }
}
