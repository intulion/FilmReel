package com.akat.filmreel.ui.movieList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.akat.filmreel.util.Constants;

public class MoviePagerAdapter extends FragmentStateAdapter {

    MoviePagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PARAM.PAGER_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
