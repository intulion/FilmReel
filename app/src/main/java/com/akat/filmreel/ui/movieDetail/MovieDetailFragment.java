package com.akat.filmreel.ui.movieDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.ui.common.MovieListAdapter;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.SnackbarMessage;
import com.akat.filmreel.util.SnackbarUtils;
import com.bumptech.glide.Glide;

import javax.inject.Inject;

public class MovieDetailFragment extends Fragment
        implements RecommendListAdapter.OnItemClickHandler {

    @Inject
    public ViewModelProvider.Factory factory;

    private ImageView posterView;
    private ImageView backdropView;
    private TextView titleView;
    private TextView origTitleView;
    private TextView languageView;
    private TextView releaseDateView;
    private TextView ratingView;
    private TextView popularityView;
    private TextView overviewView;

    private MovieDetailViewModel viewModel;
    private RecommendListAdapter recommendListAdapter;
    private long movieId;
    private boolean isBookmarked;
    private Movie currentMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        setHasOptionsMenu(true);

        Bundle bundle = requireArguments();
        movieId = bundle.getLong(Constants.PARAM.MOVIE_ID);

        posterView = view.findViewById(R.id.movie_poster_img);
        backdropView = view.findViewById(R.id.movie_backdrop);
        titleView = view.findViewById(R.id.movie_title);
        origTitleView = view.findViewById(R.id.movie_orig_title);
        languageView = view.findViewById(R.id.movie_language);
        releaseDateView = view.findViewById(R.id.movie_release_date);
        ratingView = view.findViewById(R.id.movie_rating);
        popularityView = view.findViewById(R.id.movie_popularity);
        overviewView = view.findViewById(R.id.movie_overview);

        // Recommendations
        RecyclerView recommendView = view.findViewById(R.id.movie_recommend_list);
        recommendListAdapter = new RecommendListAdapter(requireActivity(), this);
        recommendView.setAdapter(recommendListAdapter);

        // Remind Button
        view.findViewById(R.id.movie_notification_btn).setOnClickListener(v -> {
            FragmentMoreInfo fragment = new FragmentMoreInfo();
            fragment.setMovie(currentMovie);
            fragment.show(requireActivity().getSupportFragmentManager(), fragment.getTag());
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        viewModel.getMovie(movieId).observe(this, this::fillMovieData);
        viewModel.getRecommendations(movieId).observe(this, entry ->
                recommendListAdapter.swapItems(entry)
        );

        setupSnackbar();
    }

    private void fillMovieData(Movie entry) {
        currentMovie = entry;

        titleView.setText(entry.getTitle());
        origTitleView.setText(entry.getOriginalTitle());
        overviewView.setText(entry.getOverview());
        languageView.setText(entry.getOriginalLanguage());
        popularityView.setText(
                getString(R.string.popularity_format, entry.getPopularity())
        );

        releaseDateView.setText(
                getString(R.string.date_format, entry.getReleaseDate())
        );

        ratingView.setText(
                getString(R.string.movie_rating_format,
                        entry.getVoteAverage(),
                        entry.getVoteCount())
        );

        String posterPath = entry.getPosterPath();
        if (posterPath != null) {
            Glide.with(posterView.getContext())
                    .load(Constants.HTTP.POSTER_URL + posterPath)
                    .into(posterView);
        }

        String backdropPath = entry.getBackdropPath();
        if (backdropPath != null) {
            Glide.with(posterView.getContext())
                    .load(Constants.HTTP.BACKDROP_URL + backdropPath)
                    .into(backdropView);
        }

        // set bookmark icon
        isBookmarked = entry.isBookmarked();
        requireActivity().invalidateOptionsMenu();
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
            bookmarkItem.setIcon(R.drawable.ic_bookmark);
        } else {
            bookmarkItem.setIcon(R.drawable.ic_bookmark_border);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.movie_share_text, currentMovie.getTitle())
                );
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;
            case R.id.menu_detail_bookmark:
                viewModel.setBookmark(movieId, isBookmarked);
                isBookmarked = !isBookmarked;
                requireActivity().invalidateOptionsMenu();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId))
        );
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
        recommendListAdapter.notifyItemChanged(position);
    }
}
