package uz.gita.mycontactb7.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.mycontactb7.domain.impl.ContactRepositoryImpl
import uz.gita.mycontactb7.presentation.viewmodel.impl.MainViewModelImpl
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModelImpl::class.java)) {
            return MainViewModelImpl(ContactRepositoryImpl.getInstance()) as T
        } else throw IllegalArgumentException("Required MainViewModelImpl")
    }
}


