package uz.gita.mycontactb7.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.mycontactb7.domain.impl.ContactRepositoryImpl
import uz.gita.mycontactb7.presentation.viewmodel.AddContactViewModel
import uz.gita.mycontactb7.presentation.viewmodel.impl.AddContactViewModelImpl

@Suppress("UNCHECKED_CAST")
class AddContactViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddContactViewModelImpl::class.java)) {
            return AddContactViewModelImpl(ContactRepositoryImpl.getInstance()) as T
        } else throw IllegalArgumentException("Required MainViewModelImpl")
    }
}


