package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
        public TextView bank,card_name,key_word,compute_value,total;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            bank = (TextView) v.findViewById(R.id.card_bank);
            card_name = (TextView) v.findViewById(R.id.card_name);
            key_word =  (TextView) v.findViewById(R.id.keyword);
            compute_value =  (TextView) v.findViewById(R.id.value);
            cardView = (CardView) v.findViewById(R.id.card_view);
            total = (TextView) v.findViewById(R.id.total);
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), content_result.class);
                    Bundle context = new Bundle();
                    TextView bank = (TextView) v.findViewById(R.id.card_bank);
                    TextView card_name = (TextView) v.findViewById(R.id.card_name);
                    TextView key_word =  (TextView) v.findViewById(R.id.keyword);
                    TextView compute_value =  (TextView) v.findViewById(R.id.value);
                    context.putString("card",card_name.getText().toString());
                    context.putString("bank",bank.getText().toString());
                    context.putString("key_word",key_word.getText().toString());
                    intent.putExtras(context);
                    v.getContext().startActivity(intent);
                }
            });
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
        if (row.getStore() !=null ){
            holder.total.setText("在"+row.getStore()+"有合作優惠");
            holder.cardView.setBackgroundColor(Color.CYAN);
            row.setStore(null);
        }

    }
    @Override
    public int getItemCount() {
        return card_list.size();
    }
}