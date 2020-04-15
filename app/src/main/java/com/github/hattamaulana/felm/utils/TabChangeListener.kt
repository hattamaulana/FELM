package com.github.hattamaulana.felm.utils

import androidx.viewpager.widget.ViewPager

class TabChangeListener(private val onChange: (Int)-> Unit) : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {
        onChange(position)
    }
}