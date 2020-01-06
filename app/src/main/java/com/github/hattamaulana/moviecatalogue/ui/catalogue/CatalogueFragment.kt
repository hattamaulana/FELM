package com.github.hattamaulana.moviecatalogue.ui.catalogue


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import io.github.hattamaulana.moviecatalogue.MovieModel
import kotlinx.android.synthetic.main.fragment_catalogue.*

/**
 * A simple [Fragment] subclass.
 */
class CatalogueFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1
        val adapter = CatalogueAdapter(view.context)
        adapter.movies = loadMovies(arg)
        adapter.setOnItemClckCallback(object : CatalogueAdapter.OnItemClickCallback {
            override fun onItemClicked(movie: MovieModel) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(ARG_DATA_MOVIE, movie)

                startActivity(intent)
            }
        })
        val layout = LinearLayoutManager(view.context)
        layout.orientation = LinearLayoutManager.VERTICAL

        recycler_movies.layoutManager = layout
        recycler_movies.adapter = adapter
    }

    private fun loadMovies(i: Int): ArrayList<MovieModel> {
        val titles = resources.getStringArray(
            if (i == 0) R.array.movie_db_title else R.array.tv_db_title
        )
        val images = resources.obtainTypedArray(
            if (i == 0) R.array.movie_db_image else R.array.tv_db_images
        )
        val ratings = resources.getStringArray(
            if (i == 0) R.array.movie_db_ratings else R.array.tv_db_ratings
        )
        val releases = resources.getStringArray(
            if (i == 0) R.array.movie_db_releases else R.array.tv_db_releases
        )
        val overviews = resources.getStringArray(
            if (i == 0) R.array.movie_db_overviews else R.array.tv_db_overviews
        )
        val storylines = resources.getStringArray(
            if (i == 0) R.array.movie_db_storylines else R.array.tv_db_storylines
        )
        val genres = resources.getStringArray(
            if (i == 0) R.array.movie_db_genres else R.array.tv_db_genres
        )

        val movies = Array(size = titles.size) { i ->
            MovieModel(
                titles[i], images.getResourceId(i, -1),
                releases[i], ratings[i].toDouble(),
                overviews[i], storylines[i], genres[i]
            )
        }

        return movies.toCollection(ArrayList())
    }
}
