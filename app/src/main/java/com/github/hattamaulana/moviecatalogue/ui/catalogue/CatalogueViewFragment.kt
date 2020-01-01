package com.github.hattamaulana.moviecatalogue.ui.catalogue


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.github.hattamaulana.moviecatalogue.R
import com.google.android.material.tabs.TabLayout
import io.github.hattamaulana.moviecatalogue.MovieModel
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * A simple [Fragment] subclass.
 */
class CatalogueViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs = activity?.findViewById<TabLayout>(R.id.tabs)
        if (tabs?.visibility == View.GONE)
            tabs.visibility = View.VISIBLE

        val args = arguments
            ?.getParcelableArray(CatalogueFragment.ARG_DATA_MOVIE) as Array<MovieModel>

        val adapter = CatalogueAdapter(view.context)
            adapter.movies = args.toCollection(ArrayList())
            adapter.setOnItemClckCallback(object : CatalogueAdapter.OnItemClickCallback{
                override fun onItemClicked(movie: MovieModel) {
                    val directions =
                        CatalogueViewFragmentDirections.actionMoviesFragmentToMovieFragment(movie)
                        directions.movie = movie

                    findNavController().navigate(directions)
                }
            })

        val layout  = LinearLayoutManager(view.context)
            layout.orientation = LinearLayoutManager.VERTICAL

        recycler_movies.layoutManager = layout
        recycler_movies.adapter = adapter
    }
}
