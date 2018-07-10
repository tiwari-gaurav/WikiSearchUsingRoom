package wiki.com.wikisearch.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import wiki.com.wikisearch.repository.WikiRepository;
import wiki.com.wikisearch.room.PageEntity;

public class WikiPageViewModel extends ViewModel {

    private LiveData<List<PageEntity>> mWikiPages;
    private WikiRepository mRepository;
    private String mQuery;

    public WikiPageViewModel(WikiRepository repository,String query){
        mRepository = repository;
        mQuery=query;
        mWikiPages = mRepository.getPages(mQuery);

    }

    public LiveData<List<PageEntity>> getPagesWiki() {
        return mWikiPages;
    }
}
