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
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.common.ext.toPx
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.ui.student.StudentActivity

class WeeklyFragment : MvpFragment<WeeklyView, WeeklyPresenter>(), WeeklyView {
    lateinit private var controlView: View

    override fun createPresenter(): WeeklyPresenter = WeeklyPresenter()

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
        controlView = (parentFragment as ReportsFragment).weekControls
        controlView.nextWeek.setOnClickListener {
            presenter.onNextWeekClicked()
        }
        controlView.currentWeek.setOnClickListener {
            presenter.onCurrentWeekClicked()
        }
        controlView.previousWeek.setOnClickListener {
            presenter.onPreviousWeekClicked()
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

    override fun setCurrentWeekText(value: String) {
        controlView.currentWeek.text = value
    }

    override fun onStudentClicked(view: View) {
        val position = recycler.getChildAdapterPosition(view)
        val item = getAdapter()?.data?.get(position)
        presenter.onStudentClicked(item)
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
