package wiki.com.wikisearch.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wiki.com.wikisearch.room.AppExecutors;
import wiki.com.wikisearch.room.PageEntity;

public class PageNetworkDataSource {


        private static final String LOG_TAG = wiki.com.wikisearch.network.PageNetworkDataSource.class.getSimpleName();
        // For Singleton instantiation
        private static final Object LOCK = new Object();
        private static wiki.com.wikisearch.network.PageNetworkDataSource sInstance;
        private final Context mContext;

        // LiveData storing the latest downloaded weather forecasts
        private final MutableLiveData<List<PageEntity>> mPages;
        private final AppExecutors mExecutors;

        private PageNetworkDataSource(Context context, AppExecutors executors) {
            mContext = context;
            mExecutors = executors;
            mPages = new MutableLiveData<List<PageEntity>>();
        }

        /**
         * Get the singleton for this class
         */
        public static wiki.com.wikisearch.network.PageNetworkDataSource getInstance(Context context, AppExecutors executors) {
            Log.d(LOG_TAG, "Getting the network data source");
            if (sInstance == null) {
                synchronized (LOCK) {
                    sInstance = new wiki.com.wikisearch.network.PageNetworkDataSource(context.getApplicationContext(), executors);
                    Log.d(LOG_TAG, "Made new network data source");
                }
            }
            return sInstance;
        }

        public LiveData<List<PageEntity>> getPages() {
            return mPages;
        }

        /**
         * Starts an intent service to fetch the weather.
         */
        public void startFetchPageService(String query) {
            Intent intentToFetch = new Intent(mContext, PageIntentService.class);
            intentToFetch.putExtra("search_text",query);
            mContext.startService(intentToFetch);
            Log.d(LOG_TAG, "Service created");
        }

        public void fetchPages(String query) {
            Log.d(LOG_TAG, "Fetch weather started");
            mExecutors.networkIO().execute(() -> {
                APiInterface apiService = ApiClient.getClient().create(APiInterface.class);
              Call<List<PageEntity>> call= apiService.getPages("query","json","pageimages|pageterms","prefixsearch","2","thumbnail",
                        "description",query);
              call.enqueue(new Callback<List<PageEntity>>() {
                  @Override
                  public void onResponse(Call<List<PageEntity>> call, Response<List<PageEntity>> response) {
                      mPages.postValue(response.body());
                  }

                  @Override
                  public void onFailure(Call<List<PageEntity>> call, Throwable t) {

                  }
              });

            });

        }
    }


