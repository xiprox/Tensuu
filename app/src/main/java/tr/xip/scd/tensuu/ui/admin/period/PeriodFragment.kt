package tr.xip.scd.tensuu.ui.admin.period

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_period.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.DatePickerDialog
import tr.xip.scd.tensuu.util.ext.stripToDate
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class PeriodFragment : MvpFragment<PeriodView, PeriodPresenter>(), PeriodView {
    var startDate: Calendar = GregorianCalendar()
    var endDate: Calendar = GregorianCalendar()

    override fun createPresenter(): PeriodPresenter = PeriodPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_period, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start.setOnClickListener {
            presenter.onStartDateClicked()
        }
        end.setOnClickListener {
            presenter.onEndDateClicked()
        }

        presenter.init()
    }

    override fun setStartDate(value: Long) {
        startDate.timeInMillis = value.stripToDate()
        start.setText(SimpleDateFormat("dd/MM/yyyy").format(value))
    }

    override fun setEndDate(value: Long) {
        endDate.timeInMillis = value.stripToDate()
        end.setText(SimpleDateFormat("dd/MM/yyyy").format(value))
    }

    override fun showStartDateDialog(date: Calendar, listener: (newDate: Calendar) -> Unit) {
        DatePickerDialog.new(activity, date.get(YEAR), date.get(MONTH), date.get(DAY_OF_MONTH),
                { y, m, d ->
                    date.set(y, m, d)
                    listener.invoke(date)
                }
        )
    }

    companion object {
        fun new(): PeriodFragment {
            return PeriodFragment()
        }
    }
}