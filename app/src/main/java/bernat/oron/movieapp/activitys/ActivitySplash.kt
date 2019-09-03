package bernat.oron.movieapp.activitys

import dao.MovieTaskApi
import dao.MovieApi
import dao.MovieAdd
import dao.MovieDatabase
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import bernat.oron.movieapp.R
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONObject


class ActivitySplash : AppCompatActivity(){

    companion object {
        const val apiKey = "f801e0d11473f0b710db248ed38ba987"
        var pages = 1
        var FAVORITE_API = "https://api.themoviedb.org/3/movie/popular?page=$pages&language=en-US&api_key=$apiKey"
        const val POSTER_URL = "https://image.tmdb.org/t/p/w342"

        const val API_INIT = "https://api.androidhive.info/json/movies.json"
        lateinit var movies1: ArrayList<MovieTaskApi>
        lateinit var movies2: ArrayList<MovieApi>
        lateinit var movies3: ArrayList<MovieAdd>

    }

    lateinit var txtRrr: TextView
    lateinit var db: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        //get random favorite
        pages = (1..900).shuffled().first()

        //for no internet connection
        txtRrr = findViewById(R.id.txt_error)
        movies1 = ArrayList()
        movies2 = ArrayList()
        movies3 = ArrayList()

        //Get DataBase
        db = MovieDatabase.getInstance(applicationContext)
        initData()

    }

    private fun initData() {
        val data = db.movie1Dao().getAllMovie()
        if (data.isNullOrEmpty()){
            Log.i("DB Task","empty")
            getFromAPI(1)
        }else{
            movies1 = ArrayList(data)
            Log.i("DB Task","got ${data.size} data")
            initData2()
        }
    }

    private fun initData2(){
        val data = db.movie2Dao().getAllMovie()
        if (data.isNullOrEmpty() || data.count() < 10){
            Log.i("DB Api","empty")
            getFromAPI(2)
        }else {
            movies2 = ArrayList(data)
            Log.i("DB Api","got ${data.size} data")
            initData3()
        }
    }

    private fun initData3(){
        val data = db.movie3Dao().getAllMovie()
        Log.i("DB Scanned Movies", data.toString())
        movies3 = ArrayList(data)
        this.runOnUiThread {
            android.os.Handler().postDelayed({
                startActivity(Intent(this,ActivityMain::class.java))
            },1500)
        }
    }

    private fun getFromAPI(q: Int) {
        when(q){
            1 ->{
                val task = JsonArrayRequest(API_INIT,Response.Listener<JSONArray> {
                    for (i in 0 until it.length()){
                        if (it[i] != null){
                            val obj = it[i] as JSONObject
                            val temp = obj["genre"] as JSONArray
                            var str = ""
                            for (t in 0 until temp.length()){
                                str += temp[t].toString().plus(" ")
                            }
                            var rate: Number? = obj["rating"] as? Double
                            if (rate == null) {
                                rate = obj["rating"] as Int
                            }
                            val m = MovieTaskApi(null,
                                obj["title"] as String,
                                obj["image"] as String,
                                rate.toDouble(),
                                obj["releaseYear"] as Int,
                                str
                            )
                            Log.i("json", "got movie ${m.title}")
                            db.movie1Dao().insertMovie(m)
                        }
                    }
                    movies1 = db.movie1Dao().getAllMovie() as ArrayList<MovieTaskApi>
                    Log.i("from Json 1", "Got ${movies1.size} movies")
                    initData2()
                },Response.ErrorListener {
                    Log.e("it - json obj","$it")
                    txtRrr.text = getString(R.string.internet_error)
                    txtRrr.visibility = View.VISIBLE
                })
                Volley.newRequestQueue(applicationContext).add(task)
            }
            2 ->{
                val task = JsonObjectRequest(FAVORITE_API,null,Response.Listener<JSONObject> {
                    val objArray = it.get("results") as JSONArray
                    for (i in 0 until objArray.length()){
                        if (objArray[i] != null){
                            val obj = objArray[i] as JSONObject
                            val m = MovieApi(
                                null,
                                obj["id"] as Int,
                                obj["title"] as String,
                                obj["popularity"] as Double,
                                obj["vote_count"] as Int,
                                obj["poster_path"] as String,
                                obj["backdrop_path"] as String,
                                obj["overview"] as String,
                                obj["release_date"] as String
                            )
                            Log.i("json", "got movie ${m.title}")
                            db.movie2Dao().insertMovie(m)
                            movies2.add(m)
                        }
                    }
                    Log.i("from Json 2", "Got ${movies2.size} movies")
                    initData3()
                },Response.ErrorListener {
                    Log.e("it - json objFavorite","$it")
                    txtRrr.text = getString(R.string.internet_error)
                    txtRrr.visibility = View.VISIBLE
                })
                Volley.newRequestQueue(applicationContext).add(task)
            }
        }

    }

}
