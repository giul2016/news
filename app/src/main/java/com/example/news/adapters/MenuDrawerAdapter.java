/**
 * Created by Jure Novic on 28/11/14
 * Copyright (c) Jure Novic. All rights reserved.
 */
package com.example.news.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuDrawerAdapter extends BaseAdapter {
    private String[] menus;

    public MenuDrawerAdapter(String[] menus) {
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.length;
    }

    @Override
    public String getItem(int position) {
        return menus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            // we need to inflate, will use android list view
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(getItem(position));

        return convertView;
    }
}
