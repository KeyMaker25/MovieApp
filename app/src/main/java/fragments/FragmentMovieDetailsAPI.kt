package fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import bernat.oron.movieapp.R
import bernat.oron.movieapp.activitys.ActivitySplash
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import dao.MovieApi
import org.json.JSONObject

class FragmentMovieDetailsAPI : Fragment(){

    private val BACKDROP_URL = "https://image.tmdb.org/t/p/w780"
    private val YOUTUBE_URL = "https://www.youtube.com/watch?v="
    var movie: MovieApi? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_details_api, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var youtubeAPI = ""
        Log.i("got movie",movie.toString())
        val titleName = view.findViewById<TextView>(R.id.title_name_2)
        val poster = view.findViewById<ImageView>(R.id.poster_top)
        val image = view.findViewById<ImageView>(R.id.image_logo)
        val story = view.findViewById<TextView>(R.id.story_2)
        val releaseYear = view.findViewById<TextView>(R.id.release_year_2)
        val btn = view.findViewById<TextView>(R.id.trailer_txt)
        btn.visibility = View.INVISIBLE
        var trailerUrl = YOUTUBE_URL
        movie?.let {
            youtubeAPI = "https://api.themoviedb.org/3/movie/${it.id}/videos?api_key=${ActivitySplash.apiKey}&language=en-US"
            rating(it.popularity,view)
            titleName.text = it.title
            Glide.with(context!!)
                .load("${ActivitySplash.POSTER_URL}${it.backdrop_path}")
                .into(poster)
            Glide.with(context!!)
                .load("${BACKDROP_URL}${it.poster_path}")
                .into(image)
            story.text = it.overview
            releaseYear.text = it.release_date

        }
        btn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
            startActivity(browserIntent)
        }

        val task = JsonObjectRequest(youtubeAPI,null,Response.Listener<JSONObject> {
            val key = it.getJSONArray("results")
                .getJSONObject(0)
                .getString("key")
            Log.i("key",key.toString())
            trailerUrl += key
            btn.visibility = View.VISIBLE
        },Response.ErrorListener {
            Log.e("task response", it.toString())
        })

        Volley.newRequestQueue(context).add(task)
    }

    private fun rating(rating: Double, view: View) {
        when(rating) {
            in 0.0..60.0 -> {
                view.findViewById<ImageView>(R.id.star2).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star3).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 60.0..120.5 -> {
                view.findViewById<ImageView>(R.id.star3).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 120.5..210.3 -> {
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 210.0..250.5 -> {
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
        }
    }
}