package com.akat.filmreel.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.util.Constants;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MovieListAdapterViewHolder> {
    private static final int BOTTOM_REACHED_RANGE = 5;

    private final Context context;
    private final OnItemClickHandler clickHandler;
    private final OnBottomReachedListener bottomReachedListener;
    private List<MovieEntity> movies;

    SearchAdapter(@NonNull Context context, OnItemClickHandler clickHandler,
                         OnBottomReachedListener bottomReachedListener) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.bottomReachedListener = bottomReachedListener;
    }

    @NonNull
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_list, viewGroup, false);
        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapterViewHolder holder, int position) {
        MovieEntity movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        holder.originalTitle.setText(movie.getOriginalTitle());
        holder.rating.setText(
                String.format(holder.ratingFormat,
                        movie.getVoteAverage(),
                        movie.getVoteCount()
                )
        );

        Date releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            holder.releaseDate.setText(
                    String.format(holder.dateFormat, releaseDate)
            );
        }

        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            Glide.with(holder.poster.getContext())
                    .load(Constants.HTTP.SMALL_POSTER_URL + posterPath)
                    .into(holder.poster);
        }

        if (position == getItemCount() - BOTTOM_REACHED_RANGE) {
            bottomReachedListener.onBottomReached(position);
        }
    }

    void swapItems(final List<MovieEntity> newMovies) {
        movies = newMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public interface OnItemClickHandler {
        void onItemClick(View view, MovieEntity movie);
    }

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }

    class MovieListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final CardView cardView;
        final ImageView poster;
        final TextView title;
        final TextView originalTitle;
        final TextView rating;
        final TextView releaseDate;

        final String ratingFormat;
        final String dateFormat;

        MovieListAdapterViewHolder(View view) {
            super(view);

            cardView = itemView.findViewById(R.id.movie_list_card);
            poster = itemView.findViewById(R.id.movie_list_img);
            title = itemView.findViewById(R.id.movie_list_title);
            originalTitle = itemView.findViewById(R.id.movie_list_orig_title);
            rating = itemView.findViewById(R.id.movie_list_rating);
            releaseDate = itemView.findViewById(R.id.movie_list_release_date);

            ratingFormat = itemView.getResources().getString(R.string.movie_rating_format);
            dateFormat = itemView.getResources().getString(R.string.date_format);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickHandler.onItemClick(view, movies.get(position));
        }
    }
}
