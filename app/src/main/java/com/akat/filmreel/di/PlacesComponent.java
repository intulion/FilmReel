package com.akat.filmreel.di;

import com.akat.filmreel.ui.cinemas.CinemaListFragment;

import dagger.Subcomponent;

@PlacesScope
@Subcomponent
public interface PlacesComponent {

    @Subcomponent.Factory
    interface Factory {
        PlacesComponent create();
    }

    void inject(CinemaListFragment fragment);
}
