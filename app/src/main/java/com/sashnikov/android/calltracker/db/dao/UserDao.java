package com.sashnikov.android.calltracker.db.dao;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.sashnikov.android.calltracker.model.User;

/**
 * @author Ilya_Sashnikau
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public Long insertUser(User user);

    @Update
    public void updateUser(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    public LiveData<User> getLiveById(long id);

    @Query("SELECT * FROM user WHERE id = :id")
    public User getById(long id);

    @Query("SELECT * FROM user")
    public List<User> getAllUsers();
}
