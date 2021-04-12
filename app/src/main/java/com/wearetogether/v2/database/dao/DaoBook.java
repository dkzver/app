package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Book;
import com.wearetogether.v2.database.model.Place;

import java.util.List;

@Dao
public interface DaoBook {
    @Query("SELECT p.* FROM books b, places p where b.item_unic = p.unic")
    List<Place> getAll();

    @Insert
    void insert(Book item);

    @Update
    void update(Book item);

    @Delete
    void delete(Book item);

    @Query("DELETE FROM books WHERE item_unic=:unic")
    void deleteBy(long unic);

    @Query("SELECT * FROM books WHERE item_unic=:unic")
    Book get(Long unic);
}
