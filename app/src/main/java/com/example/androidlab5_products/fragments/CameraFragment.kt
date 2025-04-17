package com.example.androidlab5_products.fragments

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.Preview
import androidx.fragment.app.Fragment
import com.example.androidlab5_products.ui.HealthViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.androidlab5_products.R
import com.example.androidlab5_products.models.Product
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class CameraFragment(private val viewModel: HealthViewModel) : Fragment() {
    private lateinit var previewView: PreviewView
    private var onSuccessScan:  ((Product?) -> Unit)? = null
    private var isScaning: Boolean = false

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera_view, container, false)
        previewView = view.findViewById(R.id.previewView)
        return view
    }

    @OptIn(ExperimentalGetImage::class)
    fun startCamera(context: Context) {
        var cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val barcodeScanner = BarcodeScanning.getClient()

            val analysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    barcodeScanner.process(inputImage)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                if(isScaning)
                                    return@addOnSuccessListener
                                isScaning = true
                                val rawValue = barcode.rawValue
                                viewModel.fetchProduct(rawValue!!) { product ->
                                    onSuccessScan?.invoke(product)
                                    isScaning = false
                                }
                                Toast.makeText(context, "Сканировано: $rawValue", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Log.e("MLKit", "Ошибка сканирования", it)
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, analysisUseCase
                )
            } catch (exc: Exception) {
                Log.e("CameraX", "Ошибка привязки камеры", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    fun setOnSuccessScan(action: (Product?) -> Unit){
        onSuccessScan = action
    }
}