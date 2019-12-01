package com.akat.filmreel.ui.movieDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.databinding.FragmentMovieDetailBinding;
import com.akat.filmreel.ui.common.MovieListViewModel;
import com.akat.filmreel.ui.common.MovieListViewModelFactory;
import com.akat.filmreel.util.InjectorUtils;

public class MovieDetailFragment extends Fragment {

    private FragmentMovieDetailBinding binding;

    private MovieListViewModel viewModel;
    private boolean isBookmarked;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        binding = DataBindingUtil.bind(rootView);
        if (binding != null) {
            binding.setEventsHandler(new MovieDetailEventsHandler());
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MovieListViewModelFactory factory =
                InjectorUtils.provideMovieListViewModelFactory(requireActivity());
        viewModel = ViewModelProviders.of(requireActivity(), factory).get(MovieListViewModel.class);
        viewModel.getMovie().observe(this, entry -> {
            if (entry != null) {
                binding.setMovie(entry);

                // set bookmark icon
                isBookmarked = entry.isBookmarked();
                requireActivity().invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem bookmarkItem = menu.findItem(R.id.menu_detail_bookmark);
        if (isBookmarked) {
            bookmarkItem.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark));
        } else {
            bookmarkItem.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_border));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.movie_share_text, binding.getMovie().getTitle())
                );
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;
            case R.id.menu_detail_bookmark:
                viewModel.setBookmark(binding.getMovie().getId(), isBookmarked);
                isBookmarked = !isBookmarked;
                requireActivity().invalidateOptionsMenu();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieDetailEventsHandler {
        public void onMoreButtonClick(View v, MovieWithBookmark movie) {
            FragmentMoreInfo fragment = new FragmentMoreInfo();
            fragment.setGenres(movie.getGenreIds());
            fragment.show(requireActivity().getSupportFragmentManager(), fragment.getTag());
        }
    }

}
