package com.nammamela.app.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nammamela.app.NammaMelaApplication

inline fun <reified T : ViewModel> Fragment.appViewModels(): Lazy<T> {
    val application = requireActivity().application as NammaMelaApplication
    val factory = ViewModelFactory(application.repository)
    return lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, factory)[T::class.java]
    }
}
