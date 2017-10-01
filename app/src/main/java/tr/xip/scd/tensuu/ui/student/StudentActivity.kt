package tr.xip.scd.tensuu.ui.student

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_student.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter
import tr.xip.scd.tensuu.common.ui.view.RecyclerViewAdapterDataObserver
import tr.xip.scd.tensuu.common.ext.setDisplayedChildSafe
import tr.xip.scd.tensuu.common.ext.toVisibility
import java.text.SimpleDateFormat

class StudentActivity : MvpActivity<StudentView, StudentPresenter>(), StudentView {
    val dataObserver = RecyclerViewAdapterDataObserver {
        presenter.onDataChanged()
    }

    override fun createPresenter(): StudentPresenter = StudentPresenter()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)

        restrictedRangeClose.setOnClickListener {
            presenter.onRestrictedRangeClosed()
        }

        presenter.loadWith(intent.extras)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.student, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun setTitle(value: String) {
        title = value
    }

    override fun setSsid(value: String?) {
        if (value != null) {
            ssid.text = value
        } else {
            ssid.visibility = GONE
        }
    }

    override fun setGrade(value: String?) {
        if (value != null) {
            grade.text = value
        } else {
            grade.visibility = GONE
        }
    }

    override fun setFloor(value: Int?) {
        if (value != null) {
            floor.text = "$value"
        } else {
            floor.visibility = GONE
        }
    }

    override fun setPoints(value: Int) {
        this.points.text = "$value"
    }

    @SuppressLint("SimpleDateFormat")
    override fun setRestrictedRangeText(start: Long, end: Long) {
        val format = SimpleDateFormat("MMM d ''yy")
        restrictedRangeDate.text = getString(
                R.string.info_showing_x_to_y,
                format.format(start),
                format.format(end)
        )
    }

    override fun setFlipperChild(position: Int) {
        flipper.setDisplayedChildSafe(position)
    }

    override fun setAdapter(value: PointsAdapter) {
        recycler.adapter = value
        recycler.adapter.registerAdapterDataObserver(dataObserver)
    }

    override fun getAdapter(): PointsAdapter {
        return recycler.adapter as PointsAdapter
    }

    override fun showRestrictedRange(value: Boolean) {
        restrictedRange.visibility = value.toVisibility()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun notifyDateChanged() {
        dataObserver.onChanged()
    }

    override fun die() {
        finish()
    }

    companion object {
        internal val ARG_STUDENT_SSID = "student_ssid"
        internal val ARG_RANGE_START = "range_start"
        internal val ARG_RANGE_END = "range_end"
        internal val FLIPPER_CONTENT = 0
        internal val FLIPPER_NO_POINTS = 1

        fun start(context: Context, ssid: String?) {
            start(context, 0, 0, ssid)
        }

        fun start(context: Context, rangeStart: Long, rangeEnd: Long, ssid: String?) {
            val intent = Intent(context, StudentActivity::class.java)
            intent.putExtra(ARG_STUDENT_SSID, ssid)
            intent.putExtra(ARG_RANGE_START, rangeStart)
            intent.putExtra(ARG_RANGE_END, rangeEnd)
            context.startActivity(intent)
        }
    }
}