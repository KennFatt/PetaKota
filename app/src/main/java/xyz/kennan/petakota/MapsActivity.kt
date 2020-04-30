package xyz.kennan.petakota

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_REQUEST = 0xFA
    private var mLocation: Location? = null

    private fun d(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        d("onCreate()")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /** Prompt user to accept Location permission */
        validatePermission()

        /** Preparing to find device's location */
        prepareLocation()
    }

    private fun validatePermission() {
        val perms = Manifest.permission.ACCESS_FINE_LOCATION
        val createDialog: () -> AlertDialog = {
            val builder = Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Permission Requirement")
            builder.setMessage("Grant location permission to access the application")
            builder.setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(this, arrayOf(perms), LOCATION_REQUEST)
            }
            builder.setNegativeButton("Exit") { _, _ ->
                finish()
            }

            builder.create()
        }

        if (ActivityCompat.checkSelfPermission(this, perms) != PackageManager.PERMISSION_GRANTED) {
            (createDialog()).show()
        }
    }

    private fun prepareLocation() {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        d("onMapReady()")
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
