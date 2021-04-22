package com.steven.scannerapp.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.steven.scannerapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScannerFragment : Fragment() {

    @Inject
    lateinit var scannerViewModel: ScannerViewModel

    private lateinit var cameraButton: ImageButton

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
                        "ScannerFragment",
                        "Permission ${Manifest.permission.CAMERA} is granted."
                    )
                } else {
                    Log.d(
                        "ScannerFragment",
                        "Permission ${Manifest.permission.CAMERA} is NOT granted."
                    )
                }
            }

        cameraButton = view.findViewById(R.id.imagebutton_start_camera)
        cameraButton.setOnClickListener {
            if (!hasPermission()) {
                activityResultLauncher.launch(Manifest.permission.CAMERA)
            } else {
                openCamera(view)
                Log.d("ScannerFragment", "blubb")
            }
        }
    }

    private fun openCamera(view: View) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val viewFinder: PreviewView = view.findViewById(R.id.previewView)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

                preview.setSurfaceProvider(viewFinder.surfaceProvider)

                viewFinder.visibility = View.VISIBLE
                cameraButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Camera started!", Toast.LENGTH_SHORT).show()
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