package com.github.hattamaulana.felm.ui.favorite

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.databinding.FragmentFavoriteWrapperBinding
import com.github.hattamaulana.felm.ui.MainViewModel
import com.github.hattamaulana.felm.ui.TabLayoutAdapter
import com.github.hattamaulana.felm.utils.TabChangeListener

private var state: Int = 0

class FavoriteWrapperFragment : BaseFragment<FragmentFavoriteWrapperBinding>(
    FragmentFavoriteWrapperBinding::inflate
) {

    private lateinit var category: Array<String>

    private val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentFavoriteWrapperBinding) = with(binding) {
        setTitle()

        category = resources.getStringArray(R.array.category)
        viewModel.loadFavorites(category[state])
        viewPagerFavorite.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            FavoriteFragment.instance(it)
        }

        viewPagerFavorite.setCurrentItem(state, true)
        viewPagerFavorite.addOnPageChangeListener(TabChangeListener { position ->
            state = position
            setTitle()
            viewModel.loadFavorites(category[state])
        })

        tabs.setupWithViewPager(viewPagerFavorite)
    }

    override fun initData() {
    }

    private fun setTitle() {
        val title = resources.getStringArray(R.array.title_toolbar)

        binding?.toolbar?.title = "Favorite ${title[state]}"
    }
}