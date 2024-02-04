package uz.gita.mycontactb7.data.model

data class ContactUIData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val status: StatusEnum,
)