package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagemaker.domain.Character
import com.platzi.android.rickandmorty.presentation.util.Event
import com.platzi.android.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Anibal Cortez on 04-01-21.
 */
class FavoriteListViewModel(
    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
) : ViewModel() {

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<Character>) :
            FavoriteListNavigation()

        object ShowEmptyList : FavoriteListNavigation()
    }

    private val disposable = CompositeDisposable()

    private val _model = MutableLiveData<Event<FavoriteListNavigation>>()
    val model: LiveData<Event<FavoriteListNavigation>> get() = _model

    private val _characterFavoriteList: LiveData<List<Character>>
        get() = LiveDataReactiveStreams.fromPublisher(
            getAllFavoriteCharactersUseCase.invoke()
        )
    val characterFavoriteList: LiveData<List<Character>>
        get() = _characterFavoriteList

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onFavoriteCharacterList(list : List<Character>){
        if (list.isEmpty()){
            _model.value = Event(FavoriteListNavigation.ShowEmptyList)
            return
        }

        _model.value = Event(FavoriteListNavigation.ShowCharacterList(list))
    }

}