package com.ojijo.o_invest.ui.screens.topup

import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.Application
import android.widget.Toast
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.launch

// --- ENTITY ---
@Entity(tableName = "topup_requests")
data class TopUp(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountNumber: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAO ---
@Dao
interface TopUpDao {
    @Insert
    suspend fun insert(topUp: TopUp)

    @Query("SELECT * FROM topup_requests ORDER BY timestamp DESC")
    suspend fun getAll(): List<TopUp>
}

// --- DATABASE ---
@Database(entities = [TopUp::class], version = 1)
abstract class TopUpDatabase : RoomDatabase() {
    abstract fun topUpDao(): TopUpDao

    companion object {
        @Volatile private var INSTANCE: TopUpDatabase? = null

        fun getDatabase(context: android.content.Context): TopUpDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TopUpDatabase::class.java,
                    "topup_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL ---
class TopUpViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TopUpDatabase.getDatabase(application).topUpDao()

    fun topUp(account: String, amount: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                dao.insert(TopUp(accountNumber = account, amount = amount))
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
fun TopUpScreen(
    navController: NavController,
    viewModel: TopUpViewModel = viewModel()
) {
    val context = LocalContext.current

    var accountNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Up Account") },
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount to Top Up (KES)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (accountNumber.isNotBlank() && amount.isNotBlank()) {
                            viewModel.topUp(accountNumber, amount) { success ->
                                if (success) {
                                    showDialog = true
                                    accountNumber = ""
                                    amount = ""
                                } else {
                                    Toast.makeText(context, "Failed to top up.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Top Up")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Top-Up Successful") },
                        text = { Text("KES $amount has been topped up to account $accountNumber.") },
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
fun TopUpScreenPreview() {
    TopUpScreen(rememberNavController())
}
