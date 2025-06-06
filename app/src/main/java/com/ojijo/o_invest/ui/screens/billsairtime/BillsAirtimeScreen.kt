package com.ojijo.o_invest.ui.screens.billsairtime

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

// --- ROOM ENTITY ---
@Entity(tableName = "airtime_purchases")
data class AirtimePurchase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phoneNumber: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAO ---
@Dao
interface AirtimeDao {
    @Insert
    suspend fun insert(purchase: AirtimePurchase)

    @Query("SELECT * FROM airtime_purchases ORDER BY timestamp DESC")
    suspend fun getAll(): List<AirtimePurchase>
}

// --- DATABASE ---
@Database(entities = [AirtimePurchase::class], version = 1)
abstract class AirtimeDatabase : RoomDatabase() {
    abstract fun airtimeDao(): AirtimeDao

    companion object {
        @Volatile private var INSTANCE: AirtimeDatabase? = null

        fun getDatabase(context: android.content.Context): AirtimeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AirtimeDatabase::class.java,
                    "airtime_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL ---
class AirtimeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AirtimeDatabase.getDatabase(application).airtimeDao()

    fun buyAirtime(phone: String, amount: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                dao.insert(AirtimePurchase(phoneNumber = phone, amount = amount))
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}

// --- COMPOSABLE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillsAirtimeScreen(
    navController: NavController,
    viewModel: AirtimeViewModel = viewModel()
) {
    val context = LocalContext.current

    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buy Airtime") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("e.g. 0712345678") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (KSh)") },
                    placeholder = { Text("e.g. 100") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (phoneNumber.isNotBlank() && amount.isNotBlank()) {
                            viewModel.buyAirtime(phoneNumber, amount) { success ->
                                if (success) {
                                    showDialog = true
                                    phoneNumber = ""
                                    amount = ""
                                } else {
                                    Toast.makeText(context, "Error processing request.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buy Airtime")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Transaction Successful") },
                        text = {
                            Text("Airtime worth KSh $amount has been sent.")
                        },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BillsAirtimeScreenPreview() {
    BillsAirtimeScreen(rememberNavController())
}
