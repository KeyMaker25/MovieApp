package dao

import androidx.room.*

@Dao
interface DaoMovie1Interface{

    @Query("SELECT * FROM Movie1 ORDER BY releaseYear DESC")
    fun getAllMovie(): List<MovieTaskApi>

    @Query("SELECT * FROM Movie1 WHERE title = :name")
    fun getByTitle(name: String): MovieTaskApi

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(user: MovieTaskApi)

    @Delete
    fun deleteMovie(movie: MovieTaskApi)
}

@Dao
interface DaoMovie2Interface{

    @Query("SELECT * FROM Movie2")
    fun getAllMovie(): List<MovieApi>

    @Query("SELECT * FROM Movie2 WHERE title = :name")
    fun getByTitle(name: String): MovieApi

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(user: MovieApi)

    @Delete
    fun deleteMovie(movie: MovieApi)

}

@Dao
interface DaoMovie3Interface{

    @Query("SELECT * FROM Movie3")
    fun getAllMovie(): List<MovieAdd>

    @Query("SELECT * FROM Movie3 WHERE title = :name")
    fun getByTitle(name: String): MovieAdd?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovie(user: MovieAdd): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: MovieAdd): Int?

    @Delete
    fun deleteMovie(movie: MovieAdd)

}



