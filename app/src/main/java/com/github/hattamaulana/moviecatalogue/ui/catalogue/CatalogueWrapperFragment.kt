package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.hattamaulana.moviecatalogue.R
import kotlinx.android.synthetic.main.fragment_catalogue_wrapper.*

class CatalogueWrapperFragment : Fragment() {

    private val TAG = this.javaClass.name

    private inner class TabLayoutAdapter(
        private val mContext: Context,
        fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_movie, R.string.tab_tv_series)

        override fun getItem(position: Int): Fragment = CatalogueFragment.instance(position)

        override fun getCount(): Int = TAB_TITLES.size

        override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(
            TAB_TITLES[position]
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_catalogue_wrapper, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ;")
        view_pager.adapter = TabLayoutAdapter(
            context as Context, parentFragmentManager
        )

        tabs.setupWithViewPager(view_pager)

        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_language) {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return@setOnMenuItemClickListener true
            }

            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        view_pager.adapter = null
    }
}