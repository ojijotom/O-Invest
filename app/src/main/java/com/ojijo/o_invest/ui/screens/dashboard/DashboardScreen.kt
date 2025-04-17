package com.ojijo.o_invest.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.R
import com.ojijo.o_invest.navigation.ROUT_ABOUT
import com.ojijo.o_invest.navigation.ROUT_HOME
import com.ojijo.o_invest.navigation.ROUT_ITEM
import com.ojijo.o_invest.ui.theme.newWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF0F5)) // light clean background
            .verticalScroll(scrollState)
    ) {
        // Top Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }

        // Welcome Card
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-40).dp)
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Welcome to O-Invest",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A0A0A)
                )
                Text(
                    text = "Let your money work for you!",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Image(
                    painter = painterResource(R.drawable.img_15),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Features Grid
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            val cardModifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(8.dp)

            val imageModifier = Modifier.size(70.dp)

            val textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

            // First Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardOptionCard(
                    icon = R.drawable.img_27,
                    label = "Send Money",
                    modifier = cardModifier.clickable { navController.navigate(ROUT_HOME) },
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
                DashboardOptionCard(
                    icon = R.drawable.img_28,
                    label = "Mpesa Paybill",
                    modifier = cardModifier.clickable { navController.navigate(ROUT_ABOUT) },
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Second Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardOptionCard(
                    icon = R.drawable.img_29,
                    label = "Reversal",
                    modifier = cardModifier,
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
                DashboardOptionCard(
                    icon = R.drawable.img_44,
                    label = "Bills & Airtime",
                    modifier = cardModifier.clickable { navController.navigate(ROUT_ITEM) },
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Third Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardOptionCard(
                    icon = R.drawable.img_43,
                    label = "Top Up",
                    modifier = cardModifier,
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
                DashboardOptionCard(
                    icon = R.drawable.img_35,
                    label = "Withdraw",
                    modifier = cardModifier.clickable { navController.navigate(ROUT_ITEM) },
                    imageModifier = imageModifier,
                    textStyle = textStyle
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun DashboardOptionCard(
    icon: Int,
    label: String,
    modifier: Modifier,
    imageModifier: Modifier,
    textStyle: TextStyle
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = imageModifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = textStyle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}
