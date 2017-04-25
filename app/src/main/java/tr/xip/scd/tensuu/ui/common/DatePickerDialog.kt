package tr.xip.scd.tensuu.ui.common

import android.app.DatePickerDialog
import android.content.Context

object DatePickerDialog {

    fun new(
            context: Context,
            year: Int,
            month: Int,
            day: Int,
            listener: (y: Int, m: Int, d: Int) -> Unit
    ) {
        DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    listener.invoke(year, month, day)
                },
                year,
                month,
                day
        ).show()
    }
}