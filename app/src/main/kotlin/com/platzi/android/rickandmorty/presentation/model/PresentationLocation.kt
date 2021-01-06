package com.platzi.android.rickandmorty.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anibal Cortez on 05-01-21.
 */
@Parcelize
data class PresentationLocation (
    val name :String,
    val url : String
) : Parcelable
