package wiki.com.wikisearch.repository;

import android.arch.lifecycle.LiveData;

import android.util.Log;


import java.util.List;

import wiki.com.wikisearch.network.PageNetworkDataSource;
import wiki.com.wikisearch.room.AppExecutors;
import wiki.com.wikisearch.room.PageDao;
import wiki.com.wikisearch.room.PageEntity;

public class WikiRepository {

    private static final String LOG_TAG = WikiRepository.class.getSimpleName();

    private PageDao mPageDao;
    private LiveData<List<PageEntity>> mPageResult;
    private AppExecutors mExecutors;
    private PageNetworkDataSource mPageNetworkDataSource;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static WikiRepository sInstance;

    public WikiRepository(PageDao pageDao,  AppExecutors executors, PageNetworkDataSource pageNetworkDataSource){
        mPageDao = pageDao;

        mExecutors=executors;
        mPageNetworkDataSource = pageNetworkDataSource;

        mPageResult = mPageDao.getWikiPages();

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<PageEntity>> networkData = mPageNetworkDataSource.getPages();
        networkData.observeForever(newVideosFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // Deletes old historical data
                deleteOldData();
                Log.d(LOG_TAG, "Old videos deleted");
                // Insert our new weather data into Sunshine's database
                mPageDao.bulkInsert(newVideosFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });

    }


    public synchronized static WikiRepository getInstance(PageDao pageDao,AppExecutors executors, PageNetworkDataSource pageNetworkDataSource) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WikiRepository(pageDao,executors, pageNetworkDataSource);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    /**
     * Deletes old weather data because we don't need to keep multiple days' data
     */
    private void deleteOldData() {

        mPageDao.deleteOldPages();
    }




    public LiveData<List<PageEntity>> getPages(String query) {
        initializeData(query);
        return mPageResult;
    }




    private void startFetchPageService(String query) {
        mPageNetworkDataSource.startFetchPageService(query);
    }

    private synchronized void initializeData(String query) {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.

        mExecutors.diskIO().execute(() -> {

                startFetchPageService(query);

        });
    }

}
