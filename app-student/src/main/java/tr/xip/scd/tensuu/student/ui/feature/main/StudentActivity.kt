package tr.xip.scd.tensuu.student.ui.feature.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_student.*
import tr.xip.scd.tensuu.student.R
import tr.xip.scd.tensuu.common.ext.setDisplayedChildSafe
import tr.xip.scd.tensuu.common.ui.view.RecyclerViewAdapterDataObserver
import tr.xip.scd.tensuu.student.ui.feature.login.LoginActivity

class StudentActivity : MvpActivity<StudentView, StudentPresenter>(), StudentView {
    private val dataObserver = RecyclerViewAdapterDataObserver {
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

        presenter.init()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
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

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun notifyDateChanged() {
        dataObserver.onChanged()
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun die() {
        finish()
    }

    companion object {
        internal val FLIPPER_CONTENT = 0
        internal val FLIPPER_NO_POINTS = 1
    }
}