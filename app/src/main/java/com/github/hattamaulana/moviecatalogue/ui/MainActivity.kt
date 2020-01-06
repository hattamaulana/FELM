package com.github.hattamaulana.moviecatalogue.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.github.hattamaulana.moviecatalogue.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0f

        view_pager.adapter = MainTabLayoutAdapter(this, supportFragmentManager)
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
}
