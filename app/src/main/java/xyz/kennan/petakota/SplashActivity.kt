package xyz.kennan.petakota

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import pub.devrel.easypermissions.EasyPermissions

class SplashActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        /** Ask the users to accept some of required permissions */
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Handler().postDelayed({ promptPermissionDialog() }, 500L)
        } else {
            switchToMaps()
        }
    }

    /**
     * Prompt the user to grant some required permissions.
     */
    private fun promptPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Permission Requirement")
        builder.setMessage("Grant location permission to access the application")
        builder.setPositiveButton("OK") { _, _ ->
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0xF0)
        }

        builder.create().show()
    }

    /**
     * Switch to main context within a .5 seconds delay.
     *
     * The idea of the delay is to make UX better.
     */
    private fun switchToMaps() {
        Handler().postDelayed({
            startActivity(Intent(this, MapsActivity::class.java))
            finish()
        }, 500L)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /** Pass to EasyPermission to invoke the listener */
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        /** Close current (one and only) activity whenever users reject it */
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        /** Everything seems to be okay, continue to the main context */
        switchToMaps()
    }
}
