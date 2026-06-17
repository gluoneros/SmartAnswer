package com.smartanswer.app.domain.model

enum class SchedulerMode {
    ALWAYS,
    BUSINESS,
    NIGHT;

    companion object {
        val DEFAULT: SchedulerMode = ALWAYS
    }
}
