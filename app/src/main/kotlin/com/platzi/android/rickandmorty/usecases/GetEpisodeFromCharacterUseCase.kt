package com.platzi.android.rickandmorty.usecases

import com.imagemaker.domain.Episode
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.api.EpisodeServer
import com.platzi.android.rickandmorty.api.EpisodeService
import com.platzi.android.rickandmorty.api.toEpisodeDomain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetEpisodeFromCharacterUseCase(private val episodeRequest: EpisodeRequest) {
    fun invoke(episodeUrlList : List<String>) : Single<List<Episode>> =  Observable.fromIterable(episodeUrlList)
        .flatMap { episode: String ->
            episodeRequest.baseUrl = episode
            episodeRequest
                .getService<EpisodeService>()
                .getEpisode()
                .map(EpisodeServer::toEpisodeDomain)
                .toObservable()
        }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}