package uz.gita.mycontactb7.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.presentation.viewmodel.AddContactViewModel
import uz.gita.mycontactb7.utils.MyEventBus
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import javax.inject.Inject

@HiltViewModel
class AddContactViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : ViewModel(), AddContactViewModel {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    override fun addContact(firstName: String, lastName: String, phone: String) {
        progressLiveData.value = true
        repository.addContact(firstName = firstName, lastName = lastName, phone = phone, successBlock = {
            progressLiveData.value = false
            if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
            else messageLiveData.value = "Save in local"

            closeScreenLiveData.value = Unit
            MyEventBus.reloadEvent?.invoke()
        }, errorBlock = {
            progressLiveData.value = false
            errorMessageLiveData.value = it
        })
    }

}