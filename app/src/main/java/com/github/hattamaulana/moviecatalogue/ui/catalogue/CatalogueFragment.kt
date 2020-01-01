package com.github.hattamaulana.moviecatalogue.ui.catalogue


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

import com.github.hattamaulana.moviecatalogue.R
import io.github.hattamaulana.moviecatalogue.MovieModel

/**
 * A simple [Fragment] subclass.
 */
class CatalogueFragment : Fragment() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mNavArgument: NavArgument

    companion object {
        const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"
        const val ARG_DATA_MOVIE = "ARG_DATA_MOVIE"

        fun instance(index: Int): CatalogueFragment {
            val bundle = Bundle()
                bundle.putInt(ARG_SECTION_NUMBER, index)

            val fragment = CatalogueFragment()
                fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1
        mNavArgument = NavArgument.Builder()
            .setType(NavType.ParcelableArrayType(MovieModel::class.java))
            .setDefaultValue(loadMovies(arg))
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.container)
                as NavHostFragment
        val navInflater = navHostFragment.navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.mobile_navigation)
            navGraph.addArgument(ARG_DATA_MOVIE, mNavArgument)
        val navHostController = navHostFragment.navController
            navHostController.setGraph(R.navigation.mobile_navigation)
            navHostController.graph = navGraph

        setupActionBarWithNavController(activity as AppCompatActivity, navHostController)
    }

    private fun loadMovies(i: Int): Array<MovieModel> {
        val titles = resources.getStringArray (
            if (i == 0) R.array.movie_db_title else R.array.movie_db_title
        )
        val images = resources.obtainTypedArray (
            if (i == 0) R.array.movie_db_image else R.array.tv_db_images
        )
        val ratings = resources.getStringArray (
            if (i == 0) R.array.movie_db_ratings else R.array.movie_db_ratings
        )
        val releases = resources.getStringArray (
            if (i == 0) R.array.movie_db_releases else R.array.movie_db_releases
        )
        val overviews = resources.getStringArray (
            if (i == 0) R.array.movie_db_overviews else R.array.movie_db_overviews
        )
        val storylines = resources.getStringArray (
            if (i == 0) R.array.movie_db_storylines else R.array.movie_db_storylines
        )
        val genres = resources.getStringArray (
            if (i == 0) R.array.movie_db_genres else R.array.movie_db_genres
        )

        return Array(size = titles.size) {i ->
           MovieModel(titles[i], images.getResourceId(i, -1),
               releases[i].toInt(), ratings[i].toDouble(),
               overviews[i], storylines[i], genres[i]
            )
        }
    }
}
