package com.imagemaker.data.repository

import com.imagemaker.data.sources.EpisodeRemoteDataSource

/**
 * Created by Anibal Cortez on 06-01-21.
 */
class EpisodeRepository(private val remoteDataSource: EpisodeRemoteDataSource) {

    fun getEpisodeFromCharacter(episodeUrlList: List<String>) =
        remoteDataSource.getEpisodeFromCharacter(episodeUrlList)
}