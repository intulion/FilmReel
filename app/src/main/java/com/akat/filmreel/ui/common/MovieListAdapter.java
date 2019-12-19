package com.akat.filmreel.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private final Context context;
    private final OnItemClickHandler clickHandler;
    private OnBottomReachedListener onBottomReachedListener;
    private List<Movie> movies;
    private int lastPosition = -1;

    public MovieListAdapter(@NonNull Context context, OnItemClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @NonNull
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_movie_list;
        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapterViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        holder.originalTitle.setText(movie.getOriginalTitle());
        holder.overview.setText(movie.getOverview());
        holder.rating.setText(
                String.format(holder.ratingFormat,
                        movie.getVoteAverage(),
                        movie.getVoteCount()
                )
        );
        holder.releaseDate.setText(
                String.format(holder.dateFormat, movie.getReleaseDate())
        );

        holder.bookmark.setVisibility(movie.isBookmarked() ? View.VISIBLE : View.GONE);

        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            Glide.with(holder.poster.getContext())
                    .load(Constants.HTTP.POSTER_URL + posterPath)
                    .into(holder.poster);
        }

        if (onBottomReachedListener != null && position == getItemCount() - 5) {
            onBottomReachedListener.onBottomReached();
        }

        setAnimation(holder.itemView, position);
    }

    public void swapItems(final List<Movie> newMovies) {
        if (movies == null || newMovies == null) {
            movies = newMovies;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return movies.size();
                }

                @Override
                public int getNewListSize() {
                    return newMovies.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return movies.get(oldItemPosition).getId() ==
                            newMovies.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Movie newMovie = newMovies.get(newItemPosition);
                    Movie oldMovie = movies.get(oldItemPosition);
                    return newMovie.getId() == oldMovie.getId()
                            && newMovie.getTitle().equals(oldMovie.getTitle());
                }
            });
            movies = newMovies;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public interface OnItemClickHandler {
        void onItemClick(View view, long movieId);

        void onItemLongClick(View view, int position, long movieId, boolean isBookmarked);
    }

    public interface OnBottomReachedListener {
        void onBottomReached();
    }

    class MovieListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final CardView cardView;
        final ImageView poster;
        final ImageView bookmark;
        final TextView title;
        final TextView originalTitle;
        final TextView overview;
        final TextView rating;
        final TextView releaseDate;

        final String ratingFormat;
        final String dateFormat;

        MovieListAdapterViewHolder(View view) {
            super(view);

            cardView = itemView.findViewById(R.id.movie_list_card);
            poster = itemView.findViewById(R.id.movie_list_img);
            bookmark = itemView.findViewById(R.id.movie_list_bookmark);
            title = itemView.findViewById(R.id.movie_list_title);
            originalTitle = itemView.findViewById(R.id.movie_list_orig_title);
            overview = itemView.findViewById(R.id.movie_list_overview);
            rating = itemView.findViewById(R.id.movie_list_rating);
            releaseDate = itemView.findViewById(R.id.movie_list_release_date);

            ratingFormat = itemView.getResources().getString(R.string.movie_rating_format);
            dateFormat = itemView.getResources().getString(R.string.date_format);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies.get(adapterPosition);

            clickHandler.onItemClick(view, selectedMovie.getId());
        }

        @Override
        public boolean onLongClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies.get(adapterPosition);
            boolean isBookmarked = selectedMovie.isBookmarked();

            setLongTapAnimation(cardView);
            clickHandler.onItemLongClick(view, adapterPosition, selectedMovie.getId(), isBookmarked);

            selectedMovie.setBookmark(!isBookmarked);
            return true;
        }
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_list_animation);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setLongTapAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bookmark_animation);
        view.startAnimation(animation);
    }

}
