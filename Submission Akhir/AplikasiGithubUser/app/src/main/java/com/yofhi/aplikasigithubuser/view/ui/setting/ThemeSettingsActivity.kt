package com.yofhi.aplikasigithubuser.view.ui.setting

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.viewModel.SettingsViewModelFactory
import com.yofhi.aplikasigithubuser.viewModel.ThemeSettingsViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSettingsActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_setting)

        val switchTheme: SwitchMaterial = findViewById(R.id.switch_theme)

        val pref = SettingsPreferences.getInstance(dataStore)

        val themeSettingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref)).get(
            ThemeSettingsViewModel::class.java
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        themeSettingsViewModel.getThemeSettings().observe(this) { isLightModeActive: Boolean ->
            if (isLightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.soft_green)))
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_700)))
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeSettingsViewModel.saveThemeSetting(isChecked)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Theme Setting"
            elevation = 0f
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}