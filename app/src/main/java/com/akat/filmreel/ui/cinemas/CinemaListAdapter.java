package com.akat.filmreel.ui.cinemas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.places.dto.Location;

import java.util.List;

public class CinemaListAdapter extends RecyclerView.Adapter<CinemaListAdapter.CinemaListAdapterViewHolder> {

    private final Context context;
    private final CinemaListAdapter.CinemaListAdapterOnItemClickHandler clickHandler;
    private List<Cinema> cinemas;

    CinemaListAdapter(@NonNull Context context, CinemaListAdapter.CinemaListAdapterOnItemClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CinemaListAdapter.CinemaListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema_list, viewGroup, false);
        return new CinemaListAdapter.CinemaListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaListAdapter.CinemaListAdapterViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);

        holder.name.setText(cinema.getName());
        holder.vicinity.setText(cinema.getVicinity());
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

        final TextView name;
        final TextView vicinity;

        CinemaListAdapterViewHolder(View view) {
            super(view);

            name = itemView.findViewById(R.id.cinema_list_name);
            vicinity = itemView.findViewById(R.id.cinema_list_vicinity);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Cinema selectedCinema = cinemas.get(getAdapterPosition());
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
