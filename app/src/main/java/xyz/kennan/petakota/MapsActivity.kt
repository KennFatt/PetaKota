package xyz.kennan.petakota

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mLocMgr: LocationManager
    private lateinit var mLoc: Location

    private lateinit var currentLatLng: LatLng
    private lateinit var mapMarker: MarkerOptions

    private var latestUpdatedTimestamp: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Set status bar to transparent */
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /** Preparing to find device's location */
        updateLocation()
    }

    /**
     * Update and store device's Location
     *
     * NOTE: This might not ideal to check the permission every time.
     * TODO: Make workaround to suppress permissions validation error.
     */
    private fun updateLocation() {
        mLocMgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val l = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (l != null) {
                mLoc = l
                currentLatLng = LatLng(mLoc.latitude, mLoc.longitude)
                latestUpdatedTimestamp = Utility.milliseconds()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /** Update basic maps object into the screen */
        mapMarker = MarkerOptions().position(currentLatLng).title("Your Position")
        mMap.addMarker(mapMarker)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        mMap.setMinZoomPreference(15.0F)

        /**
         * Set CameraIdleListener to update
         *  position within 5 seconds interval.
         *
         *  The interval is counted whenever the Listener were triggered.
         */
        mMap.setOnCameraIdleListener {
            val now = System.currentTimeMillis() / 1000
            if (now - latestUpdatedTimestamp > 5) {
                updateLocation()
                mapMarker.position(currentLatLng)
            }
        }
    }
}
