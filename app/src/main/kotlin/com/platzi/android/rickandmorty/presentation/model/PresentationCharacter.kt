package com.platzi.android.rickandmorty.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anibal Cortez on 05-01-21.
 */
@Parcelize
data class PresentationCharacter (
    val id: Int,
    val name: String,
    val image: String?,
    val gender: String,
    val species: String,
    val status: String,
    val origin: PresentationOrigin,
    val location: PresentationLocation,
    val episodeList: List<String>
) : Parcelable
