package com.example.android.quetico;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    public static class QueticoPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference source = findPreference("source");
            bindSummaryToValue(source);

            Preference sortBy = findPreference("sortBy");
            bindSummaryToValue(sortBy);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String storedValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(storedValue);

                if (index >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    listPreference.setSummary(labels[index]);

                }


            } else {

                preference.setSummary(storedValue);
            }

            return true;
        }


        private void bindSummaryToValue(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String value = sharedPrefs.getString(pref.getKey(), "");
            onPreferenceChange(pref, value);

        }


    }
}
