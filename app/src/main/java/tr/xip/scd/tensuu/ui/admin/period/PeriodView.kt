package tr.xip.scd.tensuu.ui.admin.period

import com.hannesdorfmann.mosby3.mvp.MvpView
import java.util.*

interface PeriodView : MvpView {
    fun setStartDate(value: Long)
    fun setEndDate(value: Long)

    fun showStartDateDialog(date: Calendar, listener: (newDate: Calendar) -> Unit)
}