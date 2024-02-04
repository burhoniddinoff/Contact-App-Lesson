package uz.gita.mycontactb7.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactb7.R
import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.databinding.ScreenMainBinding
import uz.gita.mycontactb7.presentation.adapter.ContactAdapter
import uz.gita.mycontactb7.presentation.viewmodel.MainViewModel
import uz.gita.mycontactb7.presentation.viewmodel.impl.MainViewModelImpl
import uz.gita.mycontactb7.utils.logger
import uz.gita.mycontactb7.utils.myApply
import uz.gita.mycontactb7.utils.showToast

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private val binding: ScreenMainBinding by viewBinding(ScreenMainBinding::bind)
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val adapter by lazy { ContactAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadContacts()
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.notConnectionLiveData.observe(this, notConnectionObserver)
        viewModel.openAddContactScreenLiveData.observe(this, openAddContactScreenObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext())

        logger("main")
        buttonAdd.setOnClickListener { viewModel.openAddContactScreen() }

        buttonRefresh.setOnClickListener {
            logger("click")
            viewModel.loadContacts()
        }
        refreshLayout.setOnRefreshListener { viewModel.loadContacts() }

        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.emptyStateLiveData.observe(viewLifecycleOwner, emptyStateObserver)
        viewModel.contactsLiveData.observe(viewLifecycleOwner, contactsObserver)
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

    private val notConnectionObserver = Observer<Unit> {
        showToast("Not connection")
    }

    private val progressObserver = Observer<Boolean> {
        binding.refreshLayout.isRefreshing = it
        binding.containerEmpty.visibility = View.GONE
    }

    private val emptyStateObserver = Observer<Unit> {
        binding.containerEmpty.visibility = View.VISIBLE
    }

    private val contactsObserver = Observer<List<ContactUIData>> {
        binding.containerEmpty.visibility = View.GONE
        adapter.submitList(it)
    }

    private val openAddContactScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_mainScreen_to_addContactScreen)
    }
}