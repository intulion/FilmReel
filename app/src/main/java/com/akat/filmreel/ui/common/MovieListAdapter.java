package com.akat.filmreel.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.databinding.ItemMovieListBinding;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private final Context context;
    private final OnItemClickHandler clickHandler;
    private OnBottomReachedListener onBottomReachedListener;
    private List<MovieWithBookmark> movies;
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemMovieListBinding binding =
                DataBindingUtil.inflate(layoutInflater, layoutId, viewGroup, false);

        return new MovieListAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapterViewHolder holder, int position) {
        MovieWithBookmark movie = movies.get(position);
        holder.bind(movie);

        if (onBottomReachedListener != null && position == getItemCount() - 3) {
            onBottomReachedListener.onBottomReached(position);
        }

        setAnimation(holder.itemView, position);
    }

    public void swapItems(final List<MovieWithBookmark> newMovies) {

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
                    MovieWithBookmark newMovie = newMovies.get(newItemPosition);
                    MovieWithBookmark oldMovie = movies.get(oldItemPosition);
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
        void onBottomReached(int position);
    }

    class MovieListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final ItemMovieListBinding binding;

        final CardView cardView;

        MovieListAdapterViewHolder(ItemMovieListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            cardView = itemView.findViewById(R.id.movie_list_card);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(MovieWithBookmark movie) {
            binding.setMovieItem(movie);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieWithBookmark selectedMovie = movies.get(adapterPosition);

            clickHandler.onItemClick(view, selectedMovie.getId());
        }

        @Override
        public boolean onLongClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieWithBookmark selectedMovie = movies.get(adapterPosition);
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
