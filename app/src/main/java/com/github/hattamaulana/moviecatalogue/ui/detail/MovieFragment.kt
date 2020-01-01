package com.github.hattamaulana.moviecatalogue.ui.detail


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import com.github.hattamaulana.moviecatalogue.R
import com.google.android.material.tabs.TabLayout
import io.github.hattamaulana.moviecatalogue.MovieModel
import kotlinx.android.synthetic.main.adapter_movie.*
import kotlinx.android.synthetic.main.adapter_movie.img_movie
import kotlinx.android.synthetic.main.adapter_movie.txt_genre
import kotlinx.android.synthetic.main.adapter_movie.txt_overview
import kotlinx.android.synthetic.main.adapter_movie.txt_rating
import kotlinx.android.synthetic.main.adapter_movie.txt_title
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs = activity?.findViewById<TabLayout>(R.id.tabs)
            tabs?.visibility = View.GONE

        val movie = MovieFragmentArgs
            .fromBundle(arguments as Bundle)
            .movie

        img_movie.setImageResource(movie.img)

        txt_genre.text    = movie.genres
        txt_title.text    = movie.title
        txt_rating.text   = movie.rating.toString()
        txt_release.text  = movie.release.toString()
        txt_overview.text = movie.overview
    }
}
