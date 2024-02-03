package uz.gita.mycontactb7.utils

import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import timber.log.Timber

fun <T> T.myApply(block: T.() -> Unit) {
    block(this)
}

fun Fragment.showToast(message :String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun logger(message: String, tag: String= "TTT") {
    Timber.tag(tag).d(message)
}

fun EditText.myAddTextChangedListener(block: (String) -> Unit) {
    this.addTextChangedListener {editable ->
        editable?.let { block.invoke(it.toString()) }
    }
}

fun EditText.text() = this.text.toString()