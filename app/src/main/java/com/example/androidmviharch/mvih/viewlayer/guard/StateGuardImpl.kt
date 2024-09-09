package com.example.androidmviharch.mvih.viewlayer.guard

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewModifier
import kotlin.reflect.KClass

/**
 * Implementação de [StateGuard]
 * @param statesAllowed Mapa de [State] e [ViewModifier] permitidos
 */
class StateGuardImpl internal constructor(
    private val statesAllowed: Map<KClass<out State>, Set<KClass<out ViewModifier>>>
) : StateGuard {
    override fun isModifierAllowed(currentState: State, viewModifier: ViewModifier): Boolean {
        return statesAllowed[currentState::class]?.firstOrNull { it == viewModifier::class } != null
    }
}
