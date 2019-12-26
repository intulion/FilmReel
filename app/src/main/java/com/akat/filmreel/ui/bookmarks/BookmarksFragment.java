package com.akat.filmreel.ui.bookmarks;

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

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.ui.common.MovieListAdapter;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.SnackbarMessage;
import com.akat.filmreel.util.SnackbarUtils;

import javax.inject.Inject;

public class BookmarksFragment extends Fragment
        implements MovieListAdapter.OnItemClickHandler {

    @Inject
    public ViewModelProvider.Factory factory;

    private BookmarksViewModel viewModel;
    private MovieListAdapter movieListAdapter;

    private RecyclerView recyclerView;
    private View noItemView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        noItemView = view.findViewById(R.id.bookmarks_no_item);

        recyclerView = view.findViewById(R.id.recycler_view_bookmarks);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        movieListAdapter = new MovieListAdapter(requireActivity(), this);
        recyclerView.setAdapter(movieListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, factory).get(BookmarksViewModel.class);
        viewModel.getBookmarkedMovies().observe(this, entries -> {
            movieListAdapter.swapItems(entries);
            updateDataView();
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
        updateDataView();
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

    private void setupSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId))
        );
    }
}
