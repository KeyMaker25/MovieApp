package dao
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Movie1")
data class MovieTaskApi(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "releaseYear") val releaseYear: Int,
    @ColumnInfo(name = "genre") val genre: String?
): MovieMain(title,releaseYear.toString(),image)


@Entity(tableName = "Movie2")
data class MovieApi(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "vote_count") val vote_count: Int,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "release_date") val release_date: String
): MovieMain(title,release_date, poster_path)

@Entity(tableName = "Movie3")
data class MovieAdd(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "link") val link: String,
        @ColumnInfo(name = "rating") val rating: Double,
        @ColumnInfo(name = "releaseYear") val releaseYear: String,
        @ColumnInfo(name = "genre") val genre: String
): MovieMain(title,releaseYear, link)

open class MovieMain(var mtitle: String, var mdate: String, var mimg: String)
