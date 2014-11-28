package com.example.news.activities;

import com.example.news.R;
import com.example.news.adapters.GridViewAdapter;
import com.example.news.adapters.MenuDrawerAdapter;
import com.example.news.interfaces.ToolbarToggle;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FeedsActivity extends BaseActivity implements Callback {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private List<Article> mItems = new ArrayList<Article>();
    private int selectedFeed = 0;

    String[] menuTitles;
    String[] feedUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_ab_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        final ToolbarToggle toolbarToggle = new ToolbarToggle(this, mDrawerLayout, mToolbar, R.string.drawer_opened, R.string.drawer_closed);
        toolbarToggle.setToggleListener(new ToolbarToggle.ToggleListener() {
            @Override
            public void onOpened() {
                mToolbar.setTitle(R.string.drawer_opened);
            }

            @Override
            public void onClosed() {
                mToolbar.setTitle(menuTitles[selectedFeed]);

            }
        });
        mDrawerLayout.setDrawerListener(toolbarToggle);
        toolbarToggle.syncState();

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
                selectFeed(position);
                mDrawerLayout.closeDrawers();
            }
        });

        mGridView = (GridView) findViewById(R.id.gridView);
        // we need to display articles, so lets check the library
        mGridViewAdapter = new GridViewAdapter();
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open new activity and display article
                ArticleActivity.launch(FeedsActivity.this, view.findViewById(R.id.relative_layout), mItems.get(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectFeed(selectedFeed);
    }

    private void selectFeed(int position){
        selectedFeed = position;
        PkRSS.with(FeedsActivity.this).load(feedUrls[position]).callback(this).async();
        mToolbar.setTitle(menuTitles[position]);
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

    @Override
    public void OnPreLoad() {

    }

    @Override
    public void OnLoaded(List<Article> articles) {
        mItems = articles;
        FeedsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridViewAdapter.updateItems(mItems);
                mGridViewAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void OnLoadFailed() {

    }
}
