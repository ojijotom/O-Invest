package com.ojijo.o_invest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.data.UserDatabase
import com.ojijo.o_invest.repository.UserRepository
import com.ojijo.o_invest.ui.screens.about.AboutScreen
import com.ojijo.o_invest.ui.screens.dashboard.DashboardScreen
import com.ojijo.o_invest.ui.screens.form.FormScreen
import com.ojijo.o_invest.ui.screens.home.HomeScreen
import com.ojijo.o_invest.ui.screens.intent.IntentScreen
import com.ojijo.o_invest.ui.screens.item.ItemScreen
import com.ojijo.o_invest.ui.screens.splash.SplashScreen
import com.ojijo.o_invest.ui.screens.start.StartScreen
import com.ojijo.o_invest.viewmodel.AuthViewModel
import com.ojijo.o_invest.ui.screens.auth.LoginScreen
import com.ojijo.o_invest.ui.screens.auth.RegisterScreen
import com.ojijo.o_invest.ui.screens.service.ServiceScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    val context = LocalContext.current


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
            composable(ROUT_HOME) {
                HomeScreen(navController)
            }
            composable(ROUT_ABOUT) {
                AboutScreen(navController)
            }
            composable(ROUT_START) {
                StartScreen(navController)
            }
            composable(ROUT_INTENT) {
                IntentScreen(navController)
            }
            composable(ROUT_DASHBOARD) {
                DashboardScreen(navController)
            }
            composable(ROUT_SPLASH) {
                SplashScreen(navController)
            }
            composable(ROUT_FORM) {
                FormScreen(navController)
            }
            composable(ROUT_ITEM) {
                ItemScreen(navController)
            }
            composable(ROUT_SERVICE) {
                ServiceScreen(navController)
            }



        //AUTHENTICATION

        // Initialize Room Database and Repository for Authentication
        val appDatabase = UserDatabase.getDatabase(context)
        val authRepository = UserRepository(appDatabase.userDao())
        val authViewModel: AuthViewModel = AuthViewModel(authRepository)
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }

    }
}