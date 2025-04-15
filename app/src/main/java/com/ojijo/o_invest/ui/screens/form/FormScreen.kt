package com.ojijo.o_invest.ui.screens.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ojijo.o_invest.R
import com.ojijo.o_invest.navigation.ROUT_LOGIN

@Composable
fun FormScreen(navController: NavController) {
    var fullname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Makes it scrollable
            .background(Color(0xFFF6F6F6)), // Light background
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Circular image with border
        Image(
            painter = painterResource(R.drawable.img_10),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Blue, CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CREATE AN ACCOUNT",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Form Card
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                CustomTextField(value = fullname, onValueChange = { fullname = it }, label = "Fullname", icon = Icons.Default.Person)
                CustomTextField(value = username, onValueChange = { username = it }, label = "Username", icon = Icons.Default.Face)
                CustomTextField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email, type = KeyboardType.Email)
                CustomTextField(value = tel, onValueChange = { tel = it }, label = "Phone", icon = Icons.Default.Phone, type = KeyboardType.Phone)
                CustomTextField(value = password, onValueChange = { password = it }, label = "Password", icon = Icons.Default.Lock, type = KeyboardType.Password, isPassword = true)
                CustomTextField(value = confirmpassword, onValueChange = { confirmpassword = it }, label = "Confirm Password", icon = Icons.Default.Lock, type = KeyboardType.Password, isPassword = true)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate(ROUT_LOGIN) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
                ) {
                    Text("Register", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    type: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1E3A8A))
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFF1E3A8A),
            focusedBorderColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(keyboardType = type),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    FormScreen(navController = rememberNavController())
}
