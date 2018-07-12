package wiki.com.wikisearch.utils;

import android.content.Context;



import wiki.com.wikisearch.network.PageNetworkDataSource;
import wiki.com.wikisearch.repository.WikiRepository;
import wiki.com.wikisearch.room.AppExecutors;
import wiki.com.wikisearch.room.RoomDatabase;
import wiki.com.wikisearch.ui.MainViewModelFactory;

public class InjectorUtils {


    public static WikiRepository provideRepository(Context context) {
        RoomDatabase database = RoomDatabase.getdatabase(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        PageNetworkDataSource networkDataSource =
                PageNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return WikiRepository.getInstance(database.pageDao(), executors, networkDataSource);
    }

    public static PageNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return PageNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }



    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        WikiRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }
}
