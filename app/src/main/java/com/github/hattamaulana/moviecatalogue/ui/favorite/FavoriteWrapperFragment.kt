package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.MainViewModel
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import com.github.hattamaulana.moviecatalogue.utils.TabChangeListener
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.*

private var state: Int = 0

class FavoriteWrapperFragment : Fragment() {

    private lateinit var category: Array<String>

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_favorite_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()

        category = resources.getStringArray(R.array.category)
        viewModel.loadFavorites(category[state])
        view_pager_favorite.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            FavoriteFragment.instance(it)
        }

        view_pager_favorite.setCurrentItem(state, true)
        view_pager_favorite.addOnPageChangeListener(TabChangeListener { position ->
            state = position
            setTitle()
            viewModel.loadFavorites(category[state])
        })

        tabs.setupWithViewPager(view_pager_favorite)
    }

    private fun setTitle() {
        val title = resources.getStringArray(R.array.title_toolbar)
        toolbar.title = "Favorite ${title[state]}"
    }
}