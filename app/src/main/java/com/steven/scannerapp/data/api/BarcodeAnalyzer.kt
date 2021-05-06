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
import java.net.MalformedURLException
import java.net.URL
import java.util.*

typealias BarcodeListener = (barcode: String) -> Unit

class BarcodeAnalyzer(private val barcodeListener: BarcodeListener? = null) :
    ImageAnalysis.Analyzer {

    private val listeners = ArrayList<BarcodeListener>().apply { barcodeListener?.let { add(it) } }

    private lateinit var item: Item

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            // get correct rotation from camerax lib
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val barcodeScannerOptions = BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            ).build()
            val scanner = BarcodeScanning.getClient(barcodeScannerOptions)
            scanner.process(image)
                .addOnSuccessListener {
                    prepareItem(it)
                }.addOnFailureListener {
                    Log.e("ImageAnalyzer", "Image could not be scanned.")
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    /**
     * Prepares an Item from a scanned QR oder Barcode.
     */
    private fun prepareItem(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val barcodeValue = barcode.rawValue
            var scannedUrl: URL? = null
            try {
                scannedUrl = URL(barcodeValue)
            } catch (e: MalformedURLException) {
                Log.d(TAG, "Code does not contain url")
            }
            val currentTime = Date(System.currentTimeMillis())
            item =
                Item(code = barcodeValue!!, url = scannedUrl, date = currentTime, name = null)
            Log.d("ImageAnalyzer", "Value: $item")
        }
    }

    companion object {
        private const val TAG = "BarcodeAnalyzer"
    }
}