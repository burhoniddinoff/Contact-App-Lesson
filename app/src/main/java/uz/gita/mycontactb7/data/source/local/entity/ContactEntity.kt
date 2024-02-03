package uz.gita.mycontactb7.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.mycontactb7.data.model.StatusEnum

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val remoteID: Int,  //  ADD face, EDIT or delete real ID
    val firstName: String,
    val lastName: String,
    val phone:String,
    val statusCode: Int
)
