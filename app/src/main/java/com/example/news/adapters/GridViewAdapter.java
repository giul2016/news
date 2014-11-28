/**
 * Created by Jure Novic on 28/11/14
 * Copyright (c) Jure Novic. All rights reserved.
 */
package com.example.news.adapters;

import com.example.news.R;
import com.pkmmte.pkrss.Article;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridViewAdapter extends BaseAdapter {
    private List<Article> items = new ArrayList<Article>();
    private DateFormat mDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,
            Locale.getDefault());

    public void updateItems(List<Article> articles){
        items = articles;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Article getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            // we will use ViewHolder
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_layout);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mTextViewDate = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        }else{
            // from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextViewTitle.setText(getItem(position).getTitle());
        viewHolder.mTextViewDate.setText("published by " + getItem(position).getAuthor() + "on " + mDateFormat.format(getItem(position).getDate()));

        // use picasso library
        RequestCreator requestCreator;
        if (getItem(position).getImage() != null && !getItem(position).getImage().toString().isEmpty()){
            requestCreator = Picasso.with(parent.getContext()).load(getItem(position).getImage()).error(R.drawable.placeholder);
        }else{
            requestCreator = Picasso.with(parent.getContext()).load(R.drawable.placeholder);
            // we need some place holder
        }
        requestCreator.into(viewHolder.mImageView, new Callback() {
            @Override
            public void onSuccess() {
                Palette.generateAsync(((BitmapDrawable) viewHolder.mImageView.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getLightMutedSwatch();
                        if (swatch != null) {
                            viewHolder.mTextViewTitle.setTextColor(swatch.getBodyTextColor());
                            viewHolder.mTextViewDate.setTextColor(swatch.getBodyTextColor());
                            viewHolder.mRelativeLayout.setBackgroundColor(swatch.getRgb());
                        }
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
        return convertView;
    }

    public class ViewHolder{
        RelativeLayout mRelativeLayout;
        ImageView mImageView;
        TextView mTextViewTitle;
        TextView mTextViewDate;
    }
}
