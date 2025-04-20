package com.ojijo.o_invest.ui.screens.invest

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestScreen(navController: NavController) {
    val context = LocalContext.current

    var investmentBalance by rememberSaveable { mutableStateOf(50000.0) } // starting balance
    var depositAmount by rememberSaveable { mutableStateOf("") }

    val recentTransactions = remember {
        mutableStateListOf(
            "Deposit: KES 5,000",
            "Invested: KES 10,000 in Bonds",
            "Deposit: KES 15,000"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Investment Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Current Balance",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "KES ${investmentBalance.toString()}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = depositAmount,
                    onValueChange = { depositAmount = it },
                    label = { Text("Deposit Amount (KES)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amount = depositAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            investmentBalance += amount
                            recentTransactions.add(0, "Deposit: KES ${amount.toInt()}")
                            depositAmount = ""
                            Toast.makeText(context, "Deposit Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = depositAmount.isNotBlank()
                ) {
                    Text("Deposit to Investment Account")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(recentTransactions) { transaction ->
                        Text(
                            text = transaction,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Divider()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun InvestScreenPreview() {
    InvestScreen(rememberNavController())
}
