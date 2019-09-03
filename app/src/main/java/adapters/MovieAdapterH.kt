package adapters

import dao.MovieTaskApi
import dao.MovieApi
import dao.MovieMain
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bernat.oron.movieapp.R
import bernat.oron.movieapp.activitys.ActivitySplash
import com.bumptech.glide.Glide

class MovieAdapterH(val items: List<MovieMain>, val context: Context) : RecyclerView.Adapter<MovieAdapterH.ViewHolder>()  {

    var onItemClick: ((Any) -> Unit)? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_card_horizontal, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var img = ""
        when {
            items[p1] is MovieTaskApi -> img = (items[p1] as MovieTaskApi).image
            items[p1] is MovieApi -> img = "${ActivitySplash.POSTER_URL}${(items[p1] as MovieApi).poster_path}"
        }
        val image = img
                Glide.with(context)
                    .load(image)
                    .into(p0.itemImage)
        p0.itemName.text = items[p1].mtitle
        p0.itemReleaseYear.text = items[p1].mdate
        if (items[p1].mdate.count() != 4) p0.itemReleaseYear.text = items[p1].mdate.subSequence(0,4)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        // Holds the TextView that will add each animal to
        val itemImage: ImageView = view.findViewById(R.id.recycler_view_h_image_view)
        val itemName: TextView = view.findViewById(R.id.recycler_view_h_txt_name)
        val itemReleaseYear: TextView = view.findViewById(R.id.recycler_view_h_txt_location)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }


    }
}

