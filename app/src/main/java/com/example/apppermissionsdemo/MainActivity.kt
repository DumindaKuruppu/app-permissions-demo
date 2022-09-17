package com.example.apppermissionsdemo

import android.Manifest
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Permission granted for location", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRequestCameraPermission: Button? = findViewById(R.id.btnRequestCameraPermission)
        val btnSnackBar: Button? = findViewById(R.id.btnSnackBar)
        val btnAlertDialog: Button? = findViewById(R.id.btnAlertDialog)
        val btnCustomDialog: Button? = findViewById(R.id.btnCustomDialog)
        val btnCustomProgress: Button? = findViewById(R.id.btnCustomProgress)


        btnRequestCameraPermission?.setOnClickListener {
            btnRequestCameraPermission!!.background =
                getDrawable(R.drawable.button_background_clicked)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
            ) {
                showRationalDialog(
                    "Camera Permission",
                    "Your mobile does not support this application"
                )
            } else {
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }

        btnSnackBar?.setOnClickListener { view ->
            Toast.makeText(this, "TOAST", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "You have done", Snackbar.LENGTH_SHORT).show()

        }

        btnAlertDialog?.setOnClickListener {
            alertDialogFunction()
        }

        btnCustomDialog?.setOnClickListener {
            customDialogFunction()
            Toast.makeText(this, "Custom Alert Dialog", Toast.LENGTH_SHORT).show()
        }

        btnCustomProgress?.setOnClickListener {
            customDialogProgressFunction()
        }

    }

    private fun customDialogProgressFunction() {
        val customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.custom_progress_dialog)
        customProgressDialog.show()
    }

    private fun customDialogFunction() {
        val button: Button? = findViewById(R.id.button)
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom)
        button?.setOnClickListener {
            Toast.makeText(applicationContext, "How", Toast.LENGTH_SHORT).show()
            customDialog.dismiss()
        }
        customDialog.show()


    }

    private fun alertDialogFunction() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("This is alert dialog. Which is used to show alert")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            Toast.makeText(applicationContext, "Operation cancelled !", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showRationalDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}