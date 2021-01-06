package com.platzi.android.rickandmorty.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.platzi.android.rickandmorty.ui.CharacterListFragment
import com.platzi.android.rickandmorty.ui.FavoriteListFragment

class HomeNavigationStateAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {


    private val homeStateFragmentList: List<Fragment> = listOf(
        CharacterListFragment.newInstance(),
        FavoriteListFragment.newInstance()
    )

    override fun getItemCount(): Int = homeStateFragmentList.size

    override fun createFragment(position: Int): Fragment = homeStateFragmentList[position]

}
