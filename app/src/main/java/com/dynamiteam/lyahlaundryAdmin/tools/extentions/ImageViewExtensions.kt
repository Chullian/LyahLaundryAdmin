package com.dynamiteam.lyahlaundryAdmin.tools.extentions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


fun ImageView.loadImage(imageUri: String, @DrawableRes placeholder: Int) {
    Glide.with(this.context).load(imageUri).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().centerCrop().placeholder(placeholder).error(placeholder)).into(this)
}

fun ImageView.loadImage(@DrawableRes drawableId: Int) {
    Glide.with(this.context).load(drawableId).into(this)
}

fun ImageView.loadImageWithError(imageUri: String, @DrawableRes placeholder: Int, @DrawableRes error: Int) {
    Glide.with(this.context).load(imageUri).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().centerCrop().placeholder(placeholder).error(error)).into(this)
}

fun ImageView.loadImageAsBitmap(imageUri: String, @DrawableRes placeholder: Int) {
    Glide.with(this.context).asBitmap().load(imageUri).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().centerCrop().placeholder(placeholder).error(placeholder)).into(this)
}

fun ImageView.loadImage(imageUri: String) {
    Glide.with(this.context).load(imageUri).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().centerCrop()).into(this)
}

fun ImageView.loadImageWithoutCrop(imageUri: String) {
    Glide.with(this.context).load(imageUri).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(this)
}

fun ImageView.loadCircularImage(imageUri: String, @DrawableRes placeholder: Int) {
    Glide.with(this.context).load("http://$imageUri").apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().placeholder(placeholder).error(placeholder).circleCrop()).into(this)
}

fun ImageView.loadCircularImage(@DrawableRes imageRes: Int) {
    Glide.with(this.context).load(imageRes).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .apply(RequestOptions().placeholder(imageRes).error(imageRes).circleCrop()).into(this)
}

