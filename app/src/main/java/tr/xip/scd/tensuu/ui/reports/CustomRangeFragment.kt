package tr.xip.scd.tensuu.ui.reports

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_reports.*
import kotlinx.android.synthetic.main.fragment_reports.view.*
import kotlinx.android.synthetic.main.fragment_reports_page.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.ui.common.DatePickerDialog
import tr.xip.scd.tensuu.ui.student.StudentActivity
import tr.xip.scd.tensuu.util.ext.*

class CustomRangeFragment : MvpFragment<CustomRangeView, CustomRangePresenter>(), CustomRangeView {
    private var controlView: View? = null

    override fun createPresenter(): CustomRangePresenter = CustomRangePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reports_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (parentFragment !is ReportsFragment) {
            throw IllegalStateException("Parent of WeeklyFragment must be ReportsFragment")
        }
        controlView = (parentFragment as ReportsFragment).rangeControls
        controlView?.rangeStartSelector?.setOnClickListener {
            presenter.onStartDateClicked()
        }
        controlView?.rangeEndSelector?.setOnClickListener {
            presenter.onEndDateClicked()
        }

        recycler.layoutManager = LinearLayoutManager(context)

        presenter.init()
    }

    override fun setAdapter(value: ReportsAdapter) {
        recycler.adapter = value
        animateRecycler()
    }

    override fun getAdapter(): ReportsAdapter? {
        return recycler.adapter as ReportsAdapter?
    }

    override fun setStartDateText(value: String) {
        controlView?.rangeStartSelector?.text = value
    }

    override fun setEndDateText(value: String) {
        controlView?.rangeEndSelector?.text = value
    }

    override fun onStudentClicked(view: View) {
        val position = recycler.getChildAdapterPosition(view)
        val item = getAdapter()?.data?.get(position)
        presenter.onStudentClicked(item)
    }

    override fun showDatePickedDialog(y: Int, m: Int, d: Int, listener: (y: Int, m: Int, d: Int) -> Unit) {
        DatePickerDialog.new(context, y, m, d, { y, m, d ->
            listener.invoke(y, m, d)
        })
    }

    override fun startStudentActivity(student: Student, start: Long, end: Long) {
        StudentActivity.start(context, start, end, student.ssid)
    }

    private fun animateRecycler() {
        val amount = 24.toPx(context).toFloat()

        recycler.alpha = 0f
        recycler.translationY = recycler.translationY + amount

        recycler.animate()
                .alpha(1f)
                .translationYBy(-amount)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(200)
                .start()
    }
}
