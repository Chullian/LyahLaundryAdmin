package com.dynamiteam.lyahlaundryAdmin.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chullian.foodbank.data.network.ApiRepo
import com.chullian.foodbank.data.network.RetrofitClient

abstract class BaseViewModel : ViewModel() {

    val api: ApiRepo = RetrofitClient.instance.api
    val errorObserverLD = MutableLiveData<Throwable>()
}