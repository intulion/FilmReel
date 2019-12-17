package com.akat.filmreel.di;

import com.akat.filmreel.data.local.AppPreferences;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Module
public class TestAppModule {

    @Provides
    AppPreferences providePreferences() {
        AppPreferences preferences = mock(AppPreferences.class);
        when(preferences.getLastPage()).thenReturn(0);
        when(preferences.getTotalPages()).thenReturn(1);
        when(preferences.getLocale()).thenReturn("en-US");

        return preferences;
    }
}