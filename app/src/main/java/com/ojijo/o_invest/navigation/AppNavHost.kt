package com.ojijo.o_invest.navigation

import ContactScreen
import ProfileScreen
import RegisterScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ojijo.o_invest.data.UserDatabase
import com.ojijo.o_invest.repository.UserRepository
import com.ojijo.o_invest.ui.screens.about.AboutScreen
import com.ojijo.o_invest.ui.screens.dashboard.DashboardScreen
import com.ojijo.o_invest.ui.screens.form.FormScreen
import com.ojijo.o_invest.ui.screens.splash.SplashScreen
import com.ojijo.o_invest.ui.screens.start.StartScreen
import com.ojijo.o_invest.viewmodel.AuthViewModel
import com.ojijo.o_invest.ui.screens.auth.LoginScreen
import com.ojijo.o_invest.ui.screens.billsairtime.BillsAirtimeScreen
import com.ojijo.o_invest.ui.screens.cameracapture.CameraCaptureScreen
import com.ojijo.o_invest.ui.screens.home.HomeScreen
import com.ojijo.o_invest.ui.screens.invest.InvestScreen
import com.ojijo.o_invest.ui.screens.mpesapaybill.MpesaPaybillScreen
import com.ojijo.o_invest.ui.screens.reversal.ReversalScreen
import com.ojijo.o_invest.ui.screens.sendmoney.SendmoneyScreen
import com.ojijo.o_invest.ui.screens.service.ServiceScreen
import com.ojijo.o_invest.ui.screens.withdrawal.WithdrawScreen
import com.ojijo.o_invest.ui.screens.portfolio.PortfolioScreen
import com.ojijo.o_invest.ui.screens.setting.SettingsScreen


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
            composable(ROUT_SETTING) {
                SettingsScreen(navController)
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

            composable(ROUT_SERVICE) {
                ServiceScreen(navController)
            }
            composable(ROUT_INVEST) {
                InvestScreen(navController)
            }
            composable(ROUT_CONTACT) {
                ContactScreen(navController)
            }
            composable(ROUT_WITHDRAW) {
            WithdrawScreen(navController)
            }
            composable(ROUT_MPESAPAYBILL) {
            MpesaPaybillScreen(navController)
            }
            composable(ROUT_REVERSAL) {
            ReversalScreen(navController)
            }
            composable(ROUT_SENDMONEY) {
            SendmoneyScreen(navController)
            }
            composable(ROUT_BILLSAIRTIME) {
            BillsAirtimeScreen(navController)
            }
            composable(ROUT_CAMERACAPTURE) {
            CameraCaptureScreen(navController)
           }
        // Route without image URI
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUT_PORTFOLIO) {
            PortfolioScreen(navController)
        }


// Route with image URI
        composable(
            route = "$ROUT_PROFILE?faceImageUri={faceImageUri}",
            arguments = listOf(
                navArgument("faceImageUri") {
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val faceImageUri = backStackEntry.arguments?.getString("faceImageUri")
            ProfileScreen(navController, faceImageUri)
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



