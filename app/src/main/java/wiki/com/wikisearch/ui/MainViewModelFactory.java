package wiki.com.wikisearch.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;



import wiki.com.wikisearch.repository.WikiRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WikiRepository mRepository;
    private String query;

    public MainViewModelFactory(WikiRepository repository) {
        this.mRepository = repository;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new WikiPageViewModel(mRepository);
    }
}
