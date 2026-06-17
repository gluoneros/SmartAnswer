package com.smartanswer.app.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.smartanswer.app.domain.model.AppSettings
import com.smartanswer.app.domain.model.GreetingType
import com.smartanswer.app.domain.model.SchedulerMode

object SettingsCodec {

    fun encodeSchedulerMode(mode: SchedulerMode): String = mode.name

    fun decodeSchedulerMode(value: String?): SchedulerMode =
        value?.let { runCatching { SchedulerMode.valueOf(it) }.getOrNull() } ?: SchedulerMode.DEFAULT

    fun encodeGreetingType(type: GreetingType): String = type.name

    fun decodeGreetingType(value: String?): GreetingType =
        value?.let { runCatching { GreetingType.valueOf(it) }.getOrNull() } ?: GreetingType.DEFAULT

    fun decode(preferences: Preferences): AppSettings = AppSettings(
        isContestadorActive = preferences[Keys.contestadorActive] ?: AppSettings.DEFAULT.isContestadorActive,
        greetingType = decodeGreetingType(preferences[Keys.greetingType]),
        predefinedGreetingId = preferences[Keys.predefinedGreetingId] ?: AppSettings.DEFAULT.predefinedGreetingId,
        customGreetingPath = preferences[Keys.customGreetingPath] ?: AppSettings.DEFAULT.customGreetingPath,
        schedulerMode = decodeSchedulerMode(preferences[Keys.schedulerMode]),
    )

    object Keys {
        val contestadorActive = booleanPreferencesKey("contestador_active")
        val greetingType = stringPreferencesKey("greeting_type")
        val predefinedGreetingId = intPreferencesKey("predefined_greeting_id")
        val customGreetingPath = stringPreferencesKey("custom_greeting_path")
        val schedulerMode = stringPreferencesKey("scheduler_mode")
    }
}
