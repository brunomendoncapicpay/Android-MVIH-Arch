package com.example.androidmviharch.mvih.viewlayer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.androidmviharch.BuildConfig
import com.example.androidmviharch.logs.Devlytics
import com.example.androidmviharch.logs.TrackBack
import com.example.androidmviharch.mvih.exception.IntentWithoutHandlerException
import com.example.androidmviharch.mvih.viewlayer.guard.StateGuard
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewStateWrapper
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewintent.IntentHandlerProvider
import com.example.androidmviharch.mvih.viewlayer.viewintent.ViewIntent
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewActionWrapper
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewModifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val MVI_SCOPE = "mvi_arch"

/**
 * Implementação da [MVIViewModel] que gerencia o estado da view
 *
 * @param S Tipo do estado
 * @param VA Tipo da ação
 * @param VD Tipo dos dados da view
 * @param VI Tipo do intent
 * @property featureName Nome da feature utilizado para logs
 * @property initialState Estado inicial da view
 * @property errorState Estado de erro da view utilizado em erros internos
 * @property viewData Dados da view
 * @property intentHandlerProvider Provedor de handlers de intents
 * @property stateGuard Guardião do estado
 * @property savedStateHandle Handle de estado salvo
 * @property operationDispatcher Dispatcher para operações assíncronas
 */
@Suppress("LongParameterList")
class MVIViewModelImpl<S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> internal constructor(
    private val featureName: String,
    initialState: S,
    private val errorState: S,
    private var viewData: VD,
    private val intentHandlerProvider: IntentHandlerProvider<S, VA, VD, VI>,
    private val stateGuard: StateGuard,
    private val savedStateHandle: SavedStateHandle?,
    operationDispatcher: CoroutineDispatcher,
    private val devlytics: Devlytics = Devlytics(MVI_SCOPE)
) : MVIViewModel<S, VA, VD, VI>(), MVIViewModelDelegate<S, VA, VD>, MVIErrorHandler {

    private val _state: MutableStateFlow<ViewStateWrapper<S, VD>> =
        MutableStateFlow(ViewStateWrapper(initialState, viewData, this))
    override val state: StateFlow<ViewStateWrapper<S, VD>> = _state

    private val _action = MutableSharedFlow<ViewActionWrapper<VA>>(replay = 0)
    override val action: SharedFlow<ViewActionWrapper<VA>> = _action

    private val intentQueue = MutableSharedFlow<VI>(replay = 0)

    init {
        viewModelScope.launch(operationDispatcher) {
            intentQueue.collect { intent ->
                runCatching {
                    intentHandlerProvider.get(intent::class)
                        ?.handle(intent, this@MVIViewModelImpl)
                        ?: throw IntentWithoutHandlerException(intent)
                }.onFailure {
                    handleError("onIntent", intent::class.simpleName, it)
                }
            }
        }
    }

    override fun onIntent(intent: VI) {
        viewModelScope.launch {
            intentQueue.emit(intent)
        }
    }

    override fun getViewData(): VD = viewData

    override fun updateViewData(viewDataBuilder: (VD) -> VD) {
        viewData = viewDataBuilder(viewData)
    }

    override fun getCurrentState(): S = _state.value.state

    override fun updateState(newState: S) {
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                checkIfIsModifierAllowed(newState)
                internalUpdateState(newState)
            }.onFailure {
                handleError("updateState", state::class.simpleName, it)
            }
        }
    }

    override fun sendAction(action: VA) {
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                checkIfIsModifierAllowed(action)
                _action.emit(ViewActionWrapper(action, this@MVIViewModelImpl))
            }.onFailure {
                handleError("sendAction", action::class.simpleName, it)
            }
        }
    }

    override fun getSavedStateHandle(): SavedStateHandle? = savedStateHandle

    override fun handleError(functionName: String, info: String?, error: Throwable) {
        val trackBack = TrackBack(featureName, functionName, info ?: error.message ?: "generic error")
        devlytics.logError(trackBack, error)
        if (BuildConfig.DEBUG) throw error
        else internalUpdateState(errorState)
    }

    private fun checkIfIsModifierAllowed(viewModifier: ViewModifier) {
        val isModifierAllowed = stateGuard.isModifierAllowed(_state.value.state, viewModifier)
        if (isModifierAllowed.not()) error("$viewModifier is not allowed after $state")
    }

    private fun internalUpdateState(newState: S) {
        _state.update { _ ->
            ViewStateWrapper(newState, viewData, this)
        }
    }
}
