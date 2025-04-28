package com.ojijo.o_invest.ui.screens.sendmoney

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.launch
import java.util.*

// --- ENTITY ---
@Entity(tableName = "send_money_transactions")
data class SendMoneyTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phoneNumber: String,
    val amount: String,
    val txRef: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAO ---
@Dao
interface SendMoneyDao {
    @Insert
    suspend fun insert(transaction: SendMoneyTransaction)

    @Query("SELECT * FROM send_money_transactions ORDER BY timestamp DESC")
    suspend fun getAll(): List<SendMoneyTransaction>
}

// --- DATABASE ---
@Database(entities = [SendMoneyTransaction::class], version = 1)
abstract class SendMoneyDatabase : RoomDatabase() {
    abstract fun sendMoneyDao(): SendMoneyDao

    companion object {
        @Volatile private var INSTANCE: SendMoneyDatabase? = null

        fun getDatabase(context: Context): SendMoneyDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SendMoneyDatabase::class.java,
                    "send_money_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL ---
class SendMoneyViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = SendMoneyDatabase.getDatabase(application).sendMoneyDao()

    fun sendMoney(phone: String, amount: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val txRef = UUID.randomUUID().toString()

            try {
                // Save to local DB
                dao.insert(SendMoneyTransaction(phoneNumber = phone, amount = amount, txRef = txRef))

                // Simulate Flutterwave transaction
                val response = simulateFlutterwavePayment(phone, amount, txRef)

                if (response) {
                    onResult(true, "Payment sent successfully!\nTxRef: $txRef")
                } else {
                    onResult(false, "Flutterwave transaction failed")
                }
            } catch (e: Exception) {
                onResult(false, "Database error: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun simulateFlutterwavePayment(phone: String, amount: String, txRef: String): Boolean {
        // Placeholder: In real app, make a network request here
        return phone.isNotBlank() && amount.isNotBlank()
    }
}

// --- COMPOSABLE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendmoneyScreen(navController: NavController, viewModel: SendMoneyViewModel = viewModel()) {
    val context = LocalContext.current

    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Send Money") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (KES)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (phoneNumber.isNotBlank() && amount.isNotBlank()) {
                            viewModel.sendMoney(phoneNumber, amount) { success, result ->
                                message = result
                                if (success) {
                                    phoneNumber = ""
                                    amount = ""
                                }
                            }
                        } else {
                            message = "Please fill in all fields"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = phoneNumber.isNotBlank() && amount.isNotBlank()
                ) {
                    Text("Send Money")
                }

                message?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SendmoneyPreview() {
    SendmoneyScreen(rememberNavController())
}
