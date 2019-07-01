package com.dynamiteam.lyahlaundryAdmin.fragments.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dynamiteam.lyahlaundryAdmin.base.BaseViewModel
import com.dynamiteam.lyahlaundryAdmin.data.model.request.RegsitrationRequest
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


class RegistrationVM : BaseViewModel() {

    var emptyFieldLD = MutableLiveData<String>()
    var registrationResponseLD = MutableLiveData<Any>()


    fun registerUser(regsitrationRequest: RegsitrationRequest) {
        if (!checkEmptyFeild(regsitrationRequest)) {
            registrationResponseLD.postValue("TEST")
//            TODO("registration goes here")
        }
    }

    fun getCountries() {

    }


    private fun checkEmptyFeild(registrationRequest: RegsitrationRequest): Boolean {
        var hasEmptyField = false
        if (registrationRequest.password.isNullOrEmpty()) {
            emptyFieldLD.postValue("password");
            return true
        } else {
            if (registrationRequest.password.length < 8) {
                emptyFieldLD.postValue("weak password")
                return true
            } else hasEmptyField = false
        }
        if (registrationRequest.phone.isNullOrEmpty()) {
            emptyFieldLD.postValue("phone");return true
        } else {
            hasEmptyField = false
        }
        if (registrationRequest.address.isNullOrEmpty()) {
            emptyFieldLD.postValue("address");return true
        } else {
            hasEmptyField = false
        }
        if (registrationRequest.name.isNullOrEmpty()) {
            emptyFieldLD.postValue("name");return true
        } else {
            hasEmptyField = false
        }
        return hasEmptyField
    }

    fun parseCountry(countryISO: String, countries: InputStream): Country {
        var list = ArrayList<Country>()
        viewModelScope.launch(Dispatchers.IO) {
            var jsonString = countries.bufferedReader().use { it.readText() }.toString()
            list = jsonConverter(jsonString) as ArrayList<Country>
        }
        return list.filter { it.code == countryISO }[0]
    }

    fun jsonConverter(json: String): List<Country> {
        val gson = Gson()
        val listType = object : TypeToken<List<Country>>() {}.type
        return gson.fromJson(json, listType)
    }

}

data class Country(
    @SerializedName("code")
    var code: String?,
    @SerializedName("dial_code")
    var dialCode: String?,
    @SerializedName("name")
    var name: String?
)
