package com.example.news.activities;

import com.example.news.R;
import com.example.news.adapters.GridViewAdapter;
import com.example.news.adapters.MenuDrawerAdapter;
import com.example.news.interfaces.ToolbarToggle;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FeedsActivity extends BaseActivity implements Callback,
        SearchView.OnQueryTextListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private List<Article> mItems = new ArrayList<Article>();
    private SearchView mSearchView;

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
        getMenuInflater().inflate(R.menu.menu_feeds, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
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
        updateGrid(mItems);
    }

    @Override
    public void OnLoadFailed() {
        FeedsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FeedsActivity.this, getString(R.string.toast_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchedResults(s);
        return true;
    }

    private void searchedResults(String searchString){
        if (searchString.length() < 3){
            updateGrid(mItems);
        }
        List<Article> filtered = new ArrayList<Article>();
        for (Article item : mItems) {
            if (item.getTitle().toLowerCase().contains(searchString.toLowerCase()) ||
                    item.getAuthor().toLowerCase().contains(searchString.toLowerCase())){
                filtered.add(item);
            }
        }
        updateGrid(filtered);
    }

    private void updateGrid(final List<Article> items){
        FeedsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridViewAdapter.updateItems(items);
                mGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()){
            invalidateOptionsMenu();
            updateGrid(mItems);
        }else{
            super.onBackPressed();
        }
    }
}
