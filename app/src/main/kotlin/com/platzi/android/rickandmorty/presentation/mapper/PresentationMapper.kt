package com.platzi.android.rickandmorty.presentation.mapper

import com.imagemaker.domain.Character
import com.imagemaker.domain.Location
import com.imagemaker.domain.Origin
import com.platzi.android.rickandmorty.presentation.model.PresentationCharacter
import com.platzi.android.rickandmorty.presentation.model.PresentationLocation
import com.platzi.android.rickandmorty.presentation.model.PresentationOrigin

/**
 * Created by Anibal Cortez on 05-01-21.
 */


fun Character.toPresentationCharacter() = PresentationCharacter(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toPresentationOrigin(),
    location.toPresentationLocation(),
    episodeList
)

fun Location.toPresentationLocation() = PresentationLocation(
    name, url
)

fun Origin.toPresentationOrigin() = PresentationOrigin(
    name, url
)

fun PresentationCharacter.toDomainCharacter() = Character(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toDomainOrigin(),
    location.toDomainLocation(),
    episodeList
)

fun PresentationLocation.toDomainLocation() = Location(
    name, url
)

fun PresentationOrigin.toDomainOrigin() = Origin(
    name, url
)




