package com.example.androidmviharch.mvih.viewlayer

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.androidmviharch.logs.Devlytics
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewStateWrapper
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewActionWrapper
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewBindingState
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Funções de extensão para renderizar estados em Activities e Fragments
 */

fun <S : ViewBindingState<VD, VB>, VD : ViewData, VB : ViewBinding> AppCompatActivity.renderState(
    state: StateFlow<ViewStateWrapper<S, VD>>,
    viewBinding: VB
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.collect {
                runCatching {
                    (it.state).render(it.viewData, viewBinding, this@renderState)
                }.onFailure { error ->
                    it.errorHandler.handleError("renderState", it.state::class.simpleName, error)
                }
            }
        }
    }
}

fun <S : ViewState<VD>, VD : ViewData> AppCompatActivity.renderState(
    state: StateFlow<ViewStateWrapper<S, VD>>,
    view: View,
    devlytics: Devlytics? = null,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.collect {
                runCatching {
                    (it.state).render(it.viewData, view, this@renderState)
                }.onFailure { error ->
                    it.errorHandler.handleError("renderState", it.state::class.simpleName, error)
                }
            }
        }
    }
}

fun <S : ViewBindingState<VD, VB>, VD : ViewData, VB : ViewBinding> Fragment.renderState(
    state: StateFlow<ViewStateWrapper<S, VD>>,
    viewBinding: VB,
    devlytics: Devlytics? = null,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.collect {
                runCatching {
                    (it.state).render(
                        it.viewData,
                        viewBinding,
                        this@renderState.requireContext()
                    )
                }.onFailure { error ->
                    it.errorHandler.handleError("renderState", it.state::class.simpleName, error)
                }
            }
        }
    }
}

fun <S : ViewState<VD>, VD : ViewData> Fragment.renderState(
    state: StateFlow<ViewStateWrapper<S, VD>>,
    view: View,
    devlytics: Devlytics? = null,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.collect {
                runCatching {
                    (it.state).render(it.viewData, view, this@renderState.requireContext())
                }.onFailure { error ->
                    it.errorHandler.handleError("renderState", it.state::class.simpleName, error)
                }
            }
        }
    }
}

/**
 * Funções de extensão para observar ações em Activities e Fragments
 */

fun <VA : ViewAction> AppCompatActivity.observeAction(
    action: SharedFlow<ViewActionWrapper<VA>>,
    devlytics: Devlytics? = null,
    block: (VA) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.collect {
                runCatching {
                    block(it.action)
                }.onFailure { error ->
                    it.errorHandler.handleError("observeAction", it.action::class.simpleName, error)
                }
            }
        }
    }
}

fun <VA : ViewAction> Fragment.observeAction(
    action: SharedFlow<ViewActionWrapper<VA>>,
    devlytics: Devlytics? = null,
    block: (VA) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.collect {
                runCatching {
                    block(it.action)
                }.onFailure { error ->
                    it.errorHandler.handleError("observeAction", it.action::class.simpleName, error)
                }
            }
        }
    }
}
