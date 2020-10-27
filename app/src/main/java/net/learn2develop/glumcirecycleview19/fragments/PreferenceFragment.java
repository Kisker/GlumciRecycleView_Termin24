package net.learn2develop.glumcirecycleview19.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import net.learn2develop.glumcirecycleview19.R;

public class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
    }
}