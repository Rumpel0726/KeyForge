package ru.yarsu.keyforge.ui.generator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.yarsu.keyforge.util.PasswordGenerator

class GeneratorViewModel : ViewModel() {

    val length = MutableLiveData(16)
    val useLowercase = MutableLiveData(true)
    val useUppercase = MutableLiveData(true)
    val useDigits = MutableLiveData(true)
    val useSpecial = MutableLiveData(false)

    val generatedPassword = MutableLiveData("")

    fun generate() {
        val lower = useLowercase.value ?: true
        val upper = useUppercase.value ?: true
        val digits = useDigits.value ?: true
        val special = useSpecial.value ?: false

        if (!lower && !upper && !digits && !special) {
            generatedPassword.value = ""
            return
        }

        generatedPassword.value = PasswordGenerator.generate(
            length = length.value ?: 16,
            useLowercase = lower,
            useUppercase = upper,
            useDigits = digits,
            useSpecial = special
        )
    }

    init {
        generate()
    }
}
