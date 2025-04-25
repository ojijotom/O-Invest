package com.ojijo.o_invest.ui.screens.invest

import android.app.*
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/* --- ROOM DATABASE --- */
@Entity(tableName = "transactions")
data class InvestmentTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: InvestmentTransaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<InvestmentTransaction>>

    @Query("SELECT SUM(amount) FROM transactions")
    fun getTotalBalance(): Flow<Double?>
}

@Database(entities = [InvestmentTransaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "investment_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/* --- VIEWMODEL --- */
class InvestmentViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.transactionDao()
    val transactions: Flow<List<InvestmentTransaction>> = dao.getAll()
    val balance: Flow<Double> = dao.getTotalBalance().map { it ?: 0.0 }

    // Flutterwave Test Secret Key
    private val flutterwaveSecretKey = "FLWSECK_TEST-742115817aae25d96f4889229a1f726b-X"

    fun depositWithFlutterwave(amount: Double, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val json = """
                {
                    "tx_ref": "TX-${UUID.randomUUID()}",
                    "amount": "$amount",
                    "currency": "KES",
                    "redirect_url": "https://yourapp.com/callback",
                    "payment_options": "mpesa",
                    "customer": {
                        "email": "user@example.com",
                        "phonenumber": "2547XXXXXXXX",
                        "name": "John Doe"
                    },
                    "customizations": {
                        "title": "Investment Deposit",
                        "description": "Deposit to investment account"
                    }
                }
            """
            val body = json.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("https://api.flutterwave.com/v3/payments")
                .addHeader("Authorization", "Bearer $flutterwaveSecretKey")  // Added the key here
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val tx = InvestmentTransaction(amount = amount, description = "M-Pesa via Flutterwave")
                    dao.insert(tx)
                    Handler(Looper.getMainLooper()).post {
                        showNotification(context, "Deposit Successful", "KES $amount added to your balance.")
                        Toast.makeText(context, "M-Pesa payment successful", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Payment failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "deposit_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Deposits",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for deposit notifications"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, notification)
        } else {
            Toast.makeText(context, "Notification Manager not available", Toast.LENGTH_SHORT).show()
        }
    }
}

/* --- COMPOSABLE SCREEN --- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestScreen(navController: NavController, viewModel: InvestmentViewModel = viewModel(factory = object : ViewModelProvider.Factory {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvestmentViewModel(application) as T
    }
})) {
    val context = LocalContext.current
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    val balance by viewModel.balance.collectAsState(initial = 0.0)
    var depositAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Investment Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Text("Current Balance", style = MaterialTheme.typography.titleLarge)
                Text("KES ${"%.2f".format(balance)}", style = MaterialTheme.typography.headlineMedium)

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
                            viewModel.depositWithFlutterwave(amount, context)
                            depositAmount = ""
                        } else {
                            Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = depositAmount.isNotBlank()
                ) {
                    Text("Deposit with M-Pesa")
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(transactions) { tx ->
                        Text("KES ${tx.amount.toInt()} • ${Date(tx.timestamp)}")
                        Divider()
                    }
                }
            }
        }
    )
}
