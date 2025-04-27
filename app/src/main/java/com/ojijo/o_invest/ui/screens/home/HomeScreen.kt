package com.ojijo.o_invest.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.navigation.ROUT_DASHBOARD
import com.ojijo.o_invest.navigation.ROUT_INVEST
import com.ojijo.o_invest.navigation.ROUT_PORTFOLIO
import com.ojijo.o_invest.navigation.ROUT_SETTING
import com.ojijo.o_invest.navigation.ROUT_HOME
import com.ojijo.o_invest.navigation.ROUT_PROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedIndex = selectedIndex,
                onItemSelected = { index -> selectedIndex = index }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {
                // Header Section
                HeaderSection()

                // Investment Stats Section
                Spacer(modifier = Modifier.height(16.dp))
                InvestmentStatsSection()

                // Quick Access Section
                Spacer(modifier = Modifier.height(24.dp))
                QuickAccessSection(navController)

                // Recent Activities Section
                Spacer(modifier = Modifier.height(24.dp))
                RecentActivitiesSection()
            }
        }
    )
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
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        // App logo can be added here
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
                InvestmentStatItem("Total Investment", "KSh 0.00", Icons.Default.AttachMoney)
                InvestmentStatItem("ROI", "0%", Icons.Default.TrendingFlat)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                InvestmentStatItem("Active Projects", "None", Icons.Default.Work)
                InvestmentStatItem("Net Worth", "Not available", Icons.Default.AccountBalance)
            }
        }
    }
}

@Composable
fun InvestmentStatItem(title: String, value: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
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
        QuickAccessItem("Invest Now", Icons.Default.Add, onClick = { navController.navigate(ROUT_INVEST) })
        QuickAccessItem("My Portfolio", Icons.Default.PieChart, onClick = { navController.navigate(ROUT_PORTFOLIO) })
        QuickAccessItem("Settings", Icons.Default.Settings, onClick = { navController.navigate(ROUT_SETTING) })
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
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(40.dp), tint = Color.Blue)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun RecentActivitiesSection() {
    Text(text = "Recent Activities", fontSize = 18.sp, color = Color.Black)
    val activities = listOf(
        "Start Investing now to grow your Net worth",
        "You haven't made any investment",
        "Explore investment opportunities"
    )
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

@Composable
fun BottomNavigationBar(navController: NavController, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color(0xFF1976D2)) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = {
                onItemSelected(0)
                navController.navigate(ROUT_HOME)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") }, // Changed icon here
            label = { Text("Dashboard") }, // Changed label here
            selected = selectedIndex == 1,
            onClick = {
                onItemSelected(1)
                navController.navigate(ROUT_DASHBOARD) // Adjust routing as needed
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedIndex == 2,
            onClick = {
                onItemSelected(2)
                navController.navigate(ROUT_PROFILE) // Adjust routing as needed
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Payments, contentDescription = "Invest") },
            label = { Text("Invest") },
            selected = selectedIndex == 3,
            onClick = {
                onItemSelected(3)
                navController.navigate(ROUT_INVEST)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
