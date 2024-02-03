package uz.gita.mycontactb7.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mycontactb7.R
import uz.gita.mycontactb7.databinding.ScreenContactAddBinding
import uz.gita.mycontactb7.presentation.viewmodel.AddContactViewModel
import uz.gita.mycontactb7.presentation.viewmodel.factory.AddContactViewModelFactory
import uz.gita.mycontactb7.presentation.viewmodel.impl.AddContactViewModelImpl
import uz.gita.mycontactb7.utils.myAddTextChangedListener
import uz.gita.mycontactb7.utils.myApply
import uz.gita.mycontactb7.utils.showToast
import uz.gita.mycontactb7.utils.text

class AddContactScreen : Fragment(R.layout.screen_contact_add){
    private val binding by viewBinding(ScreenContactAddBinding::bind)
    private val viewModel : AddContactViewModel by viewModels<AddContactViewModelImpl> { AddContactViewModelFactory() }
    private var prepareFirstName = false
    private var prepareLastName = false
    private var preparePhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
        viewModel.errorMessageLiveData.observe(this,errorMessageObserver)
        viewModel.messageLiveData.observe(this, messageObserver)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        binding.inputFirstName.myAddTextChangedListener {
            prepareFirstName = it.length > 3
            check()
        }
        binding.inputLastName.myAddTextChangedListener {
            prepareLastName = it.length > 3
            check()
        }
        binding.inputPhone.myAddTextChangedListener {
            preparePhone = it.startsWith("+998") && it.length == 13
            check()
        }

        binding.buttonBack.setOnClickListener { viewModel.closeScreen() }
        binding.buttonAdd.setOnClickListener { viewModel.addContact(inputFirstName.text(), inputLastName.text(), inputPhone.text()) }
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private fun check() {
        binding.buttonAdd.isEnabled = prepareFirstName && prepareLastName && preparePhone
    }
    private val closeScreenObserver = Observer<Unit> { findNavController().navigateUp() }
    private val errorMessageObserver = Observer<String> { showToast(it) }
    private val messageObserver = Observer<String> { showToast(it)}
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.buttonAdd.visibility = View.GONE
            binding.frameLoading.visibility = View.VISIBLE
            binding.progress.show()
        } else  {
            binding.buttonAdd.visibility = View.VISIBLE
            binding.frameLoading.visibility = View.GONE
            binding.progress.hide()
        }
    }
}





