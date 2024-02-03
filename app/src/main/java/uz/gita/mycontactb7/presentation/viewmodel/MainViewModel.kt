package uz.gita.mycontactb7.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.gita.mycontactb7.data.model.ContactUIData

interface MainViewModel {
    val progressLiveData: LiveData<Boolean>
    val contactsLiveData: LiveData<List<ContactUIData>>
    val errorMessageLiveData: LiveData<String>
    val notConnectionLiveData: LiveData<Unit>
    val emptyStateLiveData: LiveData<Unit>

    val openAddContactScreenLiveData :LiveData<Unit>

    fun loadContacts()
    fun openAddContactScreen()
}

