package com.akat.filmreel.ui.movieList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.ui.common.MovieListAdapter;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.SnackbarMessage;
import com.akat.filmreel.util.SnackbarUtils;

import javax.inject.Inject;

public class MovieListFragment extends Fragment
        implements MovieListAdapter.OnItemClickHandler, MovieListAdapter.OnBottomReachedListener {

    @Inject
    public ViewModelProvider.Factory factory;

    private MovieListViewModel viewModel;

    private MovieListAdapter movieListAdapter;
    private View loadingIndicator;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        setHasOptionsMenu(true);

        loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        recyclerView = view.findViewById(R.id.recycler_view_movie_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        movieListAdapter = new MovieListAdapter(requireActivity(), this);
        movieListAdapter.setOnBottomReachedListener(this);
        recyclerView.setAdapter(movieListAdapter);

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reloadMovies();
            refreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, factory).get(MovieListViewModel.class);
        viewModel.getMovies().observe(this, entries -> {
            movieListAdapter.swapItems(entries);

            if (entries != null && entries.size() != 0) showDataView();
            else showLoading();
        });
        setupSnackbar();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search_action) {
            Navigation.findNavController(recyclerView).navigate(R.id.fragment_search);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, long movieId) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.PARAM.MOVIE_ID, movieId);

        Navigation.findNavController(view).navigate(R.id.fragment_movie_detail, bundle);
    }

    @Override
    public void onItemLongClick(View view, int position, long movieId, boolean isBookmarked) {
        viewModel.setBookmark(movieId, isBookmarked);
        movieListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onBottomReached() {
        viewModel.loadNewData();
    }

    private void showDataView() {
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void setupSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId))
        );
    }
}
