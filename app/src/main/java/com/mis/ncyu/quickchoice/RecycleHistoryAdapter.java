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

import com.mis.ncyu.quickchoice.recommend.activity_history;

import java.util.List;

/**
 * Created by UserMe on 2017/9/10.
 */

public class RecycleHistoryAdapter extends RecyclerView.Adapter<RecycleHistoryAdapter.ViewHolder> {
    private List<card_datatype> card_list;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bank,card_name,key_word;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            card_name = (TextView) v.findViewById(R.id.card_name);
            key_word =  (TextView) v.findViewById(R.id.keyword);
            cardView = (CardView) v.findViewById(R.id.card_view);
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_history.class);
                    Bundle context = new Bundle();
                    TextView card_name = (TextView) v.findViewById(R.id.card_name);
                    context.putString("card",card_name.getText().toString());
                    intent.putExtras(context);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public RecycleHistoryAdapter(List<card_datatype> data) {
        card_list = data;
    }
    @Override
    public RecycleHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_history, parent, false);
        RecycleHistoryAdapter.ViewHolder vh = new RecycleHistoryAdapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(RecycleHistoryAdapter.ViewHolder holder, int position) {
        card_datatype row = card_list.get(position);
        holder.card_name.setText(row.getname());
        holder.key_word.setText(String.valueOf(Math.round(row.getValue()))+"元");
    }
    @Override
    public int getItemCount() {
        return card_list.size();
    }
}
