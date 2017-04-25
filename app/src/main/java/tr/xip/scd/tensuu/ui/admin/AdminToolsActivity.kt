package tr.xip.scd.tensuu.ui.admin

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_admin_tools.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.admin.student.AddStudentDialog
import tr.xip.scd.tensuu.ui.admin.user.AddUserDialog

class AdminToolsActivity : MvpActivity<AdminToolsView, AdminToolsPresenter>(), AdminToolsView {
    override fun createPresenter(): AdminToolsPresenter = AdminToolsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tools)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pager.adapter = AdminToolsPagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)

        add.setOnClickListener {
            presenter.onAddClicked()
        }

        presenter.init()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun getCurrentItem(): Int {
        return pager?.currentItem ?: -1
    }

    override fun showAddUserDialog(realm: Realm) {
        AddUserDialog.new(realm, this)?.show()
    }

    override fun showAddStudentDialog(realm: Realm) {
        AddStudentDialog.new(realm, this)?.show()
    }

    override fun die() {
        finish()
    }
}