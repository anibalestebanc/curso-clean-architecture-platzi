package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.api.EpisodeService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetEpisodeFromCharacterUseCase(private val episodeRequest: EpisodeRequest) {
    fun invoke(episodeUrlList : List<String>) =  Observable.fromIterable(episodeUrlList)
        .flatMap { episode: String ->
            episodeRequest.baseUrl = episode
            episodeRequest
                .getService<EpisodeService>()
                .getEpisode()
                .toObservable()
        }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}