package com.chullian.foodbank.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    val api: ApiRepo
    private var request: ApiService

    init {
        val retrofit =
            Retrofit.Builder().baseUrl("https:laundry.mylyah.com/api/").addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
        request = retrofit.create(ApiService::class.java)
        api = ApiRepo(request)
    }

    companion object {
        val instance = RetrofitClient()
    }


}