package ru.yarsu.keyforge.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.yarsu.keyforge.data.model.PasswordEntry
import ru.yarsu.keyforge.databinding.ItemPasswordBinding

class PasswordAdapter(
    private val onItemClick: (PasswordEntry) -> Unit,
    private val onCopyClick: (PasswordEntry) -> Unit
) : ListAdapter<PasswordEntry, PasswordAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<PasswordEntry>() {
        override fun areItemsTheSame(oldItem: PasswordEntry, newItem: PasswordEntry) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PasswordEntry, newItem: PasswordEntry) =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemPasswordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: PasswordEntry) {
            binding.entry = entry
            binding.root.setOnClickListener { onItemClick(entry) }
            binding.btnCopy.setOnClickListener { onCopyClick(entry) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPasswordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
