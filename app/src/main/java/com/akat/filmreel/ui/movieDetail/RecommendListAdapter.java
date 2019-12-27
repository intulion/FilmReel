package com.akat.filmreel.ui.movieDetail;

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

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.RecommendListAdapterViewHolder> {

    private final Context context;
    private final OnItemClickHandler clickHandler;
    private List<Movie> movies;

    RecommendListAdapter(@NonNull Context context, OnItemClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecommendListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_recommend_list;
        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new RecommendListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListAdapterViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.title.setText(movie.getTitle());

        holder.bookmark.setVisibility(movie.isBookmarked() ? View.VISIBLE : View.GONE);

        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            Glide.with(holder.poster.getContext())
                    .load(Constants.HTTP.POSTER_URL + posterPath)
                    .into(holder.poster);
        }
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

    class RecommendListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final CardView cardView;
        final ImageView poster;
        final ImageView bookmark;
        final TextView title;

        RecommendListAdapterViewHolder(View view) {
            super(view);

            cardView = itemView.findViewById(R.id.recommend_poster);
            poster = itemView.findViewById(R.id.recommend_poster_img);
            bookmark = itemView.findViewById(R.id.recommend_bookmark);
            title = itemView.findViewById(R.id.recommend_title);

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

            selectedMovie.setIsBookmarked(!isBookmarked);
            return true;
        }
    }

    private void setLongTapAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bookmark_animation);
        view.startAnimation(animation);
    }
}
