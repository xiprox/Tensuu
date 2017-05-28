package tr.xip.scd.tensuu.ui.students

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_students.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.ui.student.StudentActivity
import tr.xip.scd.tensuu.util.ext.*

open class StudentsFragment : MvpFragment<StudentsView, StudentsPresenter>(), StudentsView, AnimateableFragment {

    override fun createPresenter(): StudentsPresenter = StudentsPresenter()

    override fun onResume() {
        super.onResume()
        recycler?.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(context)

        searchContainer.doOnLayout {
            animateEnter()
        }

        search.watchForChange {
            presenter.onSearchTextChangedInstant(it)
        }

        search.watchForChangeDebounce {
            presenter.onSearchTextChanged(it)
        }

        clear.setOnClickListener {
            search.setText("")
        }
    }

    override fun setSearchExitVisible(value: Boolean) {
        activity?.runOnUiThread {
            clear?.visibility = value.toVisibility()
        }
    }

    override fun setAdapter(value: StudentsAdapter) {
        recycler?.adapter = value
        animateRecycler()
    }

    override fun getAdapter(): StudentsAdapter? {
        return recycler?.adapter as StudentsAdapter?
    }

    override fun onStudentClicked(view: View) {
        val position = recycler.getChildAdapterPosition(view)
        val item = getAdapter()?.data?.get(position)
        if (item != null) presenter.onStudentClicked(item)
    }

    override fun startStudentActivity(student: Student, rangeStart: Long?, rangeEnd: Long?) {
        if (rangeStart != null && rangeEnd != null) {
            StudentActivity.start(context, rangeStart, rangeEnd, student.ssid)
        } else {
            StudentActivity.start(context, student.ssid)
        }
    }

    override fun runOnUiThread(body: () -> Unit) {
        activity?.runOnUiThread {
            body.invoke()
        }
    }

    override fun animateEnter() {
        animate(true, getEnterDuration())
    }

    override fun animateExit() {
        animate(false, getExitDuration())
    }

    private fun animate(enter: Boolean, duration: Long) {
        if (searchContainer == null) return

        val height = searchContainer.measuredHeight.toFloat()

        if (enter) {
            searchContainer.translationY = search.translationY + height
            searchDivider.translationY = searchDivider.translationY + height
        }

        searchContainer.animate()
                .translationYBy(if (enter) -height else height)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(duration)
                .start()
        searchDivider.animate()
                .translationYBy(if (enter) -height else height)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(duration)
                .start()

        for (i in 0..(searchContainer.childCount - 1)) {
            val child = searchContainer.getChildAt(i)
            if (child.id == R.id.searchDivider) continue

            val translationYBy = child.height * 0.25f

            if (enter) {
                child.alpha = 0f
                child.translationY = child.translationY + translationYBy
            }

            child.animate()
                    .translationYBy(if (enter) -translationYBy else translationYBy)
                    .alpha(if (enter) 1f else 0f)
                    .setStartDelay(if (enter) 100 else 0)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(if (enter) duration + 100 else duration - 50)
                    .start()
        }
    }

    override fun getEnterDuration(): Long = 200

    override fun getExitDuration(): Long = 100

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