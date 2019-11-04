package com.akat.filmreel.ui.topRated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akat.filmreel.R;
import com.akat.filmreel.util.InjectorUtils;

public class TopRatedFragment extends Fragment implements TopRatedAdapter.TopRatedAdapterOnItemClickHandler {

    private TopRatedAdapter topRatedAdapter;
    private ProgressBar loadingIndicator;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_top_rated, container, false);

        loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        recyclerView = view.findViewById(R.id.recycler_view_top_rated);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        topRatedAdapter = new TopRatedAdapter(requireActivity(), this);
        recyclerView.setAdapter(topRatedAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TopRatedViewModelFactory factory = InjectorUtils.provideTopRatedViewModelFactory(
                requireActivity().getApplicationContext());
        TopRatedViewModel viewModel = ViewModelProviders.of(this, factory).get(TopRatedViewModel.class);
        viewModel.getMovies().observe(this, entries -> {
            topRatedAdapter.swapItems(entries);

            if (entries != null && entries.size() != 0) showGamesDataView();
            else showLoading();
        });
    }

    @Override
    public void onItemClick(View view, long movieId) {

    }

    private void showGamesDataView() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
