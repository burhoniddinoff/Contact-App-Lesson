package uz.gita.mycontactb7.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.mycontactb7.data.source.local.dao.ContactDao
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao

    companion object {
        private lateinit var instance: AppDatabase

        fun init(context: Context) {
            if (!(::instance.isInitialized))
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "MyContacts.db")
                    .allowMainThreadQueries()
                    .build()
        }

        fun getInstance() = instance
    }
}



