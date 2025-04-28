package com.ojijo.o_invest.ui.screens.mpesapaybill

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
import java.util.*

// --- ROOM ENTITY ---
@Entity(tableName = "mpesa_payments")
data class MpesaPayment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val paybillNumber: String,
    val accountNumber: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAO ---
@Dao
interface MpesaPaymentDao {
    @Insert
    suspend fun insert(payment: MpesaPayment)

    @Query("SELECT * FROM mpesa_payments ORDER BY timestamp DESC")
    suspend fun getAll(): List<MpesaPayment>
}

// --- DATABASE ---
@Database(entities = [MpesaPayment::class], version = 1)
abstract class MpesaDatabase : RoomDatabase() {
    abstract fun paymentDao(): MpesaPaymentDao

    companion object {
        @Volatile private var INSTANCE: MpesaDatabase? = null

        fun getDatabase(context: android.content.Context): MpesaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MpesaDatabase::class.java,
                    "mpesa_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL ---
class MpesaPaybillViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MpesaDatabase.getDatabase(application).paymentDao()

    fun savePayment(paybill: String, account: String, amount: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                dao.insert(MpesaPayment(paybillNumber = paybill, accountNumber = account, amount = amount))
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
fun MpesaPaybillScreen(
    navController: NavController,
    viewModel: MpesaPaybillViewModel = viewModel()
) {
    val context = LocalContext.current

    var paybillNumber by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MPESA Paybill") },
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
                    value = paybillNumber,
                    onValueChange = { paybillNumber = it },
                    label = { Text("Paybill Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account Number") },
                    keyboardOptions = KeyboardOptions.Default,
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
                        if (paybillNumber.isNotBlank() && accountNumber.isNotBlank() && amount.isNotBlank()) {
                            showDialog = true
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill all the fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = paybillNumber.isNotBlank() && accountNumber.isNotBlank() && amount.isNotBlank()
                ) {
                    Text("Pay Now")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Payment") },
                    text = {
                        Text("Pay KES $amount to Paybill $paybillNumber for account $accountNumber?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.savePayment(paybillNumber, accountNumber, amount) { success ->
                                val msg = if (success) {
                                    "Payment of KES $amount to $paybillNumber saved!"
                                } else {
                                    "Payment failed to save."
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MpesaPaybillScreenPreview() {
    MpesaPaybillScreen(rememberNavController())
}
