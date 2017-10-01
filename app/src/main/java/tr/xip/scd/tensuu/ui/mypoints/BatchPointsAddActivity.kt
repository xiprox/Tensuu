package tr.xip.scd.tensuu.ui.mypoints

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_batch_point_add.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.common.ext.toVisibility
import tr.xip.scd.tensuu.common.ext.watchForChange
import tr.xip.scd.tensuu.common.ext.watchForChangeDebounce
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class BatchPointsAddActivity : MvpActivity<BatchPointsAddView, BatchPointsAddPresenter>(), BatchPointsAddView {

    override fun createPresenter(): BatchPointsAddPresenter = BatchPointsAddPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_point_add)

        recycler.layoutManager = LinearLayoutManager(this)

        search.setOnItemClickListener { _, _, position, _ ->
            presenter.onNewStudentSelected(search.adapter.getItem(position) as Student)
            search.setText("")
        }
        search.threshold = 1

        reason.threshold = 1

        exit.setOnClickListener {
            finish()
        }

        done.setOnClickListener {
            presenter.onDoneClicked()
        }

        amount.watchForChangeDebounce {
            val text = it.toString()
            if (text.isNotBlank() && text != "-") {
                presenter.onMainAmountChanged(text.toInt())
            }
        }

        date.setOnClickListener {
            presenter.onDateClicked(this)
        }

        reason.watchForChangeDebounce {
            presenter.onReasonChanged(it.toString())
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

        presenter.init()

        intent?.extras?.let {
            presenter.addStudents(it.getSerializable(ARG_STUDENT_IDS) as List<String>)
        }
    }

    override fun setAdapter(value: BatchPointsStudentsAdapter) {
        recycler.adapter = value
    }

    override fun getAdapter(): BatchPointsStudentsAdapter? {
        return recycler.adapter as BatchPointsStudentsAdapter?
    }

    override fun setStudentsAdapter(value: StudentsAutoCompleteAdapter) {
        search.setAdapter(value)
    }

    override fun getStudentsAdapter(): StudentsAutoCompleteAdapter? {
        return search.adapter as StudentsAutoCompleteAdapter?
    }

    override fun setReasonsAdapter(value: ReasonsAutoCompleteAdapter) {
        reason.setAdapter(value)
    }

    override fun getReasonsAdapter(): ReasonsAutoCompleteAdapter? {
        return reason.getAdapter() as ReasonsAutoCompleteAdapter
    }

    @SuppressLint("SimpleDateFormat")
    override fun setDate(cal: Calendar) {
        date.setText(SimpleDateFormat("dd/MM/yyyy").format(cal.timeInMillis))
    }

    override fun setSearchClearVisible(value: Boolean) {
        clear.visibility = value.toVisibility()
    }

    override fun runOnUi(body: () -> Unit) {
        runOnUiThread {
            body()
        }
    }

    override fun die() {
        finish()
    }

    companion object {
        private val ARG_STUDENT_IDS = "students"

        fun getLaunchIntent(studentIds: List<String>): Intent {
            val intent = Intent(App.context, BatchPointsAddActivity::class.java)
            intent.putExtra(ARG_STUDENT_IDS, studentIds as Serializable)
            return intent
        }
    }
}
