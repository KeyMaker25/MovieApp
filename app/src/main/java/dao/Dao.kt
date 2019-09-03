package dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [MovieTaskApi::class, MovieApi::class,MovieAdd::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase(){

    abstract fun movie1Dao(): DaoMovie1Interface
    abstract fun movie2Dao(): DaoMovie2Interface
    abstract fun movie3Dao(): DaoMovie3Interface

    companion object{

        var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "Movies.db"
                ).allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MovieDatabase
        }
    }
}