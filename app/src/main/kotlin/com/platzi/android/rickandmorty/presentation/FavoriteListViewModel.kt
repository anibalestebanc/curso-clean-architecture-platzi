package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.presentation.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 04-01-21.
 */
class FavoriteListViewModel(
    private val characterDao: CharacterDao
) : ViewModel() {

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<CharacterEntity>) :
            FavoriteListNavigation()

        object ShowEmptyList : FavoriteListNavigation()
    }

    private val disposable = CompositeDisposable()

    private val _model = MutableLiveData<Event<FavoriteListNavigation>>()
    val model: LiveData<Event<FavoriteListNavigation>> get() = _model

    private val _characterFavoriteList: LiveData<List<CharacterEntity>>
        get() = LiveDataReactiveStreams.fromPublisher(
            characterDao
                .getAllFavoriteCharacters()
                .onErrorReturn { emptyList() }
                .subscribeOn(Schedulers.io())
        )
    val characterFavoriteList: LiveData<List<CharacterEntity>>
        get() = _characterFavoriteList

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onFavoriteCharacterList(list : List<CharacterEntity>){
        if (list.isEmpty()){
            _model.value = Event(FavoriteListNavigation.ShowEmptyList)
            return
        }

        _model.value = Event(FavoriteListNavigation.ShowCharacterList(list))
    }

    /**
     *
    disposable.add(
    characterDao.getAllFavoriteCharacters()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
    .subscribe({ characterList ->
    if(characterList.isEmpty()) {
    tvEmptyListMessage.isVisible = true
    favoriteListAdapter.updateData(emptyList())
    } else {
    tvEmptyListMessage.isVisible = false
    favoriteListAdapter.updateData(characterList)
    }
    },{
    tvEmptyListMessage.isVisible = true
    favoriteListAdapter.updateData(emptyList())
    })
    )
     */


}