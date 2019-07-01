package com.dynamiteam.lyahlaundryAdmin.tools

import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BundleDelegate<T : Any> : ReadWriteProperty<Fragment, T?> {

    private var value: T? = null
    private var isDeleted = false

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T? =
        value ?: takeUnless { isDeleted }?.let {
            (thisRef.arguments?.get(property.name + thisRef::class.java) as? T)
        }.also { value = it }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        isDeleted = value == null
        this.value = value
        value?.let {
            val args = thisRef.arguments ?: Bundle().apply { thisRef.arguments = this }
            val key = property.name + thisRef::class.java

            when (it) {
                is String -> args.putString(key, it)
                is Int -> args.putInt(key, it)
                is Short -> args.putShort(key, it)
                is Long -> args.putLong(key, it)
                is Byte -> args.putByte(key, it)
                is ByteArray -> args.putByteArray(key, it)
                is Char -> args.putChar(key, it)
                is CharArray -> args.putCharArray(key, it)
                is CharSequence -> args.putCharSequence(key, it)
                is Float -> args.putFloat(key, it)
                is Bundle -> args.putBundle(key, it)
                is Binder -> BundleCompat.putBinder(args, key, it)
                is Parcelable -> args.putParcelable(key, it)
                is Serializable -> args.putSerializable(key, it)
                else -> throw IllegalArgumentException("Type ${value.javaClass.canonicalName} of property ${property.name} is not supported")
            }
        }
    }

}