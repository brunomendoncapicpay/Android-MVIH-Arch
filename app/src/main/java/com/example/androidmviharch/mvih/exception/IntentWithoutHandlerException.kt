package com.example.androidmviharch.mvih.exception

import com.example.androidmviharch.mvih.viewlayer.viewintent.ViewIntent

class IntentWithoutHandlerException(
    viewIntent: ViewIntent
) : Exception("Intent ${viewIntent::class.java} is not mapped")
