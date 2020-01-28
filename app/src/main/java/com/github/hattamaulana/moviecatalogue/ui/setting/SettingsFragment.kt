package com.github.hattamaulana.moviecatalogue.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.hattamaulana.moviecatalogue.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}