package ru.yarsu.keyforge.ui.generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.yarsu.keyforge.R
import ru.yarsu.keyforge.databinding.FragmentGeneratorBinding

class GeneratorFragment : Fragment() {

    private var _binding: FragmentGeneratorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GeneratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SeekBar min is 4, max is 32; XML max=28 (4+28=32), progress=12 (4+12=16)
        binding.seekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val length = progress + 4
                viewModel.length.value = length
                if (fromUser) viewModel.generate()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.switchLowercase.setOnCheckedChangeListener { _, _ -> viewModel.generate() }
        binding.switchUppercase.setOnCheckedChangeListener { _, _ -> viewModel.generate() }
        binding.switchDigits.setOnCheckedChangeListener { _, _ -> viewModel.generate() }
        binding.switchSpecial.setOnCheckedChangeListener { _, _ -> viewModel.generate() }

        binding.btnRegenerate.setOnClickListener {
            val anySelected = (viewModel.useLowercase.value == true)
                    || (viewModel.useUppercase.value == true)
                    || (viewModel.useDigits.value == true)
                    || (viewModel.useSpecial.value == true)

            if (!anySelected) {
                Snackbar.make(binding.root, R.string.error_no_charset, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.generate()
        }

        binding.btnUsePassword.setOnClickListener {
            val pwd = viewModel.generatedPassword.value ?: ""
            if (pwd.isEmpty()) {
                Snackbar.make(binding.root, R.string.error_no_charset, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("generated_password", pwd)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
