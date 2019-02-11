package in.ajna.ajnamobile.ajna.Settings;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import in.ajna.ajnamobile.ajna.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
