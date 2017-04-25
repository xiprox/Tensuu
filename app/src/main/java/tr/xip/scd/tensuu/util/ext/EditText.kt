package tr.xip.scd.tensuu.util.ext

import android.text.Editable
import android.widget.EditText
import tr.xip.scd.tensuu.util.TextWatcherAdapter
import java.util.*

/**
 * [EditText] extensions
 */

fun EditText.watchForChange(doOnChange: (s: Editable?) -> Unit) {
    addTextChangedListener(object : TextWatcherAdapter() {
        override fun afterTextChanged(s: Editable?) {
            doOnChange.invoke(s)
        }
    })
}

fun EditText.watchForChangeDebounce(wait: Long = 300, doOnChange: (s: Editable?) -> Unit) {
    addTextChangedListener(object : TextWatcherAdapter() {
        var timer = Timer()
        var lastText = ""

        override fun afterTextChanged(s: Editable?) {
            val q = s.toString()

            if (lastText == q) return
            lastText = q

            timer.cancel()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    doOnChange.invoke(s)
                }
            }, wait)
        }
    })
}