package com.treeleaf.quiz.navigators

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberCustomAnimatedNavController(vararg navigators: Navigator<out NavDestination>): NavHostController =
    rememberNavController(*navigators).apply {
        navigatorProvider += remember(this) { AnimatedComposeNavigator() }
    }

const val NavigationDuration = 500

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.customAnimatedComposable(
    route: String,
    overridingDuration: Int? = null,
    enterTransitionDirection: AnimatedContentScope.SlideDirection = AnimatedContentScope.SlideDirection.Left,
    exitTransitionDirection: AnimatedContentScope.SlideDirection = AnimatedContentScope.SlideDirection.Left,
    popEnterTransitionDirection: AnimatedContentScope.SlideDirection = AnimatedContentScope.SlideDirection.Right,
    popExitTransitionDirection: AnimatedContentScope.SlideDirection = AnimatedContentScope.SlideDirection.Right,
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(route, enterTransition = {
    slideIntoContainer(
        enterTransitionDirection,
        animationSpec = tween(overridingDuration?: NavigationDuration)
    )
    /*when (initialState.destination.route) {
        Screen.Home.Tutorial.route ->
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        else -> null
    }*/
},
    exitTransition = {
        slideOutOfContainer(
            exitTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (targetState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    },
    popEnterTransition = {
        slideIntoContainer(
            popEnterTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (initialState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    },
    popExitTransition = {
        slideOutOfContainer(
            popExitTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (targetState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    }) {
    content(it)
}

fun NavController.navigator(route: String, clearBackStack: Boolean = false) {
    if (clearBackStack)
        this.backQueue.clear()
    try {
        navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            /*popUpTo(this@navigator.graph.startDestinationId) {
            saveState = true
        }*/

            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    } catch (ex: java.lang.NullPointerException){
        ex.printStackTrace()
    }
}