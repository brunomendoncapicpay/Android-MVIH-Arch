package com.example.androidmviharch.logs

data class TrackBack(
    val stage: String,
    val label: String,
    val result: String? = null,
    val additional: Map<String, Any> = emptyMap()
)