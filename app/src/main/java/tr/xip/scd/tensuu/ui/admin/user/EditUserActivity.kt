package tr.xip.scd.tensuu.ui.admin.user

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_user.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.util.ext.watchForChange

class EditUserActivity : MvpActivity<EditUserView, EditUserPresenter>(), EditUserView {
    override fun createPresenter(): EditUserPresenter = EditUserPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.loadWith(intent.extras)

        password.watchForChange {
            runOnUiThread {
                presenter.onPasswordChanged(it)
            }
        }

        name.watchForChange {
            runOnUiThread {
                presenter.onNameChanged(it)
            }
        }

        adminSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            runOnUiThread {
                presenter.onAdminChanged(isChecked)
            }
        }

        modifySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            runOnUiThread {
                presenter.onCanModifyChanged(isChecked)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) presenter.onOptionsMenuItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun setEmail(value: String?) {
        email?.setText(value)
    }

    override fun setPassword(value: String?) {
        password?.setText(value)
    }

    override fun setName(value: String?) {
        name?.setText(value)
    }

    override fun setAdmin(value: Boolean) {
        adminSwitch?.isChecked = value
    }

    override fun setCanModify(value: Boolean) {
        modifySwitch.isChecked = value
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun die() {
        finish()
    }

    companion object {
        internal val ARG_USER_EMAIL = "user_email"
    }
}