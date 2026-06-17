package com.smartanswer.app.domain.repository

import com.smartanswer.app.domain.model.AppSettings
import com.smartanswer.app.domain.model.GreetingType
import com.smartanswer.app.domain.model.SchedulerMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun setContestadorActive(active: Boolean)

    suspend fun setGreetingType(type: GreetingType)

    suspend fun setPredefinedGreetingId(id: Int)

    suspend fun setCustomGreetingPath(path: String)

    suspend fun setSchedulerMode(mode: SchedulerMode)
}
