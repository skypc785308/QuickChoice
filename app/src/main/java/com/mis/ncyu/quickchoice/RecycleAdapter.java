package com.mis.ncyu.quickchoice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by UserMe on 2017/9/3.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private List<card_datatype> card_list;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bank,card_name,key_word,compute_value;

        public ViewHolder(View v) {
            super(v);
            bank = (TextView) v.findViewById(R.id.card_bank);
            card_name = (TextView) v.findViewById(R.id.card_name);
            key_word =  (TextView) v.findViewById(R.id.keyword);
            compute_value =  (TextView) v.findViewById(R.id.value);
        }
    }

    public RecycleAdapter(List<card_datatype> data) {
        card_list = data;
    }
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        card_datatype row = card_list.get(position);
        holder.bank.setText(row.getBank());
        holder.card_name.setText(row.getname());
        holder.key_word.setText(row.getkey());
        holder.compute_value.setText(String.valueOf(row.getValue()));

    }
    @Override
    public int getItemCount() {
        return card_list.size();
    }
}
