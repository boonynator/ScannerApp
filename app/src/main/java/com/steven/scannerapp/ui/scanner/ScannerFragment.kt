package com.steven.scannerapp.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.steven.scannerapp.R
import com.steven.scannerapp.data.api.BarcodeAnalyzer
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class ScannerFragment : Fragment() {

    @Inject
    lateinit var scannerViewModel: ScannerViewModel

    private lateinit var cameraButton: ImageButton
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var successProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_scanner, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d(
                        TAG,
                        "Permission ${Manifest.permission.CAMERA} is granted."
                    )
                } else {
                    Log.d(
                        TAG,
                        "Permission ${Manifest.permission.CAMERA} is NOT granted."
                    )
                }
            }

        backgroundExecutor = Executors.newSingleThreadExecutor()

        cameraButton = view.findViewById(R.id.imagebutton_start_camera)
        cameraButton.setOnClickListener {
            if (!hasPermission()) {
                activityResultLauncher.launch(Manifest.permission.CAMERA)
            } else {
                openCamera(view)
                Log.d(TAG, "Initiating opening of camera")
            }
        }

        successProgressBar = view.findViewById(R.id.success_loading)
        // set progressbar color
        val progressBarColor = resources.getColor(R.color.design_default_color_primary_dark, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            successProgressBar.indeterminateDrawable.colorFilter =
                BlendModeColorFilter(progressBarColor, BlendMode.MULTIPLY)
        } else {
            successProgressBar.indeterminateDrawable.setColorFilter(
                progressBarColor,
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun openCamera(view: View) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val viewFinder: PreviewView = view.findViewById(R.id.previewView)

        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val aspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

        val rotation = viewFinder.display.rotation
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetRotation(rotation)
                .build()

            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(rotation)
                .setTargetAspectRatio(aspectRatio)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetRotation(rotation)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor, BarcodeAnalyzer { barcode ->
                        openSuccessDialog(barcode)
                        Log.d(TAG, "Dialog should've opened.")
                    })
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )

                preview.setSurfaceProvider(viewFinder.surfaceProvider)

                viewFinder.visibility = View.VISIBLE
                cameraButton.visibility = View.GONE
            } catch (e: Exception) {
                Log.e("ScannerFragment", "Use case binding failed", e)
                Toast.makeText(
                    requireContext(),
                    "Camera could not be initialized.",
                    Toast.LENGTH_SHORT
                ).show()

                viewFinder.visibility = View.GONE
                cameraButton.visibility = View.VISIBLE
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun openSuccessDialog(barcode: String) {
        successProgressBar.visibility = View.VISIBLE
        val successDialog = ScanSuccessDialog(barcode)
        successDialog.show(parentFragmentManager, "testDialog")
        //successProgressBar.visibility = View.GONE
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - (4.0 / 3.0)) <= abs(previewRatio - (16.0 / 9.0))) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun hasPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * The fragments logging TAG.
         */
        private const val TAG = "ScreenFragment"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): ScannerFragment {
            return ScannerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}