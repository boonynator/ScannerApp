package com.steven.scannerapp.ui.scanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import javax.inject.Inject

class ScanSuccessDialog @Inject constructor(private val barcode: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Open $barcode ?")
                .setPositiveButton("Open", DialogInterface.OnClickListener { _, id ->
                    Log.d(TAG, "Dialog $id accepted.")
                })
                .setNegativeButton("Close", DialogInterface.OnClickListener { _, id ->
                    Log.d(TAG, "Dialog $id cancelled.")
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        private const val TAG = "ScanSuccessDialog"
    }

}