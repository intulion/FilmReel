package com.akat.filmreel.ui.bookmarks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.ui.common.MovieListAdapter;
import com.akat.filmreel.ui.movieList.MovieListViewModelFactory;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.InjectorUtils;

public class BookmarksFragment extends Fragment
        implements MovieListAdapter.MovieListAdapterOnItemClickHandler {

    private BookmarksViewModel viewModel;

    private MovieListAdapter movieListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_bookmarks);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        movieListAdapter = new MovieListAdapter(requireActivity(), this);
        recyclerView.setAdapter(movieListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BookmarksViewModelFactory factory =
                InjectorUtils.provideBookmarksViewModelFactory(requireActivity());
        viewModel = ViewModelProviders.of(this, factory).get(BookmarksViewModel.class);
        viewModel.getBookmarkedMovies().observe(this, entries -> {
            movieListAdapter.swapItems(entries);
        });
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

}
