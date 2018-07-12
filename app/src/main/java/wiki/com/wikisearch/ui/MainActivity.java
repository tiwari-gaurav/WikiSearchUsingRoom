package wiki.com.wikisearch.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import wiki.com.wikisearch.R;
import wiki.com.wikisearch.room.PageEntity;
import wiki.com.wikisearch.utils.InjectorUtils;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mPagesRecyclerView;
    private PageAdapter mPageAdapter;
    private WikiPageViewModel mWikiPageViewModel;
    boolean success = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        //fetchPages("");
    }

    private void initViews() {
        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mWikiPageViewModel = ViewModelProviders.of(this, factory).get(WikiPageViewModel.class);
        mPagesRecyclerView = (RecyclerView)findViewById(R.id.page_recycle);
        mPageAdapter = new PageAdapter( R.layout.news_list_item, this);
        mPagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPagesRecyclerView.setAdapter(mPageAdapter );
        MainActivity.this.mPagesRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_text, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchPages(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(success) {
                    mPageAdapter.getFilter().filter(newText);
                    return true;
                }

                return false;
            }
        });
        return true;
    }

    private void fetchPages(String query) {


        mWikiPageViewModel.setFilter(query);
        mWikiPageViewModel.getPagesWiki().observe(this, new Observer<List<PageEntity>>() {
            @Override
            public void onChanged(@Nullable List<PageEntity> pageEntity) {
                if(pageEntity!=null) {

                    mPageAdapter.swapVideos(pageEntity);
                   // Toast.makeText(MainActivity.this, pageEntity.toString(), Toast.LENGTH_LONG).show();
                    success=true;
                }else{
                    success=false;
                }
            }
        });
    }
}
