package com.akat.filmreel.ui.movieDetail;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.akat.filmreel.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Locale;

public class FragmentMoreInfo extends BottomSheetDialogFragment {

    private BottomSheetBehavior behavior;

    private List<Integer> genres;

    void setGenres(final List<Integer> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.sheet_more_details, null);

        dialog.setContentView(view);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        (view.findViewById(R.id.sheet_more_close_btn)).setOnClickListener(v -> dismiss());

        ChipGroup chipGroup = view.findViewById(R.id.sheet_more_genres_group);
        for (Integer genre : genres) {
            Chip chip = new Chip(requireContext());
            chip.setText(
                    String.format(Locale.getDefault(), "%d", genre)
            );
            chipGroup.addView(chip);
        }

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}