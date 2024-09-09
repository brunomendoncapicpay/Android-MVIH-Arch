package com.example.androidmviharch.mvih.viewlayer.viewintent

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewmodel.MVIViewModelDelegate
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction

/**
 * Interface responsável por abstrair ações a serem executadas para cada intent da View
 */
interface IntentHandler<S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> {
    suspend fun handle(intent: VI, viewModelDelegate: MVIViewModelDelegate<S, VA, VD>)
}
