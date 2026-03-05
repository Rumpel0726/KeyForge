package ru.yarsu.keyforge.ui.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.yarsu.keyforge.R
import ru.yarsu.keyforge.databinding.FragmentPasswordListBinding

class PasswordListFragment : Fragment() {

    private var _binding: FragmentPasswordListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PasswordListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PasswordAdapter(
            onItemClick = { entry ->
                findNavController().navigate(
                    R.id.action_list_to_detail,
                    bundleOf("entryId" to entry.id)
                )
            },
            onCopyClick = { entry ->
                copyToClipboard(entry.password)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allEntries.observe(viewLifecycleOwner) { entries ->
            adapter.submitList(entries)
            binding.emptyView.visibility = if (entries.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                R.id.action_list_to_addEdit,
                bundleOf("entryId" to -1L)
            )
        }
    }

    private fun copyToClipboard(password: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.label_password), password)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
