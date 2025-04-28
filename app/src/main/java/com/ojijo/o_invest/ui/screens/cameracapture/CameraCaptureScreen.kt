package com.ojijo.o_invest.ui.screens.cameracapture

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// 1. Entity
@Entity(tableName = "faces")
data class FaceCapture(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageBase64: String
)

// 2. DAO
@Dao
interface FaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(face: FaceCapture)

    @Query("SELECT * FROM faces")
    suspend fun getAll(): List<FaceCapture>
}

// 3. Database
@Database(entities = [FaceCapture::class], version = 1)
abstract class FaceDatabase : RoomDatabase() {
    abstract fun faceDao(): FaceDao

    companion object {
        @Volatile
        private var INSTANCE: FaceDatabase? = null

        fun getDatabase(context: Context): FaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaceDatabase::class.java,
                    "face_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// 4. ViewModel
class FaceViewModel(context: Context) : ViewModel() {
    private val db = FaceDatabase.getDatabase(context)
    private val dao = db.faceDao()

    fun saveFace(bitmap: Bitmap) {
        val base64 = bitmap.toBase64()
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(FaceCapture(imageBase64 = base64))
        }
    }
}

// 5. Bitmap Extensions
fun Bitmap.toBase64(): String {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
}

fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

// 6. UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCaptureScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val executor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    val viewModel: FaceViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FaceViewModel(context) as T
        }
    })

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Runtime permission request if needed
    val hasCameraPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    if (!hasCameraPermission) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CAMERA), 101)
    }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val capture = ImageCapture.Builder().build()
        imageCapture = capture

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner, cameraSelector, preview, capture
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Face") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    imageCapture?.takePicture(
                        executor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                val bitmap = imageProxy.toBitmap()
                                imageProxy.close()

                                val options = FaceDetectorOptions.Builder()
                                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                                    .build()

                                val detector = FaceDetection.getClient(options)
                                val inputImage = InputImage.fromBitmap(bitmap, 0)

                                detector.process(inputImage)
                                    .addOnSuccessListener { faces ->
                                        if (faces.isNotEmpty()) {
                                            viewModel.saveFace(bitmap)
                                            Toast.makeText(context, "Face Detected & Saved", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        } else {
                                            Toast.makeText(context, "No face detected!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Detection failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capture Face")
            }
        }
    }
}
