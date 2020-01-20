package com.github.hattamaulana.moviecatalogue.ui.catalogue

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
import kotlinx.android.synthetic.main.fragment_catalogue_wrapper.*

class CatalogueWrapperFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_catalogue_wrapper, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "LIST DB"
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_language) {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return@setOnMenuItemClickListener true
            }

            true
        }

        view_pager.adapter = TabLayoutAdapter (
            context as Context, parentFragmentManager
        ) { position -> CatalogueFragment.instance(position) }
        tabs.setupWithViewPager(view_pager)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        view_pager.adapter = null
    }
}