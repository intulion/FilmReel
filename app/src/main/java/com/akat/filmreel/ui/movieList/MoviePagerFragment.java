package com.akat.filmreel.ui.movieList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.akat.filmreel.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.akat.filmreel.util.Constants.PAGER.POPULAR;
import static com.akat.filmreel.util.Constants.PAGER.TOP_RATED;
import static com.akat.filmreel.util.Constants.PAGER.UPCOMING;

public class MoviePagerFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_pager, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MoviePagerAdapter pagerAdapter = new MoviePagerAdapter(this);
        ViewPager2 viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(
                        getTabTitle(position)
                )
        ).attach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search_action) {
            Navigation.findNavController(rootView).navigate(R.id.fragment_search);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getTabTitle(int position) {
        switch (position) {
            case TOP_RATED:
                return R.string.tab_title_top_rated;
            case POPULAR:
                return R.string.tab_title_popular;
            case UPCOMING:
                return R.string.tab_title_upcoming;
            default:
                return R.string.tab_title_now_playing;
        }
    }
}
