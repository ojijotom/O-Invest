import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ojijo.o_invest.model.User
import com.ojijo.o_invest.navigation.ROUT_LOGIN
import com.ojijo.o_invest.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    onRegisterSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var investorType by remember { mutableStateOf("Retail Investor") }
    val roleOptions = listOf(
        "Retail Investor",
        "Institutional Investor",
        "Angel Investor",
        "Venture Capitalist",
        "Private Equity Investor",
        "Hedge Fund Investor",
        "Mutual Fund Manager",
        "Sovereign Wealth Fund",
        "Family Office",
        "Other"
    )
    var expanded by remember { mutableStateOf(false) }

    // ✅ Privacy/Terms acceptance
    var acceptedTerms by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
    )

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White.copy(alpha = 0.1f),
        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
        cursorColor = Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Create Account",
            fontSize = 36.sp,
            fontFamily = FontFamily.Cursive,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            colors = textFieldColors,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))



        // Investor Type dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = investorType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Investor Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = textFieldColors,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roleOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            investorType = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password", tint = Color.White) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Confirm Password field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password", tint = Color.White) },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ✅ Terms and Privacy Checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = acceptedTerms, onCheckedChange = { acceptedTerms = it })
            Text(text = "I agree to the ", color = Color.White)
            Text(
                text = "Privacy Policy",
                color = Color.Cyan,
                modifier = Modifier.clickable { showPrivacyDialog = true }
            )
            Text(text = " and ", color = Color.White)
            Text(
                text = "Terms & Conditions",
                color = Color.Cyan,
                modifier = Modifier.clickable { showTermsDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Register Button
        Button(
            onClick = {
                if (!acceptedTerms) {
                    Toast.makeText(context, "Please accept the Privacy Policy and Terms", Toast.LENGTH_SHORT).show()
                } else if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.registerUser(User(username = username, email = email, role = investorType, password = password))
                    onRegisterSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0072FF)
            ),
            shape = MaterialTheme.shapes.medium,
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Text("Register", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        TextButton(onClick = { navController.navigate(ROUT_LOGIN) }) {
            Text("Already have an account? Login", color = Color.White)
        }

        // ✅ Dialogs for Privacy and Terms
        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                confirmButton = {
                    TextButton(onClick = { showPrivacyDialog = false }) {
                        Text("Close")
                    }
                },
                title = { Text("Privacy Policy", fontSize = 10.sp) },
                text = {
                    Text("O-Invest respects your privacy. We do not sell or share your data. All personal and biometric information is encrypted and used solely for account protection.")
                }
            )
        }

        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text("Close")
                    }
                },
                title = { Text("Terms and Conditions", fontSize = 10.sp) },
                text = {
                    Text("By registering, you agree that O-Invest is not responsible for investment losses. We provide tools for investment; decisions are yours. Face scan features are secure and optional.")
                }
            )
        }
    }
}
