package com.dynamiteam.lyahlaundryAdmin.fragments.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.dynamiteam.lyahlaundryAdmin.R
import com.dynamiteam.lyahlaundryAdmin.activities.MainCallback
import com.dynamiteam.lyahlaundryAdmin.base.BaseFragment
import com.dynamiteam.lyahlaundryAdmin.tools.PrefManager
import com.dynamiteam.lyahlaundryAdmin.tools.bindInterfaceOrThrow
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.setClickListeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import kotlinx.android.synthetic.main.fr_home.*

class HomeFragment : BaseFragment<HomeVM>(), LocationListener, View.OnClickListener {

    private val request: PermissionRequest by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }
    override val layoutId = R.layout.fr_home
    override val viewModelClass = HomeVM::class.java
    private lateinit var locationManager: LocationManager

    override fun observeLiveData() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListeners(logout)
        callback?.showTrackFragment()
//        request.send()
//        request.listeners {
//            onAccepted {
//                getLocation()
//            }
//            onDenied {
//                callback?.showSnack("permission denied")
//            }
//            onPermanentlyDenied {
//                callback?.showSnack("permission denied permanently")
//            }
//            onShouldShowRationale { strings, permissionNonce ->
//                callback?.showSnack("blahh")
//            }
//        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        callback?.showTrackFragment()
//        callback?.showSnack("WoooHHHH")
       /* locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isNetworkEnabled && !isGpsEnabled) {
            callback?.showSnack("no gps and network connection")
        } else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0f, this)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0f, this)
            }
        }*/
    }

    var callback: MainCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<MainCallback>(parentFragment, context)
    }

    override fun onLocationChanged(location: Location?) {
//        showSnack(location?.latitude.toString() + "\n" + location?.longitude)

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onClick(v: View?) {
        PrefManager.clearSession()
        callback?.showLoginFragment()
    }
}


//name
//phone
//address
//password