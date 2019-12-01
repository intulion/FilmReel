package com.akat.filmreel.ui.common;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.akat.filmreel.util.Constants;
import com.bumptech.glide.Glide;

public class BindingAdapters {
    @BindingAdapter("app:poster")
    public static void setPosterUrl(ImageView view, String path) {
        if (path != null) {
            Glide.with(view.getContext())
                    .load(Constants.HTTP.POSTER_URL + path)
                    .into(view);
        }
    }

    @BindingAdapter("app:backdrop")
    public static void setBackdropUrl(ImageView view, String path) {
        if (path != null) {
            Glide.with(view.getContext())
                    .load(Constants.HTTP.BACKDROP_URL + path)
                    .into(view);
        }
    }
}
