package com.example.arrax.ui.screen.lobby.settings.component

import androidx.annotation.StringRes
import com.example.arrax.R

enum class SettingsPreference(@StringRes val titleRes: Int, @StringRes val subtitleRes: Int) {
    Language(R.string.settings_language_title, R.string.settings_language_subtitle),
    Backup(R.string.settings_backup_title, R.string.settings_backup_subtitle)
}

enum class AboutLink(@StringRes val titleRes: Int) {
    Terms(R.string.settings_about_terms),
    Privacy(R.string.settings_about_privacy),
    Licenses(R.string.settings_about_licenses)
}