package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UserMe on 2017/7/31.
 */

public class mylistadapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private ArrayList<String> my_card;

        public mylistadapter(Context context, ArrayList<String> _card){
            myInflater = LayoutInflater.from(context);
            this.my_card = _card;
        }
        /*private view holder class*/
        private class ViewHolder {
            TextView card;
            ImageButton delete;


            public ViewHolder(TextView card){
                this.card = card;
//                this.delete = b;

            }
        }

        @Override
        public int getCount() {
            return my_card.size();
        }

        @Override
        public Object getItem(int arg0) {
            return my_card.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return my_card.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = myInflater.inflate(R.layout.list_result,null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.my_card_name)
//                        (ImageButton) convertView.findViewById(R.id.delete)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            String movie = (String)my_card.get(position);

            holder.card.setText(movie);


            return convertView;
        }
}
