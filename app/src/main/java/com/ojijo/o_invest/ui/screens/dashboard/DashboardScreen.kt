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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
fun DashboardScreen(navController: NavController){

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .verticalScroll(rememberScrollState())
    ){
        Box (){
            //Card
            Card (
                modifier = Modifier.fillMaxWidth().height(300.dp),
                shape = RoundedCornerShape(bottomStart =60.dp, bottomEnd = 60.dp ),
                colors = CardDefaults.cardColors(newWhite)
            ){
                TopAppBar(
                    title = { Text(text = "Dashboard Section", color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
                        }
                    },

                    )


            }
            //End of Card

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(alignment = Alignment.BottomEnd)
                    .padding(start = 20.dp, end = 20.dp)
                    .offset(y = 90.dp)


            ){
                Column (
                    modifier = Modifier.fillMaxSize().handwritingHandler(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center


                ){
                    Text(
                        text = "Welcome to O-Invest",
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.Black

                    )
                    Text(
                        text = "Let your money work for you!",
                        fontSize = 16.sp,
                        color = Color.Black




                    )


                    Image(
                        painter = painterResource(R.drawable.img_15),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)

                    )

                }


            }



        }
        //End of Box

        Spacer(modifier = Modifier.height(100.dp))

        //Row

        Row (
            modifier = Modifier.padding(start = 20.dp)){

            //Card1
            Card (
                modifier = Modifier
                    .width(150.dp)
                    .height(180.dp)
                    .clickable{navController.navigate(ROUT_HOME)}
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(100.dp)

                    )
                    Text(text = "Home", fontSize = 15.sp)
                }
            }

            //End of Card1

            Spacer(modifier = Modifier.width(20.dp))



            //Card2
            Card (
                modifier = Modifier
                    .width(150.dp)
                    .height(180.dp)
                    .clickable{navController.navigate(ROUT_ABOUT)}
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.about),
                        contentDescription = "About",
                        modifier = Modifier.size(100.dp)

                    )
                    Text(text = "About", fontSize = 15.sp)
                }
            }

            //End of Card2

        }



        //End of Row3

        Spacer(modifier = Modifier.height(30.dp))

        //Row2

        Row (
            modifier = Modifier.padding(start = 20.dp)){


            //Card1
            Card (
                modifier = Modifier.width(150.dp).height(180.dp)
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.contact),
                        contentDescription = "Contact",
                        modifier = Modifier.size(100.dp)

                    )
                    Text(text = "Contact", fontSize = 15.sp)
                }
            }

            //End of Card3

            Spacer(modifier = Modifier.width(20.dp))



            //Card4
            Card (
                modifier = Modifier
                    .width(150.dp)
                    .height(180.dp)
                    .clickable{navController.navigate(ROUT_ITEM)}
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.img_2),
                        contentDescription = "Products",
                        modifier = Modifier.size(100.dp)

                    )
                    Text(text = "Products" +
                            "", fontSize = 15.sp)
                }
            }

            //End of Card4


        }

        //End of Row2

        Spacer(modifier = Modifier.height(30.dp))

    }

}
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {

    DashboardScreen(rememberNavController())

}