package uz.gita.mycontactb7.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.presentation.viewmodel.MainViewModel
import uz.gita.mycontactb7.utils.MyEventBus

class MainViewModelImpl(private val repository: ContactRepository) : ViewModel(), MainViewModel {
    override val progressLiveData = MutableLiveData<Boolean>()
    override val contactsLiveData = MutableLiveData<List<ContactUIData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val notConnectionLiveData = MutableLiveData<Unit>()
    override val emptyStateLiveData = MutableLiveData<Unit>()
    override val openAddContactScreenLiveData = MutableLiveData<Unit>()

    init {
        MyEventBus.reloadEvent = {
            loadContacts()
        }
    }

    override fun loadContacts() {
        //if (!NetworkStatusValidator.hasNetwork) notConnectionLiveData.value = Unit
        progressLiveData.value = true
        repository.getAllContact(
            successBlock = {
                progressLiveData.value = false
                if (it.isEmpty()) emptyStateLiveData.value = Unit
                else contactsLiveData.value = it
            },
            errorBlock = {
                progressLiveData.value = false
                errorMessageLiveData.value = it
            }
        )
    }

    override fun openAddContactScreen() {
        openAddContactScreenLiveData.value = Unit
    }
}