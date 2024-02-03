package uz.gita.mycontactb7.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.data.model.StatusEnum
import uz.gita.mycontactb7.databinding.ItemContactBinding

class ContactAdapter : ListAdapter<ContactUIData, ContactAdapter.ContactViewHolder>(ContactDiffUtil) {

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem == newItem
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind() {
            getItem(adapterPosition).apply {
                binding.textName.text = "$lastName $firstName"
                binding.textNumber.text = phone

                when(this.status) {
                    StatusEnum.SYNC -> binding.textStatus.visibility = View.GONE
                    else  -> {
                        binding.textStatus.visibility = View.VISIBLE
                        binding.textStatus.text = this.status.name
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ContactViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind()
    }


}