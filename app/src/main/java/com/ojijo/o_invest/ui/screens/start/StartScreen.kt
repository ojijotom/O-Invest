package com.ojijo.o_invest.ui.screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.navigation.ROUT_FORM
import com.ojijo.o_invest.navigation.ROUT_REGISTER
import com.ojijo.o_invest.navigation.ROUT_SPLASH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    var personaldetails by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountdetails by remember { mutableStateOf("") }
    var modeofinvestment by remember { mutableStateOf("") }
    var transactionpin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUT_SPLASH) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A),
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF4F6F8))
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Page Heading
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Create your account in",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "5 Easy Steps",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form inside card
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Personal Details (Navigates to another form)
                    StyledTextField(
                        value = personaldetails,
                        onValueChange = { personaldetails = it },
                        label = "Enter personal details",
                        navTarget = ROUT_REGISTER,
                        navController = navController
                    )

                    StyledTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Create a password",
                        isPassword = true,
                        navTarget = ROUT_REGISTER,
                        navController = navController
                    )

                    StyledTextField(
                        value = accountdetails,
                        onValueChange = { accountdetails = it },
                        label = "Enter account details",
                        navTarget = ROUT_REGISTER,
                        navController = navController
                    )

                    StyledTextField(
                        value = modeofinvestment,
                        onValueChange = { modeofinvestment = it },
                        label = "Select mode of investment",
                        navTarget = ROUT_REGISTER,
                        navController = navController
                    )

                    StyledTextField(
                        value = transactionpin,
                        onValueChange = { transactionpin = it },
                        label = "Set up transaction pin",
                        navTarget = ROUT_REGISTER,
                        navController = navController
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Submit Button
            Button(
                onClick = { navController.navigate(ROUT_REGISTER)  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
            ) {
                Text("Let's do this!", color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    navTarget: String? = null,
    navController: NavController? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = navTarget != null) {
                navTarget?.let { navController?.navigate(it) }
            }
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF1E3A8A))
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
            ),
            enabled = false, // Disable typing to make it a navigation field
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color(0xFF1E3A8A),
                disabledTextColor = Color.Black,
                disabledLabelColor = Color(0xFF1E3A8A),
                disabledLeadingIconColor = Color(0xFF1E3A8A)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}
