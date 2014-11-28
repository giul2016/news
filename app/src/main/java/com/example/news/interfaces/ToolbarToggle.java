/**
 * Created by Jure Novic on 28/11/14
 * Copyright (c) Jure Novic. All rights reserved.
 */
package com.example.news.interfaces;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ToolbarToggle extends ActionBarDrawerToggle{
    private ToggleListener mToggleListener;

    public ToolbarToggle(Activity activity, DrawerLayout drawerLayout,
            Toolbar toolbar, int openDrawerContentDescRes,
            int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (mToggleListener != null) {
            mToggleListener.onOpened();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (mToggleListener != null) {
            mToggleListener.onClosed();
        }
    }

    public void setToggleListener(ToggleListener toggleListener) {
        mToggleListener = toggleListener;
    }

    public interface ToggleListener{
        public void onOpened();
        public void onClosed();
    }
}
