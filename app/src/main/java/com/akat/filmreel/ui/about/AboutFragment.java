package com.akat.filmreel.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.R;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView version = view.findViewById(R.id.about_version);
        version.setText(BuildConfig.VERSION_NAME);

        view.findViewById(R.id.about_github).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/intulion/FilmReel"));
            startActivity(intent);
        });

        view.findViewById(R.id.about_mail).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:intulion@gmail.com"));
            startActivity(intent);
        });

        return view;
    }
}
