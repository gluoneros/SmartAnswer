package com.smartanswer.app.domain.model

enum class GreetingType {
    PREDEFINED,
    CUSTOM;

    companion object {
        val DEFAULT: GreetingType = PREDEFINED
    }
}
