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
import com.ojijo.o_invest.navigation.ROUT_BILLSAIRTIME
import com.ojijo.o_invest.navigation.ROUT_CONTACT
import com.ojijo.o_invest.navigation.ROUT_DASHBOARD
import com.ojijo.o_invest.navigation.ROUT_HOME
import com.ojijo.o_invest.navigation.ROUT_MPESAPAYBILL
import com.ojijo.o_invest.navigation.ROUT_PROFILE
import com.ojijo.o_invest.navigation.ROUT_REVERSAL
import com.ojijo.o_invest.navigation.ROUT_SENDMONEY
import com.ojijo.o_invest.navigation.ROUT_WITHDRAW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FB)) // Softer light background
            .verticalScroll(scrollState)
    ) {
        // Top Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "O-Invest Dashboard",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUT_CONTACT) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }

        // Welcome Card
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-40).dp)
                .fillMaxWidth()
                .height(210.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to O-Invest",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A0A0A)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Let your money work for you!",
                    fontSize = 16.sp,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(R.drawable.img_15),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Features Grid
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            val cardModifier = Modifier
                .size(130.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .padding(8.dp)

            val imageModifier = Modifier.size(60.dp)

            val textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212121))

            // First Row
            DashboardOptionsRow(
                navController,
                listOf(
                    DashboardOptionData("Send Money", R.drawable.img_27, ROUT_SENDMONEY),
                    DashboardOptionData("Mpesa Paybill", R.drawable.img_28, ROUT_MPESAPAYBILL)
                ),
                cardModifier, imageModifier, textStyle
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Second Row
            DashboardOptionsRow(
                navController,
                listOf(
                    DashboardOptionData("Reversal", R.drawable.img_29, ROUT_REVERSAL),
                    DashboardOptionData("Bills & Airtime", R.drawable.img_44, ROUT_BILLSAIRTIME)
                ),
                cardModifier, imageModifier, textStyle
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Third Row
            DashboardOptionsRow(
                navController,
                listOf(
                    DashboardOptionData("Top Up", R.drawable.img_43, ROUT_MPESAPAYBILL),
                    DashboardOptionData("Withdraw", R.drawable.img_35, ROUT_WITHDRAW)
                ),
                cardModifier, imageModifier, textStyle
            )
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

// Helper Row
data class DashboardOptionData(val label: String, val icon: Int, val route: String?)

@Composable
fun DashboardOptionsRow(
    navController: NavController,
    options: List<DashboardOptionData>,
    cardModifier: Modifier,
    imageModifier: Modifier,
    textStyle: TextStyle
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            DashboardOptionCard(
                icon = option.icon,
                label = option.label,
                modifier = cardModifier.clickable {
                    option.route?.let { navController.navigate(it) }
                },
                imageModifier = imageModifier,
                textStyle = textStyle
            )
        }
    }
}

// Option Card
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
        elevation = CardDefaults.cardElevation(5.dp)
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = label, style = textStyle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}
