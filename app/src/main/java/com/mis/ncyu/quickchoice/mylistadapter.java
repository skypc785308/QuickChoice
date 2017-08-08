package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by UserMe on 2017/7/31.
 */

public class mylistadapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private List<card_datatype> movies;

        public mylistadapter(Context context, List<card_datatype> movie){
            myInflater = LayoutInflater.from(context);
            this.movies = movie;
        }
        /*private view holder class*/
        private class ViewHolder {
            TextView card;
            TextView key;
            TextView no;
            TextView value;

            public ViewHolder(TextView no,TextView card, TextView key,TextView value){
                this.no = no;
                this.card = card;
                this.key = key;
                this.value = value;
            }
        }

        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int arg0) {
            return movies.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return movies.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = myInflater.inflate(R.layout.list_result,null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.number),
                        (TextView) convertView.findViewById(R.id.card_name),
                        (TextView) convertView.findViewById(R.id.keyvalue),
                        (TextView) convertView.findViewById(R.id.value)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            card_datatype movie = (card_datatype)getItem(position);

            holder.no.setText(movie.getno());
            holder.card.setText(movie.getname());
            holder.key.setText(movie.getkey());
            holder.value.setText(movie.getValue().toString());

            return convertView;
        }
}
