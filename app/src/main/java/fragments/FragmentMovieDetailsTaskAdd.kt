package fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import bernat.oron.movieapp.R
import dao.MovieAdd
import dao.MovieTaskApi
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class FragmentMovieDetailsTaskAdd : Fragment(){

    var movie1: MovieTaskApi? = null
    var movie3: MovieAdd? = null

    lateinit var titleName: TextView
    lateinit var image: ImageView
    lateinit var genre: TextView
    lateinit var releaseYear: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_details, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleName = view.findViewById(R.id.title_name)
        image = view.findViewById(R.id.image)
        genre = view.findViewById(R.id.genre)
        releaseYear = view.findViewById(R.id.release_year)
        when(arguments?.getInt("Movie")){
            1->{
                Log.i("got movie",movie1.toString())
                titleName.text = movie1?.title
                Glide.with(context!!)
                    .load(movie1?.image)
                    .into(image)
                genre.text = movie1?.genre
                releaseYear.text = movie1?.releaseYear.toString()
                movie1?.rating?.let { rating(it, view) }

            }
            3->{
                Log.i("got movie",movie3.toString())
                val btn = view.findViewById<Button>(R.id.btn_link)
                btn.visibility = View.VISIBLE
                btn.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(movie3?.link)))
                }
                titleName.text = movie3?.title
                genre.text = movie3?.genre
                releaseYear.text = movie3?.releaseYear.toString()
                movie3?.rating?.let { rating(it, view) }

            }

        }
    }

    private fun rating(rating: Double, view: View) {
        when(rating) {
            in 0.0..5.0 -> {
                view.findViewById<ImageView>(R.id.star2).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star3).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 5.0..6.5 -> {
                view.findViewById<ImageView>(R.id.star3).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 6.5..8.5 -> {
                view.findViewById<ImageView>(R.id.star4).visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
            in 8.5..9.5 -> {
                view.findViewById<ImageView>(R.id.star5).visibility = View.INVISIBLE
            }
        }
    }

}