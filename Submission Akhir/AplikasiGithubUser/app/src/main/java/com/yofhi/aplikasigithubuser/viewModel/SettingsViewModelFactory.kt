package com.yofhi.aplikasigithubuser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yofhi.aplikasigithubuser.view.ui.setting.SettingsPreferences

class SettingsViewModelFactory(private val pref: SettingsPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeSettingsViewModel::class.java)) {
            return ThemeSettingsViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(pref) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class:" + modelClass.name)
    }
}