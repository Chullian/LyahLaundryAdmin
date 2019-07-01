package com.dynamiteam.lyahlaundryAdmin.fragments.driverTracking

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.dynamiteam.lyahlaundryAdmin.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng

class DriverTrackingVM : BaseViewModel() {
    var nearByDriversLD = MutableLiveData<List<SampleData>>()
    var nearByExistingDrivers = ArrayList<SampleData>()
    var animateDriverLD = MutableLiveData<SampleData>()
    var nearestDriverLD = MutableLiveData<DriverData>()
    fun convertToModel(data: HashMap<String, HashMap<String, String>>) {
        var list = ArrayList<SampleData>()
        data.forEach {
            var userId = it.key
            var lat: Double = it.value["lat"] as Double
            var lon: Double = it.value["lon"] as Double
            list.add(SampleData(userId, LatLng(lat, lon)))
        }
        nearByDriversLD.postValue(list)
        locationChecking(list)
    }

    fun locationChecking(list: List<SampleData>) {
        list.forEachIndexed { i, sampleData ->
            if (nearByExistingDrivers.isNotEmpty()) {
                if (sampleData.userId == nearByExistingDrivers[i].userId)
                    if (sampleData.latLng != nearByExistingDrivers[i].latLng) {
                        animateDriverLD.postValue(sampleData)
                    }
            }
        }
        nearByExistingDrivers = list as ArrayList<SampleData>
    }

    fun distanceBetweenCordinates(source: LatLng, destination: LatLng): Float {
        var sourceLocation = Location("")
        sourceLocation.latitude = source.latitude
        sourceLocation.longitude = source.longitude

        var destinationLocation = Location("")
        destinationLocation.latitude = destination.latitude
        destinationLocation.longitude = destination.longitude

        return sourceLocation.distanceTo(destinationLocation)
    }

    fun chooseDriver(latLng: LatLng) {
        nearestDriver(latLng)
    }

    private fun nearestDriver(source: LatLng) {
        var list = ArrayList<DriverData>()
        nearByExistingDrivers.forEach {
            var distance = distanceBetweenCordinates(source, it.latLng)
            list.add(DriverData(it.userId, it.latLng, distance))
        }
        var driver = list.minBy { it.distance }
        nearestDriverLD.postValue(driver)
    }
}

data class DriverData(
    var userId: String,
    var latLng: LatLng,
    var distance: Float
)