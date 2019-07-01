package com.dynamiteam.lyahlaundryAdmin.fragments.driverTracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dynamiteam.lyahlaundryAdmin.R
import com.dynamiteam.lyahlaundryAdmin.activities.MainCallback
import com.dynamiteam.lyahlaundryAdmin.base.BaseFragment
import com.dynamiteam.lyahlaundryAdmin.tools.DEFAULT_MAP_ZOOM
import com.dynamiteam.lyahlaundryAdmin.tools.bindInterfaceOrThrow
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.hide
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.loadCircularImage
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.setClickListeners
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.show
import com.dynamiteam.lyahlaundryAdmin.utils.LatLngInterpolator
import com.dynamiteam.lyahlaundryAdmin.utils.MarkerAnimation
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fr_driver_tracking.*


@Suppress("UNCHECKED_CAST")
class DriverTrackingFragment : BaseFragment<DriverTrackingVM>(), OnMapReadyCallback, LocationListener,
    View.OnClickListener {


    private var mMap: GoogleMap? = null
    override val layoutId = R.layout.fr_driver_tracking
    override val viewModelClass = DriverTrackingVM::class.java
    private lateinit var locationManager: LocationManager
    private var listCurrentLocations = ArrayList<SampleData>()
    private var marker: Marker? = null
    private var myLocation: LatLng? = null
    private var choosenDriverData: DriverData? = null

    private val request: PermissionRequest by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }

    private var nearByDriversObserver = Observer<List<SampleData>> {
        mMap?.clear()
        it.forEachIndexed { i, sampleData ->
            marker =
                mMap?.addMarker(MarkerOptions().position(sampleData.latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))
            if (listCurrentLocations.isNotEmpty() && sampleData.userId == listCurrentLocations[i].userId) {
                if (sampleData.latLng != listCurrentLocations[i].latLng) {
                    marker?.let { it1 ->
                        MarkerAnimation.animateMarkerToGB(
                            it1,
                            sampleData.latLng,
                            LatLngInterpolator.Spherical()
                        )
                    };
                }
            }
        }
        listCurrentLocations = it as ArrayList<SampleData>
        myLocation?.let { it1 -> viewModel.chooseDriver(it1) }
    }

    private var nearestDriverObserver = Observer<DriverData> {
        choosenDriver(it)
    }


    val database = FirebaseDatabase.getInstance()
    val dbRef = database.getReference("location")

    override fun observeLiveData() {
        viewModel.run {
            nearByDriversLD.observe(this@DriverTrackingFragment, nearByDriversObserver)
            nearestDriverLD.observe(this@DriverTrackingFragment, nearestDriverObserver)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        request.send()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setClickListeners(driverChooseTrackDriver)
        var list = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback?.showSnack("cancelled")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.convertToModel(snapshot.value as HashMap<String, HashMap<String, String>>)
            }
        })

        request.listeners {
            onAccepted {
                getLocation()
            }
            onDenied {
                callback?.showSnack("permission denied")
            }
            onPermanentlyDenied {
                callback?.showSnack("permission denied permanently")
            }
            onShouldShowRationale { strings, permissionNonce ->
                callback?.showSnack("blahh")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        callback?.showSnack("WoooHHHH")
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isNetworkEnabled && !isGpsEnabled) {
            callback?.showSnack("no gps and network connection")
        } else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, this)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, this)
            }
        }
    }

    var callback: MainCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<MainCallback>(parentFragment, context)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.setOnCameraIdleListener {
            if (choosenDriverData != null)
                nearestDriverContainer.show()
        }
        mMap?.setOnCameraMoveListener { println("CAMERA MOVE");nearestDriverContainer.hide() }
        mMap?.setOnCameraMoveCanceledListener { println("CAMERA MOVE");nearestDriverContainer.hide() }
        mMap?.setOnCameraMoveStartedListener { println("CAMERA MOVE STARTED"); nearestDriverContainer.hide() }
        mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.uber_theme))
    }

    override fun onLocationChanged(location: Location?) {
        showSnack(location?.latitude.toString() + "\n" + location?.longitude)
        var latLng = location?.latitude?.let { LatLng(it, location?.longitude) }
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_MAP_ZOOM))
        locationManager.removeUpdates(this)
        myLocation = location?.latitude?.let { LatLng(it, location?.longitude) }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private fun choosenDriver(driverData: DriverData) {
        driverNameTrackDriver.text = "Noushad Chullian"
        driverImageTrackDriver.loadCircularImage(
            "www.openhost.co.za/download/bootmin/img/avatar_lg.jpg",
            R.mipmap.ic_launcher
        )
        choosenDriverData = driverData
        nearestDriverContainer.show()

    }

    override fun onClick(v: View?) {
        when (v) {
            driverChooseTrackDriver -> callback?.showSnack("Choosen Driver Id = " + choosenDriverData?.userId)
        }
    }
}

data class SampleData(
    var userId: String,
    var latLng: LatLng
)
