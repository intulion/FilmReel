package com.akat.filmreel.ui.movieDetail;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.akat.filmreel.R;
import com.akat.filmreel.ui.MainActivity;
import com.akat.filmreel.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentMoreInfo extends BottomSheetDialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private BottomSheetBehavior behavior;
    private TextView dateTextView;
    private TextView timeTextView;

    private List<Integer> genres;
    private String movieTitle;
    private Calendar remindTime = Calendar.getInstance();

    void setGenres(final List<Integer> genres) {
        this.genres = genres;
    }

    void setTitle(final String movieTitle) {
        this.movieTitle = movieTitle;
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

        // Genres
        ChipGroup chipGroup = view.findViewById(R.id.sheet_more_genres_group);
        for (Integer genre : genres) {
            Chip chip = new Chip(requireContext());
            chip.setText(
                    String.format(Locale.getDefault(), "%d", genre)
            );
            chipGroup.addView(chip);
        }

        // Reminder
        dateTextView = view.findViewById(R.id.sheet_more_date);
        dateTextView.setOnClickListener(v -> setDate());

        timeTextView = view.findViewById(R.id.sheet_more_time);
        timeTextView.setOnClickListener(v -> setTime());

        updateReminderDateTime();

        (view.findViewById(R.id.sheet_more_add_btn)).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MovieReminder.class);
            intent.putExtra(Constants.PARAM.MOVIE_TITLE, movieTitle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            AlarmManager manager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP, remindTime.getTimeInMillis(), pendingIntent);

            dismiss();
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void updateReminderDateTime() {
        dateTextView.setText(getResources().getString(R.string.date_format, remindTime));
        timeTextView.setText(getResources().getString(R.string.time_format, remindTime));
    }

    private void setDate() {
        new DatePickerDialog(requireContext(), this,
                remindTime.get(Calendar.YEAR),
                remindTime.get(Calendar.MONTH),
                remindTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setTime() {
        new TimePickerDialog(requireContext(), this,
                remindTime.get(Calendar.HOUR_OF_DAY),
                remindTime.get(Calendar.MINUTE), true)
                .show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        remindTime.set(Calendar.YEAR, year);
        remindTime.set(Calendar.MONTH, monthOfYear);
        remindTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateReminderDateTime();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        remindTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        remindTime.set(Calendar.MINUTE, minute);
        updateReminderDateTime();
    }
}