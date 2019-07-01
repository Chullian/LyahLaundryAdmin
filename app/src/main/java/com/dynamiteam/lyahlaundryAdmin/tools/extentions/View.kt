package com.dynamiteam.lyahlaundryAdmin.tools.extentions

import android.view.View
import android.view.View.*
import androidx.annotation.DrawableRes

fun View.OnClickListener.setClickListeners(vararg views: View) {
    views.forEach { view -> view.setOnClickListener(this) }
}

fun View.setDrawable(@DrawableRes drawableRes: Int) {
    background = context.getDrawable(drawableRes)
}

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) GONE else INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}

fun View.isVisible(): Boolean = visibility == VISIBLE