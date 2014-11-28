package com.example.news.activities;

import com.example.news.R;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class FeedsActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    String[] menuTitles;
    String[] feedUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_ab_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                        R.string.drawer_opened, R.string.drawer_closed);

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        menuTitles = getResources().getStringArray(R.array.menu_titles);
        feedUrls = getResources().getStringArray(R.array.feed_urls);
        // get drawer list view

        mDrawerListView = (ListView) findViewById(R.id.leftMenu);
        mDrawerListView.setAdapter(new MenuDrawerAdapter(menuTitles));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo
            }
        });


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_feeds;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MenuDrawerAdapter extends BaseAdapter{
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
}
