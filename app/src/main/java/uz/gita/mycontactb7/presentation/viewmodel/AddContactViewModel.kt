package uz.gita.mycontactb7.presentation.viewmodel

import androidx.lifecycle.LiveData

interface AddContactViewModel {
    val closeScreenLiveData : LiveData<Unit>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData:LiveData<String>
    val errorMessageLiveData :LiveData<String>

    fun closeScreen()
    fun addContact(firstName: String, lastName: String, phone:String)
}


