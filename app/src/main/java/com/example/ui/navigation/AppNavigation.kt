package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.PostDetailScreen
import com.example.ui.screens.PostFormScreen
import com.example.ui.screens.PostListScreen
import com.example.ui.screens.UserListScreen
import com.example.ui.viewmodel.PostViewModel

object AppDestinations {
    const val POST_LIST = "posts"
    const val POST_DETAIL = "posts/{postId}"
    const val POST_FORM = "post_form?postId={postId}"
    const val USER_LIST = "users"

    fun postDetailRoute(postId: Int) = "posts/$postId"
    fun postFormRoute(postId: Int = 0) = "post_form?postId=$postId"
}

private fun NavHostController.popIfCurrent(route: String) {
    if (currentBackStackEntry?.destination?.route == route && previousBackStackEntry != null) {
        popBackStack()
    }
}

@Composable
fun AppNavigation(
    viewModel: PostViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.POST_LIST,
        modifier = modifier
    ) {
        composable(route = AppDestinations.POST_LIST) {
            PostListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { postId ->
                    navController.navigate(AppDestinations.postDetailRoute(postId))
                },
                onNavigateToCreate = {
                    navController.navigate(AppDestinations.postFormRoute(0))
                },
                onNavigateToEdit = { postId ->
                    navController.navigate(AppDestinations.postFormRoute(postId))
                },
                onNavigateToUsers = {
                    navController.navigate(AppDestinations.USER_LIST) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppDestinations.POST_DETAIL,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            PostDetailScreen(
                postId = postId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popIfCurrent(AppDestinations.POST_DETAIL)
                },
                onNavigateToEdit = { id ->
                    navController.navigate(AppDestinations.postFormRoute(id))
                }
            )
        }

        composable(
            route = AppDestinations.POST_FORM,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            PostFormScreen(
                postId = postId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popIfCurrent(AppDestinations.POST_FORM)
                }
            )
        }

        composable(route = AppDestinations.USER_LIST) {
            UserListScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popIfCurrent(AppDestinations.USER_LIST)
                }
            )
        }
    }
}
