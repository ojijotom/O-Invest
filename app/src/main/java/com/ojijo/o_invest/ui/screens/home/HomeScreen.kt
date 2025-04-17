package com.ojijo.o_invest.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.handwriting.handwritingHandler
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.R
import com.ojijo.o_invest.navigation.ROUT_ABOUT
import com.ojijo.o_invest.navigation.ROUT_HOME
import com.ojijo.o_invest.navigation.ROUT_INTENT
import com.ojijo.o_invest.navigation.ROUT_ITEM
import com.ojijo.o_invest.navigation.ROUT_LOGIN
import com.ojijo.o_invest.navigation.ROUT_START
import com.ojijo.o_invest.ui.theme.newOrange
import com.ojijo.o_invest.ui.theme.newWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Action") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar(containerColor = Color(0xFFE0E0E0)) {
                listOf("Home", "Favorites", "Profile").forEachIndexed { index, label ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.Favorite
                                    else -> Icons.Default.Person
                                },
                                contentDescription = label
                            )
                        },
                        label = { Text(label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .verticalScroll(rememberScrollState())
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2196F3))
                ) {
                    // Top header card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {}

                    // Overlapping content card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .offset(y = 90.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Welcome to O-Invest",
                                fontFamily = FontFamily.Cursive,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Let your money work for you!",
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Image(
                                painter = painterResource(R.drawable.img_15),
                                contentDescription = "App Banner",
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(130.dp))

                // Action Grid (with two cards per row)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                ) {
                    ActionRow(
                        navController,
                        listOf(
                            Pair("Send Money" to R.drawable.img_27) to ROUT_HOME,
                            Pair("Mpesa Paybill" to R.drawable.img_28) to ROUT_ABOUT
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ActionRow(
                        navController,
                        listOf(
                            Pair("Reversal" to R.drawable.img_29) to "",
                            Pair("Bills & Airtime" to R.drawable.img_44) to ROUT_ITEM
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ActionRow(
                        navController,
                        listOf(
                            Pair("Top Up" to R.drawable.img_43) to "",
                            Pair("Withdraw" to R.drawable.img_35) to ROUT_ITEM
                        )
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    )
}

// Reusable composable for row of 2 cards
@Composable
fun ActionRow(
    navController: NavController,
    items: List<Pair<Pair<String, Int>, String>>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { (item, route) ->
            ActionCard(
                title = item.first,
                iconRes = item.second,
                onClick = {
                    if (route.isNotEmpty()) {
                        navController.navigate(route)
                    }
                }
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
