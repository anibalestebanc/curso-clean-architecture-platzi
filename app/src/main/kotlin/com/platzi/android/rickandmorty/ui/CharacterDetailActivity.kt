package com.platzi.android.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.EpisodeListAdapter
import com.platzi.android.rickandmorty.api.APIConstants.BASE_API_URL
import com.platzi.android.rickandmorty.api.CharacterServer
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.usecases.GetFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.bindCircularImageUrl
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {


    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var episodeRequest: EpisodeRequest
    private lateinit var characterDao: CharacterDao

    private var character: CharacterServer? = null

    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterDao)
    }
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRequest)
    }

    private val getCharacterByIdUseCase: GetFavoriteCharacterStatusUseCase by lazy {
        GetFavoriteCharacterStatusUseCase(characterDao)
    }

    private val viewModel: CharacterDetailViewModel by lazy {
        CharacterDetailViewModel(
            updateFavoriteCharacterStatusUseCase,
            getCharacterByIdUseCase,
            getEpisodeFromCharacterUseCase
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        character = intent.getParcelableExtra(Constants.EXTRA_CHARACTER)
        if (character == null) {
            this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
            finish()
            return
        }

        episodeRequest = EpisodeRequest(BASE_API_URL)
        characterDao = CharacterDatabase.getDatabase(application).characterDao()


        viewModel.model.observe(this, Observer { events ->
            events?.getContentIfNotHandled()?.let { navigation ->
                when (navigation) {
                    CharacterDetailViewModel.CharacterDetailNavigation.ShowProgressBar -> {
                        episodeProgressBar.isVisible = true
                    }
                    CharacterDetailViewModel.CharacterDetailNavigation.HideProgressBar -> {
                        episodeProgressBar.isVisible = false
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.UpdateFavoriteIcon -> {
                        updateFavoriteIcon(navigation.isFavorite)
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeList -> {
                        episodeListAdapter.updateData(navigation.episodeList)
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeError -> {
                        this@CharacterDetailActivity.showLongToast("Error -> ${navigation.error.message}")
                    }
                }

            }

        })


        viewModel.onValidateFavoriteCharacterStatus(character!!)

        binding.characterImage.bindCircularImageUrl(
            url = character!!.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character!!.name
        binding.characterDataStatus = character!!.status
        binding.characterDataSpecies = character!!.species
        binding.characterDataGender = character!!.gender
        binding.characterDataOriginName = character!!.origin.name
        binding.characterDataLocationName = character!!.location.name

        viewModel.onShowEpisodeList(character!!.episodeList)

        characterFavorite.setOnClickListener {
            viewModel.onUpdateFavoriteCharacterStatus(character!!)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun updateFavoriteIcon(isFavorite: Boolean?) {
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

}
