package com.ojijo.o_invest.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.navigation.ROUT_HOME
import com.ojijo.o_invest.navigation.ROUT_PROFILE

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            color = Color(0xFF0072FF),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        SettingsItem(title = "Profile", icon = Icons.Default.Person) {navController.navigate(ROUT_PROFILE)
            // TODO: Navigate to Profile screen
        }

        SettingsItem(title = "Notifications", icon = Icons.Default.Notifications) {
            // TODO: Notification preferences
        }

        SettingsItem(title = "Theme", icon = Icons.Default.DarkMode) {
            // TODO: Theme toggle
        }

        SettingsItem(title = "Security", icon = Icons.Default.Lock) {
            // TODO: Security settings
        }

        Spacer(modifier = Modifier.height(32.dp))

        SettingsItem(title = "Logout", icon = Icons.Default.ExitToApp, isLogout = true) {
            // TODO: Handle logout
            navController.navigate(ROUT_HOME) {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, isLogout: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isLogout) Color.Red else Color(0xFF0072FF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = if (isLogout) Color.Red else Color.Black
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    SettingsScreen(navController)
}
