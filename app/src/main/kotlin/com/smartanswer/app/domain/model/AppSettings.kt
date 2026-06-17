package com.smartanswer.app.domain.model

data class AppSettings(
    val isContestadorActive: Boolean,
    val greetingType: GreetingType,
    val predefinedGreetingId: Int,
    val customGreetingPath: String,
    val schedulerMode: SchedulerMode,
) {
    companion object {
        val DEFAULT = AppSettings(
            isContestadorActive = false,
            greetingType = GreetingType.DEFAULT,
            predefinedGreetingId = 0,
            customGreetingPath = "",
            schedulerMode = SchedulerMode.DEFAULT,
        )
    }
}
