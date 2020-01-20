package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.*

class FavoriteWrapperFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorite_wrapper, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Setup Toolbar title */
        toolbar.title = "FAVORITES"
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_language) {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return@setOnMenuItemClickListener true
            }

            true
        }

        view_pager_favorite.adapter = TabLayoutAdapter(
            context as Context, parentFragmentManager
        ) { position -> FavoriteFragment.instance(position)}
        tabs_favorite.setupWithViewPager(view_pager_favorite)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        view_pager_favorite.adapter = null
    }
}