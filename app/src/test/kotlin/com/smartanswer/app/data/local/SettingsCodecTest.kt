package com.smartanswer.app.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import com.smartanswer.app.domain.model.AppSettings
import com.smartanswer.app.domain.model.GreetingType
import com.smartanswer.app.domain.model.SchedulerMode
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsCodecTest {

    @Test
    fun `scheduler mode encodes and decodes for all values`() {
        SchedulerMode.values().forEach { mode ->
            val decoded = SettingsCodec.decodeSchedulerMode(SettingsCodec.encodeSchedulerMode(mode))
            assertEquals(mode, decoded)
        }
    }

    @Test
    fun `greeting type encodes and decodes for all values`() {
        GreetingType.values().forEach { type ->
            val decoded = SettingsCodec.decodeGreetingType(SettingsCodec.encodeGreetingType(type))
            assertEquals(type, decoded)
        }
    }

    @Test
    fun `scheduler mode falls back to default for null`() {
        assertEquals(SchedulerMode.DEFAULT, SettingsCodec.decodeSchedulerMode(null))
    }

    @Test
    fun `scheduler mode falls back to default for unknown string`() {
        assertEquals(SchedulerMode.DEFAULT, SettingsCodec.decodeSchedulerMode("WEEKEND"))
    }

    @Test
    fun `greeting type falls back to default for null`() {
        assertEquals(GreetingType.DEFAULT, SettingsCodec.decodeGreetingType(null))
    }

    @Test
    fun `greeting type falls back to default for unknown string`() {
        assertEquals(GreetingType.DEFAULT, SettingsCodec.decodeGreetingType("VIDEO"))
    }

    @Test
    fun `decode empty preferences yields defaults`() {
        val settings = SettingsCodec.decode(emptyPreferences())
        assertEquals(AppSettings.DEFAULT, settings)
    }

    @Test
    fun `decode populated preferences yields stored values`() {
        val prefs: Preferences = mutablePreferencesOf(
            SettingsCodec.Keys.contestadorActive to true,
            SettingsCodec.Keys.greetingType to SettingsCodec.encodeGreetingType(GreetingType.CUSTOM),
            SettingsCodec.Keys.predefinedGreetingId to 3,
            SettingsCodec.Keys.customGreetingPath to "/greetings/x.m4a",
            SettingsCodec.Keys.schedulerMode to SettingsCodec.encodeSchedulerMode(SchedulerMode.NIGHT),
        )

        val settings = SettingsCodec.decode(prefs)

        assertEquals(true, settings.isContestadorActive)
        assertEquals(GreetingType.CUSTOM, settings.greetingType)
        assertEquals(3, settings.predefinedGreetingId)
        assertEquals("/greetings/x.m4a", settings.customGreetingPath)
        assertEquals(SchedulerMode.NIGHT, settings.schedulerMode)
    }

    @Test
    fun `decode corrupt enum values falls back to their defaults but keeps other keys`() {
        val prefs: Preferences = mutablePreferencesOf(
            SettingsCodec.Keys.contestadorActive to true,
            SettingsCodec.Keys.greetingType to "BOGUS",
            SettingsCodec.Keys.schedulerMode to "BOGUS",
        )

        val settings = SettingsCodec.decode(prefs)

        assertEquals(true, settings.isContestadorActive)
        assertEquals(GreetingType.DEFAULT, settings.greetingType)
        assertEquals(SchedulerMode.DEFAULT, settings.schedulerMode)
    }
}
