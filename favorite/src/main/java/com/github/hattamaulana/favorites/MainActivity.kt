package com.github.hattamaulana.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.github.hattamaulana.favorites.ui.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.activity_main.*

private var viewStatePosition: Int = 0

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.elevation = 0F

        view_pager.adapter = FavoriteFragment.PagerAdapter(this, supportFragmentManager)
        view_pager.setCurrentItem(viewStatePosition, true)
        view_pager.addOnPageChangeListener(this)

        tabs.setupWithViewPager(view_pager)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewStatePosition = position
    }
}
