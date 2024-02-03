package uz.gita.mycontactb7.domain

import uz.gita.mycontactb7.data.model.ContactUIData

interface ContactRepository {
    fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit)
    fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit, )
    fun syncWithServer(finishBlock: () -> Unit, errorBlock:(String) -> Unit)
}