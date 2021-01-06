package com.imagemaker.domain

data class Character (
    val id: Int,
    val name: String,
    val image: String?,
    val gender: String,
    val species: String,
    val status: String,
    val origin: Origin,
    val location: Location,
    val episodeList: List<String>
)

