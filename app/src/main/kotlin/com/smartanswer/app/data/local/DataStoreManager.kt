package com.smartanswer.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    val data: Flow<Preferences> get() = dataStore.data

    suspend fun edit(transform: suspend (MutablePreferences) -> Unit): Preferences =
        dataStore.edit(transform)
}
