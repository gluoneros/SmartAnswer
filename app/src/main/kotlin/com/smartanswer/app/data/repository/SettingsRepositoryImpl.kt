package com.smartanswer.app.data.repository

import com.smartanswer.app.data.local.DataStoreManager
import com.smartanswer.app.data.local.SettingsCodec
import com.smartanswer.app.domain.model.AppSettings
import com.smartanswer.app.domain.model.GreetingType
import com.smartanswer.app.domain.model.SchedulerMode
import com.smartanswer.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
) : SettingsRepository {

    override val settings: Flow<AppSettings> =
        dataStoreManager.data.map(SettingsCodec::decode)

    override suspend fun setContestadorActive(active: Boolean) {
        dataStoreManager.edit { it[SettingsCodec.Keys.contestadorActive] = active }
    }

    override suspend fun setGreetingType(type: GreetingType) {
        dataStoreManager.edit { it[SettingsCodec.Keys.greetingType] = SettingsCodec.encodeGreetingType(type) }
    }

    override suspend fun setPredefinedGreetingId(id: Int) {
        dataStoreManager.edit { it[SettingsCodec.Keys.predefinedGreetingId] = id }
    }

    override suspend fun setCustomGreetingPath(path: String) {
        dataStoreManager.edit { it[SettingsCodec.Keys.customGreetingPath] = path }
    }

    override suspend fun setSchedulerMode(mode: SchedulerMode) {
        dataStoreManager.edit { it[SettingsCodec.Keys.schedulerMode] = SettingsCodec.encodeSchedulerMode(mode) }
    }
}
