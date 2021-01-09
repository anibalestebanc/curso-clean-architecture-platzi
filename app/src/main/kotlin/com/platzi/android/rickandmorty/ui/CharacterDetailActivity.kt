package com.platzi.android.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.imagemaker.domain.Character
import com.imagemaker.imagemanager.bindCircularImageUrl
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.EpisodeListAdapter
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.di.CharacterDetailComponent
import com.platzi.android.rickandmorty.di.CharacterDetailModule
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.presentation.mapper.toDomainCharacter
import com.platzi.android.rickandmorty.presentation.model.PresentationCharacter
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.app
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {


    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding
    private var character : Character? = null

    private lateinit var characterDetailComponent : CharacterDetailComponent

    private val viewModel: CharacterDetailViewModel by lazy {
       getViewModel { characterDetailComponent.characterDetailViewModel }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterDetailComponent = app.component.inject(CharacterDetailModule())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        character = intent?.getParcelableExtra<PresentationCharacter>(Constants.EXTRA_CHARACTER)?.toDomainCharacter()

        if (character == null) {
            this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
            finish()
            return
        }



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
