package com.github.hattamaulana.moviecatalogue.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.hattamaulana.moviecatalogue.R

class TabLayoutAdapter(
    private val mContext: Context,
    fragmentManager: FragmentManager,
    private val fragment: (position: Int) -> Fragment
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_movie, R.string.tab_tv_series)

    override fun getItem(position: Int): Fragment = fragment(position)

    override fun getCount(): Int = TAB_TITLES.size

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(
        TAB_TITLES[position]
    )
}