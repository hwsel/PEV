package io.github.thibaultbee.streampack.camera2.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import io.github.thibaultbee.streampack.camera2.R

private val PERMISSIONS_REQUIRED = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.RECORD_AUDIO,
//    android.Manifest.permission.INTERNET,
//    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
)

class PermissionFragment : Fragment() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Fragment", "PermissionFragment: onCreate()")

        if (hasPermissions(requireContext())) {
            navigateToCamera()
        } else {
            PERMISSIONS_REQUIRED.forEach {
                requestPermissionLauncher.launch(it)
            }
            if (hasPermissions(requireContext())) {
                navigateToCamera()
            }
        }
    }

    private fun navigateToCamera() {
        Log.i("Fragment", "PermissionFragment: navigateToCamera()")
        lifecycleScope.launch {
            withStarted {
                Navigation.findNavController(
                    requireActivity(),
                    R.id.activity_main_fragment_container
                ).navigate(PermissionFragmentDirections.actionPermissionFragmentToCameraFragment())
            }
        }
    }

    companion object {
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}