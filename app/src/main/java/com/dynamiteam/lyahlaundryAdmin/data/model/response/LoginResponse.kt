package com.dynamiteam.lyahlaundryAdmin.data.model.response
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("relation")
    var relation: String?
)