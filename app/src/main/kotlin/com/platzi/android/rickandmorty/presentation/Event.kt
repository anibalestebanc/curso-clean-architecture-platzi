package com.platzi.android.rickandmorty.presentation


/**
 * Created by Anibal Cortez on 04-01-21.
 */
class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

}