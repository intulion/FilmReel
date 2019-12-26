package com.akat.filmreel.ui.movieList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private int pageType = Constants.PAGER.NOW_PLAYING;

    private MovieListAdapter movieListAdapter;
    private View loadingIndicator;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieApplication.getAppComponent().inject(this);

        Bundle args = getArguments();
        if (args != null) {
            pageType = args.getInt(Constants.PARAM.PAGER_POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        recyclerView = view.findViewById(R.id.recycler_view_movie_list);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        movieListAdapter = new MovieListAdapter(requireActivity(), this);
        movieListAdapter.setOnBottomReachedListener(this);
        recyclerView.setAdapter(movieListAdapter);

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reloadMovies(pageType);
            refreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, factory).get(MovieListViewModel.class);
        viewModel.observeMovies(pageType);
        viewModel.getMovies(pageType).observe(this, entries -> {
            movieListAdapter.swapItems(entries);

            if (entries != null && entries.size() != 0) showDataView();
            else showLoading();
        });
        setupSnackbar();
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
        viewModel.loadNextPage(pageType);
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
