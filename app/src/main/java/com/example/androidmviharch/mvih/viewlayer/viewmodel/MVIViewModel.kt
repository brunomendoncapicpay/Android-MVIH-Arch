package com.example.androidmviharch.mvih.viewlayer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.androidmviharch.mvih.viewlayer.guard.StateGuard
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewStateWrapper
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewintent.IntentHandlerProvider
import com.example.androidmviharch.mvih.viewlayer.viewintent.ViewIntent
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewActionWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel base para utilização do padrão MVI
 *
 * @param S Tipo do estado
 * @param VA Tipo da ação
 * @param VD Tipo dos dados da view
 * @param VI Tipo do intent
 *
 * @property state Estado atual da view
 * @property action Ação a ser executada
 */
abstract class MVIViewModel<S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> : ViewModel() {
    abstract val state: StateFlow<ViewStateWrapper<S, VD>>
    abstract val action: SharedFlow<ViewActionWrapper<VA>>
    abstract fun onIntent(intent: VI)

    companion object {
        /**
         * Cria uma instância de [MVIViewModel]
         *
         * @param featureName Nome da feature utilizado para logs
         * @param initialState Estado inicial da view
         * @param errorState Estado de erro da view utilizado em erros internos
         * @param viewData Dados da view
         * @param intentHandlerProvider Provedor de handlers de intents
         * @param stateGuard Guardião do estado
         * @param savedStateHandle Handle de estado salvo
         * @param operationDispatcher Dispatcher para operações assíncronas
         */
        @Suppress("LongParameterList")
        fun <S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> create(
            featureName: String,
            initialState: S,
            errorState: S,
            viewData: VD,
            intentHandlerProvider: IntentHandlerProvider<S, VA, VD, VI>,
            stateGuard: StateGuard,
            savedStateHandle: SavedStateHandle? = null,
            operationDispatcher: CoroutineDispatcher = Dispatchers.Default
        ): MVIViewModel<S, VA, VD, VI> {
            return MVIViewModelImpl(
                featureName = featureName,
                initialState = initialState,
                errorState = errorState,
                viewData = viewData,
                intentHandlerProvider = intentHandlerProvider,
                stateGuard = stateGuard,
                savedStateHandle = savedStateHandle,
                operationDispatcher = operationDispatcher,
            )
        }
    }
}
