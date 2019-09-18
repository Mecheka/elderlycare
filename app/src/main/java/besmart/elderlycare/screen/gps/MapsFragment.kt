package besmart.elderlycare.screen.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MapsFragment : Fragment() {

    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFusedLoction: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private val mLocationCallBack: LocationCallback? = null
    private val REQEUST_LOCATION = 701
    private val viewmodel: GPSViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gps_maps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerViewModel()
        try {
            mFusedLoction = LocationServices.getFusedLocationProviderClient(context!!)
            mLocationRequest = LocationRequest.create()
            mLocationRequest?.interval = 5000
            mLocationRequest?.fastestInterval = 1000
            mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(activity!!)) {
                Log.e("keshav", "Gps already enabled")
                Toast.makeText(activity, "Gps not enabled", Toast.LENGTH_SHORT).show()
                enableLoc()
            } else {
                Log.e("keshav", "Gps already enabled")
                Toast.makeText(activity, "Gps already enabled", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mMap == null) {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync { googleMap -> createMaps(googleMap) }
        }

    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient != null) {
            try {
                mFusedLoction?.removeLocationUpdates(mLocationCallBack)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun observerViewModel(){

        viewmodel.profileLiveData.observe(this, Observer {list->
            Log.d("Shared viewmod data", "ok")
            list.forEach {profile->
                if (profile.latitude != null && profile.longitude != null){
                    mMap?.addMarker(MarkerOptions()
                        .position(LatLng(profile.latitude, profile.longitude))
                        .title(profile.firstName))
                }
            }
        })
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
    private fun createMaps(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(13.8513962850, 100.6877681210)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(12f))

        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    mMap?.isMyLocationEnabled = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                }
            }).check()
    }

    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mgr.allProviders ?: return false
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    private fun enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {

                    }

                    override fun onConnectionSuspended(i: Int) {
                        mGoogleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }
                .build()
            mGoogleApiClient?.connect()

            if (mLocationRequest != null) {

                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest!!)

                builder.setAlwaysShow(true)

                val result = LocationServices.SettingsApi.checkLocationSettings(
                    mGoogleApiClient,
                    builder.build()
                )
                result.setResultCallback { result ->
                    val status = result.status
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, REQEUST_LOCATION)

                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }

                    }
                }
            }
        }
    }
}
