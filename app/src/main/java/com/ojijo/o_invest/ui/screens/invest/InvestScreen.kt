package com.ojijo.o_invest.ui.screens.invest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val phoneNumber: String,
    val timestamp: Long,
    val description: String
)

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Query("DELETE FROM deposits")
    suspend fun clearAll()
}

@Database(entities = [DepositEntity::class], version = 1)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile private var INSTANCE: DepositDatabase? = null

        fun getInstance(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposit_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

class InvestmentViewModel : ViewModel() {
    private val flutterwaveSecretKey = "FLWSECK_TEST-4b97cd352cf15506bcf4a947e9df5b86-X"

    private val _recentActivities = mutableStateListOf<String>()
    val recentActivities: List<String> get() = _recentActivities

    private var _balance = mutableStateOf(0.0)
    val balance: State<Double> get() = _balance

    fun depositWithFlutterwave(amount: Double, phoneNumber: String, context: Context) {
        val db = DepositDatabase.getInstance(context)
        val dao = db.depositDao()

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
                        "email": "ojijotom4@gmail.com",
                        "phonenumber": "$phoneNumber",
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
                .addHeader("Authorization", "Bearer $flutterwaveSecretKey")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonObj = JSONObject(responseBody)
                    val link = jsonObj.getJSONObject("data").getString("link")

                    val description = "Deposited KES $amount on ${Date()}"
                    val depositEntity = DepositEntity(
                        amount = amount,
                        phoneNumber = phoneNumber,
                        timestamp = System.currentTimeMillis(),
                        description = description
                    )
                    dao.insert(depositEntity)

                    _recentActivities.add(description)
                    _balance.value += amount

                    Handler(Looper.getMainLooper()).post {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        Toast.makeText(context, "Follow the instructions to complete payment", Toast.LENGTH_LONG).show()
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

    fun clearActivities(context: Context) {
        val db = DepositDatabase.getInstance(context)
        val dao = db.depositDao()
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearAll()
            _recentActivities.clear()
        }
    }
}

@Composable
fun InvestScreen(
    navController: NavHostController,
    viewModel: InvestmentViewModel = viewModel()
) {
    var depositAmount by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    val isPhoneNumberValid = phoneNumber.startsWith("2547") && phoneNumber.length == 12
    val balance = viewModel.balance.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        Text("Invest Now", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Balance: KES ${"%.2f".format(balance)}", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = depositAmount,
            onValueChange = { depositAmount = it },
            label = { Text("Amount", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number (2547...)", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "NOTE: This uses Flutterwave test mode. No real M-Pesa prompt will appear.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFFFA500)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = depositAmount.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(context, "Enter a valid deposit amount", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (!isPhoneNumberValid) {
                    Toast.makeText(context, "Enter a valid phone number (2547XXXXXXXX)", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.depositWithFlutterwave(amount, phoneNumber, context)
                depositAmount = ""
                phoneNumber = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = depositAmount.isNotBlank() && isPhoneNumberValid,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Deposit with M-Pesa", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Recent Activities", style = MaterialTheme.typography.titleMedium, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight(0.4f)) {
            items(viewModel.recentActivities) { activity ->
                Text(
                    activity,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.clearActivities(context) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Clear Activities", color = Color.White)
        }
    }
}
