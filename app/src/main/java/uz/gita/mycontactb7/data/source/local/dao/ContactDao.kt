package uz.gita.mycontactb7.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity

@Dao
interface ContactDao {


    @Query("SELECT * FROM contactentity")
    fun getAllContactFromLocal(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(data: ContactEntity)

    @Delete
    fun deleteContact(data: ContactEntity)
}