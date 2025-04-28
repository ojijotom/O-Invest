package com.ojijo.o_invest.ui.screens.withdrawal

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.launch

// --- ROOM ENTITY ---
@Entity(tableName = "withdrawals")
data class WithdrawalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountNumber: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAO ---
@Dao
interface WithdrawalDao {
    @Insert
    suspend fun insert(withdrawal: WithdrawalEntity)

    @Query("SELECT * FROM withdrawals ORDER BY timestamp DESC")
    suspend fun getAll(): List<WithdrawalEntity>
}

// --- DATABASE ---
@Database(entities = [WithdrawalEntity::class], version = 1)
abstract class WithdrawalDatabase : RoomDatabase() {
    abstract fun withdrawalDao(): WithdrawalDao

    companion object {
        @Volatile private var INSTANCE: WithdrawalDatabase? = null

        fun getDatabase(context: android.content.Context): WithdrawalDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WithdrawalDatabase::class.java,
                    "withdrawals_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL ---
class WithdrawalViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = WithdrawalDatabase.getDatabase(application).withdrawalDao()

    fun requestWithdrawal(accountNumber: String, amount: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                dao.insert(WithdrawalEntity(accountNumber = accountNumber, amount = amount))
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
fun WithdrawScreen(
    navController: NavController,
    viewModel: WithdrawalViewModel = viewModel()
) {
    val context = LocalContext.current

    var accountNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Withdraw Funds") },
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
                    label = { Text("Amount to Withdraw (KES)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (accountNumber.isNotBlank() && amount.isNotBlank()) {
                            viewModel.requestWithdrawal(accountNumber, amount) { success ->
                                val message = if (success) {
                                    "Withdrawal of KES $amount requested for account $accountNumber"
                                } else {
                                    "Failed to process withdrawal request"
                                }
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                if (success) {
                                    accountNumber = ""
                                    amount = ""
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill all the fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = accountNumber.isNotBlank() && amount.isNotBlank()
                ) {
                    Text(text = "Withdraw Funds")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WithdrawScreenPreview() {
    WithdrawScreen(rememberNavController())
}
