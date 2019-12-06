package com.akat.filmreel.ui.search;

import android.app.SearchManager;
import android.content.Context;
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
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.InjectorUtils;

import java.util.concurrent.TimeUnit;

public class SearchFragment extends Fragment
        implements SearchAdapter.OnItemClickHandler {

    private SearchViewModel viewModel;
    private SearchAdapter movieListAdapter;
    private SearchView searchView = null;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler_view_bookmarks);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        movieListAdapter = new SearchAdapter(requireActivity(), this);
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
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu_action);
        searchView = (SearchView) searchItem.getActionView();

        RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(text -> {
                    if (text.isEmpty()) {
                        viewModel.clearSearchResult();
                        return false;
                    } else {
                        return true;
                    }
                })
                .distinctUntilChanged()
                .subscribe(query -> viewModel.searchNextPage(query, 1));

        if (searchView != null) {
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu_action:
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, long movieId) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.PARAM.MOVIE_ID, movieId);

        Navigation.findNavController(view).navigate(R.id.fragment_movie_detail, bundle);
    }
}
