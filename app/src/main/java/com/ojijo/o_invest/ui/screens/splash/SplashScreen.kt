package com.ojijo.o_invest.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.R
import com.ojijo.o_invest.navigation.ROUT_REGISTER
import com.ojijo.o_invest.navigation.ROUT_START
import kotlinx.coroutines.delay

data class CarouselItem(val title: String, val description: String)

@Composable
fun SplashScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    val carouselItems = listOf(
        CarouselItem("Investment Plan 1", "Diversified portfolio for stable growth."),
        CarouselItem("Investment Plan 2", "Aggressive growth with high risk."),
        CarouselItem("Investment Plan 3", "Moderate growth with lower risk.")
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { carouselItems.size }
    )

    // Auto-slide carousel
    LaunchedEffect(pagerState.currentPage) {
        delay(1500)
        val nextPage = (pagerState.currentPage + 1) % carouselItems.size
        pagerState.animateScrollToPage(nextPage)
    }

    // Splash screen timing
    LaunchedEffect(Unit) {
        visible = true
        delay(3500)
        navController.navigate(ROUT_START) {
            popUpTo(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFF0D47A1), Color(0xFF1976D2))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = carouselItems[page].title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = carouselItems[page].description,
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Animated Logo with Zoom-In Effect
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1500))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape)
                                .padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Stylish App Name
                    Text(
                        text = "O-Invest",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 3.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stylish Circular Progress Indicator
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
