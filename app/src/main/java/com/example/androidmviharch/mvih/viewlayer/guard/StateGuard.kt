package com.example.androidmviharch.mvih.viewlayer.guard

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewModifier

/**
 * Interface responsável por validar se determinado [ViewModifier] é permitido de acordo com [State] atual
 */
interface StateGuard {
    fun isModifierAllowed(currentState: State, viewModifier: ViewModifier): Boolean
}
