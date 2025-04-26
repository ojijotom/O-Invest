package com.ojijo.o_invest.ui.screens.reversal

import android.content.Context
import android.util.Log
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

// ------------------ Database Section -------------------------

@Entity(tableName = "reversals")
data class ReversalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transactionId: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface ReversalDao {
    @Insert
    suspend fun insertReversal(reversal: ReversalEntity)

    @Query("SELECT * FROM reversals ORDER BY timestamp DESC")
    suspend fun getAllReversals(): List<ReversalEntity>
}

@Database(entities = [ReversalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reversalDao(): ReversalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reversal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ------------------ ViewModel Section -------------------------

class ReversalViewModel(private val context: Context) : ViewModel() {
    private val db = AppDatabase.getDatabase(context)
    private val reversalDao = db.reversalDao()

    private val client = OkHttpClient()

    // Your real Flutterwave Secret Key (test mode)
    private val FLUTTERWAVE_SECRET_KEY = "FLWSECK_TEST-742115817aae25d96f4889229a1f726b-X"

    // UI loading state
    var isLoading = mutableStateOf(false)
        private set

    fun saveReversal(transactionId: String, amount: String) {
        viewModelScope.launch {
            reversalDao.insertReversal(
                ReversalEntity(
                    transactionId = transactionId,
                    amount = amount
                )
            )
            sendRefundRequest(transactionId, amount)
        }
    }

    private fun sendRefundRequest(transactionId: String, amount: String) {
        val url = "https://api.flutterwave.com/v3/refunds"

        val jsonBody = JSONObject()
        jsonBody.put("transaction_id", transactionId)
        jsonBody.put("amount", amount.toDoubleOrNull() ?: 0.0)

        val body: RequestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $FLUTTERWAVE_SECRET_KEY")
            .addHeader("Content-Type", "application/json")
            .build()

        isLoading.value = true // Start loading
        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("FlutterwaveRefund", "Failed: ${e.message}")
                    isLoading.value = false
                    showToast("Refund failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        Log.d("FlutterwaveRefund", "Success: ${response.body?.string()}")
                        showToast("Refund request successful!")
                    } else {
                        Log.e("FlutterwaveRefund", "Error: ${response.body?.string()}")
                        showToast("Refund request failed. Please verify transaction.")
                    }
                }
            })
        }
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}

// ------------------ UI Screen Section -------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReversalScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ReversalViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReversalViewModel(context) as T
        }
    })

    var transactionId by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reverse Transaction") },
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
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = transactionId,
                        onValueChange = { transactionId = it },
                        label = { Text("Transaction ID") },
                        keyboardOptions = KeyboardOptions.Default,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount to Reverse (KES)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (transactionId.isNotBlank() && amount.isNotBlank()) {
                                viewModel.saveReversal(transactionId, amount)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = transactionId.isNotBlank() && amount.isNotBlank()
                    ) {
                        Text(text = "Reverse Funds")
                    }
                }

                if (isLoading) {
                    // Show loading indicator
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReversalScreenPreview() {
    ReversalScreen(rememberNavController())
}
