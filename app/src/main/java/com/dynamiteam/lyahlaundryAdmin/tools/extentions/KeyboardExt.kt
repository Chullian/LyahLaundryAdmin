package com.dynamiteam.lyahlaundryAdmin.tools.extentions

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


internal val NO_FLAGS = 0

fun Activity.hideKeyboard() = currentFocus?.let {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(it.windowToken, NO_FLAGS)
    }
}

fun Activity.hasInternetConnection(): Boolean? {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnected
    //    val isConnected: Boolean = activeNetworkNetwork?.isConnectedOrConnecting == true
}

fun Fragment.hideKeyboard() = activity?.hideKeyboard()

fun android.app.Fragment.hideKeyboard() = activity.hideKeyboard()


