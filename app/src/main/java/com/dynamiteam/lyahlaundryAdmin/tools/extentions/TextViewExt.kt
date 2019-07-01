package com.dynamiteam.lyahlaundryAdmin.tools.extentions

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setTextColorCompat(@ColorRes colorRes: Int) {
    setTextColor(ContextCompat.getColor(context, colorRes))
}

fun TextView.getStringText() = this.text.toString()
fun TextView.getIntText() = this.text.toString().toInt()
