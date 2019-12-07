package com.akat.filmreel.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.InjectorUtils;
import com.akat.filmreel.util.SnackbarMessage;
import com.akat.filmreel.util.SnackbarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment
        implements SearchAdapter.OnItemClickHandler, SearchAdapter.OnBottomReachedListener {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private SearchViewModel viewModel;
    private SearchAdapter movieListAdapter;
    private RecyclerView recyclerView;
    private View noItemView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        setHasOptionsMenu(true);

        noItemView = view.findViewById(R.id.search_no_item);

        recyclerView = view.findViewById(R.id.recycler_view_search);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        movieListAdapter = new SearchAdapter(requireActivity(), this, this);
        recyclerView.setAdapter(movieListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchViewModelFactory factory =
                InjectorUtils.provideSearchViewModelFactory(requireActivity());
        viewModel = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
        viewModel.getSearchResult().observe(this, entries -> {
            movieListAdapter.swapItems(entries);
            updateDataView();
        });

        setupSnackbar();
    }

    @Override
    public void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_view);
        searchItem.expandActionView();

        SearchView searchView = (SearchView) searchItem.getActionView();

        disposable.add(RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(String::trim)
                .filter(text -> {
                    if (text.isEmpty()) {
                        viewModel.clearSearchResult();
                        return false;
                    } else {
                        return true;
                    }
                })
                .distinctUntilChanged()
                .subscribe(query -> {
                    viewModel.setQuery(query);
                    viewModel.searchNextPage();
                })
        );

        searchView.setQuery(viewModel.getQuery(), true);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search_view) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, MovieEntity movie) {
        disposable.add(viewModel.saveMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.PARAM.MOVIE_ID, movie.getId());

                        Navigation.findNavController(view).navigate(R.id.fragment_movie_detail, bundle);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    @Override
    public void onBottomReached(int position) {
        viewModel.searchNextPage();
    }

    private void setupSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId))
        );
    }

    private void updateDataView() {
        if (movieListAdapter.getItemCount() == 0) {
            noItemView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            noItemView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
