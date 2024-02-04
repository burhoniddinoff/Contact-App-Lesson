package uz.gita.mycontactb7.data.source.remote.request

data class CreateContactRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
)