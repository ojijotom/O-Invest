package com.ojijo.o_invest.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.R
import com.ojijo.o_invest.navigation.ROUT_INVEST
import com.ojijo.o_invest.navigation.ROUT_PORTFOLIO

@Composable
fun HomeScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))
        InvestmentStatsSection()
        Spacer(modifier = Modifier.height(24.dp))
        QuickAccessSection(navController)
        Spacer(modifier = Modifier.height(24.dp))
        RecentActivitiesSection()
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Welcome to O-Invest",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.White
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun InvestmentStatsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Investment Overview", fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                InvestmentStatItem("Total Investment", "$15,000", Icons.Default.AttachMoney)
                InvestmentStatItem("ROI", "5.6%", Icons.Default.TrendingUp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                InvestmentStatItem("Active Projects", "5", Icons.Default.Work)
                InvestmentStatItem("Net Worth", "$500,000", Icons.Default.AccountBalance)
            }
        }
    }
}

@Composable
fun InvestmentStatItem(title: String, value: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp) // No weight needed if using SpaceBetween
    ) {
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(32.dp), tint = Color(0xFF0072FF))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, color = Color.Black)
        Text(text = title, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun QuickAccessSection(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        QuickAccessItem("Invest Now", Icons.Default.Add, onClick = { navController.navigate(
            ROUT_INVEST
        ) })
        QuickAccessItem("My Portfolio", Icons.Default.PieChart, onClick = { navController.navigate(
            ROUT_PORTFOLIO
        ) })
        QuickAccessItem("Settings", Icons.Default.Settings, onClick = { navController.navigate("settings_screen") })
    }
}

@Composable
fun QuickAccessItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(40.dp), tint = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun RecentActivitiesSection() {
    Text(text = "Recent Activities", fontSize = 18.sp, color = Color.White)
    val activities = listOf("Invested $1,000 in Project A", "Sold 50 shares of XYZ", "Earned $200 in dividends")
    Spacer(modifier = Modifier.height(8.dp))
    Column {
        activities.forEach { activity ->
            RecentActivityItem(activity)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RecentActivityItem(activity: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.History, contentDescription = "Activity", tint = Color(0xFF0072FF))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = activity, color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    // Create a dummy NavController for preview purposes
    val navController = rememberNavController()

    HomeScreen(navController = navController)
}