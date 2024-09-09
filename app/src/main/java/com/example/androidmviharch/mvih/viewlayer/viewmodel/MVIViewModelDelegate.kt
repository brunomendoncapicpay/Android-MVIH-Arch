package com.example.androidmviharch.mvih.viewlayer.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction

/**
 * Interface que define as funções necessárias para a comunicação entre a [IntentHandler] e a [MVIViewModel]
 *
 * @param S Tipo do estado
 * @param VA Tipo da ação
 * @param VD Tipo dos dados da view
 */
interface MVIViewModelDelegate<S : State, VA : ViewAction, VD : ViewData> {
    fun getViewData(): VD
    fun updateViewData(viewDataBuilder: (VD) -> VD)
    fun getCurrentState(): S
    fun updateState(newState: S)
    fun sendAction(action: VA)
    fun getSavedStateHandle(): SavedStateHandle?
}
