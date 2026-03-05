package ru.yarsu.keyforge.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.yarsu.keyforge.R
import ru.yarsu.keyforge.data.db.AppDatabase
import ru.yarsu.keyforge.data.repository.PasswordRepository
import ru.yarsu.keyforge.databinding.FragmentAddEditBinding

class AddEditFragment : Fragment() {

    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!

    private val entryId: Long by lazy {
        arguments?.getLong("entryId") ?: -1L
    }

    private val viewModel: AddEditViewModel by viewModels {
        val dao = AppDatabase.getDatabase(requireActivity().application).passwordDao()
        AddEditViewModelFactory(PasswordRepository(dao), entryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = if (viewModel.isEditMode)
            getString(ru.yarsu.keyforge.R.string.title_edit)
        else
            getString(ru.yarsu.keyforge.R.string.title_add)

        // Listen for generated password coming back from GeneratorFragment
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("generated_password")
            ?.observe(viewLifecycleOwner) { pwd ->
                if (pwd != null) {
                    viewModel.setGeneratedPassword(pwd)
                    findNavController().currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>("generated_password")
                }
            }

        binding.btnGenerate.setOnClickListener {
            findNavController().navigate(R.id.action_addEdit_to_generator)
        }

        binding.btnSave.setOnClickListener {
            viewModel.save()
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
