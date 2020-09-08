package com.example.phonescanner

import android.app.AlertDialog
import android.content.DialogInterface

fun MainActivity.showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", okListener)
        .create()
        .show()
}
