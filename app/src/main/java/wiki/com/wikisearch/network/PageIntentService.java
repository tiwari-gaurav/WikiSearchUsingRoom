package wiki.com.wikisearch.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;



import wiki.com.wikisearch.utils.InjectorUtils;

public class PageIntentService extends IntentService {
    private static final String LOG_TAG = PageIntentService.class.getSimpleName();

    public PageIntentService() {
        super("SunshineSyncIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(LOG_TAG, "Intent service started");
        PageNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchPages(intent.getStringExtra("search_text"));

    }
}
