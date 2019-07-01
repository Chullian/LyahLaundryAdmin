package com.chullian.foodbank.data.network

import com.dynamiteam.lyahlaundryAdmin.data.model.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap

interface ApiService {
    @Multipart
    @POST("login")
    suspend fun login(@PartMap loginRequest: HashMap<String, RequestBody>): LoginResponse
}