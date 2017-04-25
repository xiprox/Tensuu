package tr.xip.scd.tensuu.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.admin.AdminToolsActivity
import tr.xip.scd.tensuu.ui.login.LoginActivity
import tr.xip.scd.tensuu.ui.mypoints.AddPointDialog

class MainActivity : MvpViewStateActivity<MainView, MainPresenter, MainViewState>(), MainView {
    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun createViewState(): MainViewState = MainViewState()

    override fun onNewViewStateInstance() {
        /* do nothing */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        addPoint.setOnClickListener {
            presenter.onAddPointClicked()
        }

        navigation.setOnNavigationItemSelectedListener({
            presenter.onNavigationItemSelectedListener(it)
        })

        presenter.init(savedInstanceState != null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        presenter.onCreateOptionsMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onOptionsMenuItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        viewState.isFabShown = addPoint.isShown
        super.onSaveInstanceState(outState)
    }

    override fun setSelectedContentId(id: Int) {
        navigation.selectedItemId = id
    }

    override fun setContentFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment, tag)
                .commit()
    }

    override fun getFragmentByTag(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    override fun showFab(show: Boolean) {
        var s = show
        if (!Credentials.canModify) s = false
        if (s) {
            addPoint.show()
        } else {
            addPoint.hide()
        }
    }

    override fun showAddPointDialog(realm: Realm) {
        AddPointDialog.new(realm, this)?.show()
    }

    override fun showSignOutDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.title_sign_out_question)
                .setMessage(R.string.info_are_you_sure_sign_out)
                .setPositiveButton(R.string.yes, { dialog, which ->
                    presenter.onSignedOut()
                })
                .setNegativeButton(R.string.no, null)
                .create()
                .show()
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun startAdminToolsActivity() {
        if (Credentials.isAdmin) startActivity(Intent(this, AdminToolsActivity::class.java))
    }

    override fun die() {
        finish()
    }

    companion object {
        internal val defaultNavPageId = R.id.navigation_my_points
    }
}
