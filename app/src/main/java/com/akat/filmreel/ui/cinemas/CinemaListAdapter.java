package com.akat.filmreel.ui.cinemas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.databinding.ItemCinemaListBinding;
import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.places.dto.Location;

import java.util.List;

public class CinemaListAdapter extends RecyclerView.Adapter<CinemaListAdapter.CinemaListAdapterViewHolder> {

    private final Context context;
    private final CinemaListAdapterOnItemClickHandler clickHandler;
    private List<Cinema> cinemas;

    CinemaListAdapter(@NonNull Context context, CinemaListAdapterOnItemClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CinemaListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_cinema_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCinemaListBinding binding =
                DataBindingUtil.inflate(layoutInflater, layoutId, viewGroup, false);

        return new CinemaListAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaListAdapterViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);
        holder.bind(cinema);
    }

    void setItems(final List<Cinema> newCinemas) {
        cinemas = newCinemas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == cinemas) return 0;
        return cinemas.size();
    }

    public interface CinemaListAdapterOnItemClickHandler {
        void onItemClick(View view, String name, Double lat, Double lng);
    }

    class CinemaListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final ItemCinemaListBinding binding;

        CinemaListAdapterViewHolder(ItemCinemaListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }

        void bind(Cinema cinema) {
            binding.setCinema(cinema);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Cinema selectedCinema = cinemas.get(adapterPosition);

            Location cinemaLocation = selectedCinema.getGeometry().getLocation();

            clickHandler.onItemClick(
                    view,
                    selectedCinema.getName(),
                    cinemaLocation.getLat(),
                    cinemaLocation.getLng()
            );
        }
    }
}
