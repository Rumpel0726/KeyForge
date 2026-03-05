package ru.yarsu.keyforge.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.yarsu.keyforge.R
import ru.yarsu.keyforge.data.db.AppDatabase
import ru.yarsu.keyforge.data.repository.PasswordRepository
import ru.yarsu.keyforge.databinding.FragmentPasswordDetailBinding

class PasswordDetailFragment : Fragment() {

    private var _binding: FragmentPasswordDetailBinding? = null
    private val binding get() = _binding!!

    private val entryId: Long by lazy {
        arguments?.getLong("entryId") ?: -1L
    }

    private val viewModel: PasswordDetailViewModel by viewModels {
        val dao = AppDatabase.getDatabase(requireActivity().application).passwordDao()
        PasswordDetailViewModelFactory(PasswordRepository(dao), entryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.entry.observe(viewLifecycleOwner) { entry ->
            if (entry == null) return@observe
            // Update password display based on visibility
            updatePasswordDisplay(entry.password, viewModel.passwordVisible.value ?: false)
        }

        viewModel.passwordVisible.observe(viewLifecycleOwner) { visible ->
            val password = viewModel.entry.value?.password ?: return@observe
            updatePasswordDisplay(password, visible)
            binding.btnTogglePassword.setImageResource(
                if (visible) R.drawable.ic_eye_off else R.drawable.ic_eye
            )
        }

        binding.btnTogglePassword.setOnClickListener {
            viewModel.togglePasswordVisibility()
        }

        binding.btnCopyPassword.setOnClickListener {
            val password = viewModel.entry.value?.password ?: return@setOnClickListener
            copyToClipboard(password)
        }

        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_detail_to_addEdit,
                bundleOf("entryId" to entryId)
            )
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewModel.delete()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().popBackStack()
            }
        }

        binding.tvUrl.setOnClickListener {
            val url = viewModel.entry.value?.url ?: return@setOnClickListener
            if (url.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    private fun updatePasswordDisplay(password: String, visible: Boolean) {
        binding.tvPassword.text = if (visible) {
            password
        } else {
            "•".repeat(password.length.coerceAtMost(20))
        }
        binding.tvPassword.transformationMethod = null
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
