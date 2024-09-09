package com.example.androidmviharch.logs

import android.util.Log

private const val BACKTRACK_SCOPE = "trackBackScope"
private const val BACKTRACK_STAGE = "trackBackStage"
private const val BACKTRACK_LABEL = "trackBackLabel"
private const val BACKTRACK_RESULT = "trackBackResult"

class Devlytics(private val scope: String) {
    fun logError(trackBack: TrackBack, throwable: Throwable) {
        logError(throwable, trackBack.toAttrMap())
    }

    private fun logError(throwable: Throwable, value: Map<String, Any?>) {
        Log.e("Devlytics $scope", "logError: $throwable, $value")
    }

    private fun TrackBack.toAttrMap(throwable: Throwable? = null): Map<String, Any?> {
        return mapOf(
            BACKTRACK_SCOPE to scope,
            BACKTRACK_STAGE to stage,
            BACKTRACK_LABEL to label,
        )
            .plusBacktrackResult(result, throwable)
            .plus(additional)
    }

    private fun Map<String, Any?>.plusBacktrackResult(result: String?, throwable: Throwable?): Map<String, Any?> {
        return if (result.isNullOrEmpty()) {
            throwable?.let { plus(BACKTRACK_RESULT to it.message) } ?: this
        } else {
            plus(BACKTRACK_RESULT to result)
        }
    }
}