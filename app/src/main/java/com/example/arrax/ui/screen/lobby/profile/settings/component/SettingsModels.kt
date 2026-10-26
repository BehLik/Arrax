package com.example.arrax.ui.screen.lobby.profile.settings.component

import androidx.annotation.StringRes
import com.example.arrax.R

enum class AboutLink(@StringRes val titleRes: Int) {
    Terms(R.string.settings_about_terms),
    Privacy(R.string.settings_about_privacy),
    Licenses(R.string.settings_about_licenses)
}