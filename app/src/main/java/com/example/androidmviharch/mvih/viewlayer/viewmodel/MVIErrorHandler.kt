package com.example.androidmviharch.mvih.viewlayer.viewmodel

interface MVIErrorHandler {
    fun handleError(functionName: String, info: String?, error: Throwable)
}
