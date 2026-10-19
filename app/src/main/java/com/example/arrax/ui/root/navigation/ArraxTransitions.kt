// ui/root/navigation/ArraxTransitions.kt
package com.example.arrax.ui.root.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavBackStackEntry

private const val DURATION = 300

typealias EnterSpec = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
typealias ExitSpec = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition

object ArraxTransitions {
    val enter: EnterSpec = {
        fadeIn(tween(210, delayMillis = 90)) +
                slideInHorizontally(tween(300)) { it / 10 }
    }
    val exit: ExitSpec = { fadeOut(tween(90)) }

    val popEnter: EnterSpec = {
        fadeIn(tween(210, delayMillis = 90)) +
                slideInHorizontally(tween(300)) { -it / 10 }
    }
    val popExit: ExitSpec = { fadeOut(tween(90)) }
}