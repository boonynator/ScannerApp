package com.steven.scannerapp.data.api

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.steven.scannerapp.data.model.Item
import javax.inject.Inject

class ImageAnalyzer @Inject constructor() : ImageAnalysis.Analyzer {

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            // get correct rotation from camerax lib
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val barcodeScannerOptions = BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            ).build()
            val scanner = BarcodeScanning.getClient(barcodeScannerOptions)
            val result = scanner.process(image)
                .addOnSuccessListener {
                    Log.d("ImageAnalyzer", "Image was successfully scanned!")
                    Log.d("ImageAnalyzer", "Value: $it")
                    prepareItem(it)
                }.addOnFailureListener {
                    Log.d("ImageAnalyzer", "Image could not be scanned.")
                }.addOnCompleteListener {
                    Log.d("ImageAnalyzer", "Task finished.")
                }
        }

    }

    fun prepareItem(barcodes: List<Barcode>): Item {
        TODO()
    }
}