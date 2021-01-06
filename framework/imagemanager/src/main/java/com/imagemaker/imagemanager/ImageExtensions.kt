package com.imagemaker.imagemanager

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

/**
 * Created by Anibal Cortez on 06-01-21.
 */

fun ImageView.bindCircularImageUrl(url: String?, @DrawableRes placeholder: Int,
                                   @DrawableRes errorPlaceholder: Int) {
    if (url.isNullOrBlank()) {
        setImageResource(placeholder)
        return
    }

    Glide.with(context)
        .load(url)
        .error(errorPlaceholder)
        .placeholder(placeholder)
        .circleCrop()
        .into(this)
}

fun ImageView.bindImageUrl(url: String?, @DrawableRes placeholder: Int,
                           @DrawableRes errorPlaceholder: Int) {
    if (url.isNullOrBlank()) {
        setImageResource(placeholder)
        return
    }

    Glide.with(context)
        .load(url)
        .error(errorPlaceholder)
        .placeholder(placeholder)
        .into(this)
}