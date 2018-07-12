package wiki.com.wikisearch.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import wiki.com.wikisearch.repository.WikiRepository;
import wiki.com.wikisearch.room.PageEntity;

public class WikiPageViewModel extends ViewModel {

    private LiveData<List<PageEntity>> mWikiPages;
    private WikiRepository mRepository;
    private String mQuery;
    private LiveData<List<PageEntity>> searchByLiveData;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<String>();

    public WikiPageViewModel(WikiRepository repository){
        mRepository = repository;

        //mWikiPages = mRepository.getPages(mQuery);
        mWikiPages = Transformations.switchMap(filterLiveData,
                v -> repository.getPages(v));

    }


    void setFilter(String filter) { this.filterLiveData.setValue(filter); }
    public LiveData<List<PageEntity>> getPagesWiki() {
        return mWikiPages;
    }
}
