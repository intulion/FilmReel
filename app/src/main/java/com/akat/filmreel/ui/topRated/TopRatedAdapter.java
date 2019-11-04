package com.akat.filmreel.ui.topRated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.TopRatedAdapterViewHolder> {

    private final Context context;
    private final TopRatedAdapterOnItemClickHandler clickHandler;
    private List<Movie> movies;

    TopRatedAdapter(@NonNull Context context, TopRatedAdapterOnItemClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TopRatedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_top_rated;
        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new TopRatedAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRatedAdapterViewHolder holder, int position) {
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

        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            Glide.with(holder.poster.getContext())
                    .load(Constants.HTTP.IMAGE_URL + posterPath)
                    .into(holder.poster);
        }
    }

    public void swapItems(final List<Movie> newMovies) {

        if (movies == null) {
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

    public interface TopRatedAdapterOnItemClickHandler {
        void onItemClick(View view, long movieId);
    }

    class TopRatedAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView poster;
        final TextView title;
        final TextView originalTitle;
        final TextView overview;
        final TextView rating;
        final TextView releaseDate;

        final String ratingFormat;
        final String dateFormat;

        TopRatedAdapterViewHolder(View view) {
            super(view);

            poster = itemView.findViewById(R.id.top_rated_img);
            title = itemView.findViewById(R.id.top_rated_title);
            originalTitle = itemView.findViewById(R.id.top_rated_orig_title);
            overview = itemView.findViewById(R.id.top_rated_overview);
            rating = itemView.findViewById(R.id.top_rated_rating);
            releaseDate = itemView.findViewById(R.id.top_rated_release_date);

            ratingFormat = itemView.getResources().getString(R.string.top_rated_rating);
            dateFormat = itemView.getResources().getString(R.string.date_format);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies.get(adapterPosition);

            clickHandler.onItemClick(view, selectedMovie.getId());
        }
    }

}
