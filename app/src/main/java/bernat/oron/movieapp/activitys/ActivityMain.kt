package bernat.oron.movieapp.activitys

import dao.MovieTaskApi
import dao.MovieApi
import dao.MovieAdd
import dao.MovieDatabase
import adapters.MovieAdapterH
import adapters.MovieAdapterV
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bernat.oron.movieapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import androidx.core.content.ContextCompat
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import fragments.FragmentMovieDetailsAPI
import fragments.FragmentMovieDetailsTaskAdd
import org.json.JSONArray
import org.json.JSONObject


class ActivityMain : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var btnAdd: FloatingActionButton
    lateinit var recyclerTaskData: RecyclerView
    lateinit var recyclerAPIFavorite: RecyclerView
    lateinit var recyclerAddedMovies: RecyclerView
    lateinit var qrCodeScanner : ZXingScannerView
    lateinit var btnAddTxt: TextView
    lateinit var txtList: TextView
    lateinit var txtMost: TextView
    lateinit var txtSave: TextView
    lateinit var fm: FragmentManager
    var frag = FragmentMovieDetailsTaskAdd()
    var fragAPI = FragmentMovieDetailsAPI()
    private var isCameraOn = false

    companion object{
        private const val REQUEST_CAMERA = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm = supportFragmentManager

        btnAdd = findViewById(R.id.btn_add)
        btnAddTxt = findViewById(R.id.btn_add_txt)
        recyclerTaskData = findViewById(R.id.recycler_1)
        recyclerAPIFavorite = findViewById(R.id.recycler_2)
        recyclerAddedMovies = findViewById(R.id.recycler_3)
        txtMost = findViewById(R.id.txt_most)
        txtList = findViewById(R.id.txt_list)
        txtSave = findViewById(R.id.txt_movies_qr)

        initFloatingBtn()
        initRecyclerTaskData()
        initRecyclerApi()
        initRecyclerAdded()
    }

    private fun initFloatingBtn() {
        qrCodeScanner = findViewById(R.id.qrCodeScanner)
        btnAdd.setOnClickListener {
            if (!checkPermission()) requestPermission()
            qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE))
            qrCodeScanner.setLaserColor(R.color.colorPrimaryTextYellow)
            qrCodeScanner.setMaskColor(R.color.colorAccent)
            qrCodeScanner.setResultHandler(this)
            qrCodeScanner.visibility = View.VISIBLE
            qrCodeScanner.startCamera()
            isCameraOn = true
            hideLayouts()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun hideLayouts(){
        recyclerAPIFavorite.visibility = View.INVISIBLE
        recyclerAddedMovies.visibility = View.GONE
        recyclerTaskData.visibility = View.INVISIBLE
        btnAddTxt.visibility = View.INVISIBLE
        txtSave.visibility = View.GONE
        txtMost.visibility = View.INVISIBLE
        txtList.visibility = View.INVISIBLE
        btnAdd.visibility = FloatingActionButton.INVISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun showLayouts(){
        recyclerAPIFavorite.visibility = View.VISIBLE
        recyclerTaskData.visibility = View.VISIBLE
        btnAddTxt.visibility = View.VISIBLE
        txtMost.visibility = View.VISIBLE
        txtList.visibility = View.VISIBLE
        btnAdd.visibility = FloatingActionButton.VISIBLE
        if (ActivitySplash.movies3.isNotEmpty()) {
            Log.i("movie3", ActivitySplash.movies3.toString())
            txtSave.visibility = View.VISIBLE
            recyclerAddedMovies.visibility = View.VISIBLE
            recyclerAddedMovies.adapter?.notifyDataSetChanged()
        }
    }

    override fun handleResult(p0: Result?) {
        qrCodeScanner.stopCamera()
        qrCodeScanner.visibility = View.INVISIBLE
        isCameraOn = false
        Log.i("QR Code results", p0.toString())
        showLayouts()
        val obj = JSONObject(p0.toString())
        val genreArr = obj["genre"] as JSONArray
        var genre = ""
        for( i in 0 until genreArr.length()){
            genre += " "+genreArr[i]
        }
        val movie = MovieAdd(
            null,
            obj["title"] as String,
            obj["image"] as String,
            obj["rating"] as Double,
            (obj["releaseYear"] as Int).toString(),
            genre
        )
        AsyncTask.execute {
            val db = MovieDatabase.getInstance(applicationContext)
            //check if exist
            val r = db.movie3Dao().getByTitle(movie.title)
            if (r == null){
                Log.i("Dao insert", "YES "+db.movie3Dao().insertMovie(movie).toString())
                ActivitySplash.movies3.add(movie)
                this.runOnUiThread {
                    recyclerAddedMovies.adapter?.notifyDataSetChanged()
                    recyclerAddedMovies.visibility = View.VISIBLE
                    txtSave.visibility = View.VISIBLE
                }
            }else{
                Log.i("Dao insert","NO exist")
                Snackbar.make(findViewById(R.id.main_container),"${r.title} already exist in the Database",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext,  Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA -> if (grantResults.count() > 0) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted) {
                    btnAdd.callOnClick()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Can't add with no camera permission",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            showMessage()
                            return
                        }
                    }
                }
            }
        }

    }

    private fun showMessage() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage("To scan QR code we need your permission to use you camera, OK ?")
            .setPositiveButton("OK") { _, _->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                { requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA) }
            }
            .setNegativeButton("Cancel") { _, _-> showLayouts()}
            .create()
            .show()
    }

    private fun initRecyclerTaskData(){
        recyclerTaskData.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerTaskData.adapter = MovieAdapterH(ActivitySplash.movies1, this)
        (recyclerTaskData.adapter as MovieAdapterH).onItemClick = {
                it ->
            addFragment(it,1)
        }
    }

    private fun initRecyclerApi(){
        recyclerAPIFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerAPIFavorite.adapter = MovieAdapterH(ActivitySplash.movies2, this)
        (recyclerAPIFavorite.adapter as MovieAdapterH).onItemClick = {
                it ->
            addFragment(it,2)
        }
    }

    private fun initRecyclerAdded() {
        if (ActivitySplash.movies3.isNullOrEmpty()) {
            txtSave.visibility = View.GONE
            recyclerAddedMovies.visibility = View.GONE
        }
        recyclerAddedMovies.adapter = MovieAdapterV(ActivitySplash.movies3,this)
        (recyclerAddedMovies.adapter as MovieAdapterV).onItemClick = {
                it ->
            addFragment(it,3)
        }
        recyclerAddedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun addFragment(data: Any, i: Int) {
        android.os.Handler().postDelayed({
            hideLayouts()
        },500)
        val b = Bundle()
        val ft = fm.beginTransaction()
        ft.setCustomAnimations(R.anim.slide_left_to_right,R.anim.silde_right_to_left)
        when(i) {
            1 -> {
                frag = FragmentMovieDetailsTaskAdd()
                ft.replace(R.id.fragment_container,frag)
                frag.movie1 = data as MovieTaskApi
                b.putInt("Movie",i)
                frag.arguments = b
                ft.show(frag)
            }
            2 -> {
                fragAPI = FragmentMovieDetailsAPI()
                ft.replace(R.id.fragment_container,fragAPI)
                fragAPI.movie = data as MovieApi
                ft.show(fragAPI)
            }
            3 -> {
                frag = FragmentMovieDetailsTaskAdd()
                ft.replace(R.id.fragment_container,frag)
                frag.movie3 = data as MovieAdd
                b.putInt("Movie",i)
                frag.arguments = b
                ft.show(frag)
            }
        }
        ft.commit()
    }

    override fun onBackPressed() {
        if (isCameraOn){
            qrCodeScanner.stopCamera()
            qrCodeScanner.visibility = View.INVISIBLE
            showLayouts()
        }else{
            if (frag.isAdded || fragAPI.isAdded){
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(R.anim.slide_left_to_right,R.anim.silde_right_to_left)
            if (frag.isAdded) ft.hide(frag)
            if (fragAPI.isAdded) ft.hide(fragAPI)
                ft.commit()
                android.os.Handler().postDelayed({
                    showLayouts()
                },700)
                return
            }
            finishAffinity()
        }
    }

}