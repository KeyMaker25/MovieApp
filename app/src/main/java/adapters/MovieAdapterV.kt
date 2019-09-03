package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bernat.oron.movieapp.R
import dao.MovieAdd
import dao.MovieMain

class MovieAdapterV(val items: List<MovieAdd>, val context: Context) : RecyclerView.Adapter<MovieAdapterV.ViewHolder>()  {

    var onItemClick: ((Any) -> Unit)? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_card_vertical, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val cItem = items[p1]
        p0.txtTitle.text = cItem.title
        p0.txtYear.text = cItem.releaseYear
        p0.txtGenre.text = "Genres - ${cItem.genre}"
        when(cItem.rating){
            in 0.0..5.0->{
                p0.star2.visibility = View.INVISIBLE
                p0.star3.visibility = View.INVISIBLE
                p0.star4.visibility = View.INVISIBLE
                p0.star5.visibility = View.INVISIBLE
            }
            in 5.0..6.5->{
                p0.star3.visibility = View.INVISIBLE
                p0.star4.visibility = View.INVISIBLE
                p0.star5.visibility = View.INVISIBLE
            }
            in 6.5..8.5->{
                p0.star4.visibility = View.INVISIBLE
                p0.star5.visibility = View.INVISIBLE
            }
            in 8.5..9.5->{
                p0.star5.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val txtTitle : TextView = view.findViewById(R.id.card_view_V_title)
        val txtYear : TextView = view.findViewById(R.id.card_view_V_release_year)
        val txtGenre : TextView = view.findViewById(R.id.card_view_V_genre)
        val star2 : ImageView = view.findViewById(R.id.card_view_V_star2)
        val star3 : ImageView = view.findViewById(R.id.card_view_V_star3)
        val star4 : ImageView = view.findViewById(R.id.card_view_V_star4)
        val star5 : ImageView = view.findViewById(R.id.card_view_V_star5)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }


    }
}