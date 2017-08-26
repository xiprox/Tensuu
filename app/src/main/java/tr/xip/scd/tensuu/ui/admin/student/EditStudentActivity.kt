package tr.xip.scd.tensuu.ui.admin.student

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_edit_student.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.util.ext.watchForChange

class EditStudentActivity : MvpActivity<EditStudentView, EditStudentPresenter>(), EditStudentView {
    override fun createPresenter(): EditStudentPresenter = EditStudentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.loadWith(intent.extras)

        ssid.watchForChange {
            runOnUiThread {
                presenter.onSsidChanged(it)
            }
        }

        firstName.watchForChange {
            runOnUiThread {
                presenter.onFirstNameChanged(it)
            }
        }

        lastName.watchForChange {
            runOnUiThread {
                presenter.onLastNameChanged(it)
            }
        }

        grade.watchForChange {
            runOnUiThread {
                presenter.onGradeChanged(it)
            }
        }

        floor.watchForChange {
            runOnUiThread {
                presenter.onFloorChanged(it)
            }
        }
    }

    override fun setSsid(value: String?) {
        ssid.setText(value)
    }

    override fun setFirstName(value: String?) {
        firstName.setText(value)
    }

    override fun setLastName(value: String?) {
        lastName.setText(value)
    }

    override fun setGrade(value: String?) {
        grade.setText(value)
    }

    override fun setFloor(value: Int?) {
        floor.setText("${value ?: ""}")
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) presenter.onOptionsMenuItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun die() {
        finish()
    }

    companion object {
        val ARG_STUDENT_SSID = "student_ssid"
    }
}