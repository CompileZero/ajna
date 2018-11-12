package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MyImmediateContactsDao {

    @Insert
    void insert(MyImmediateContactsEntity myImmediateContactsEntity);


    @Delete
    void delete(MyImmediateContactsEntity myImmediateContactsEntity);


    @Query("SELECT * FROM my_immediate_contacts_table ORDER BY id ASC")
    LiveData<List<MyImmediateContactsEntity>> getAllImmediateContacts();
}
