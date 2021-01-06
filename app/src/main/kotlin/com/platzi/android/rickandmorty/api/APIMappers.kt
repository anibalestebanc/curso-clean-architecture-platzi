package com.platzi.android.rickandmorty.api

import com.imagemaker.domain.Character
import com.imagemaker.domain.Episode
import com.imagemaker.domain.Location
import com.imagemaker.domain.Origin

fun CharacterResponseServer.toCharacterDomainList(): List<Character> = results.map {
    it.run{
        Character(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin.toOriginDomain(),
            location.toLocationDomain(),
            episodeList.map { episode -> "$episode/" }
        )
    }
}


fun OriginServer.toOriginDomain() = Origin(
    name,
    url
)

fun LocationServer.toLocationDomain() = Location(
    name,
    url
)

fun EpisodeServer.toEpisodeDomain() = Episode(
    id,
    name
)
