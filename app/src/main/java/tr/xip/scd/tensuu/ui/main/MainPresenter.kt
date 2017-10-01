package tr.xip.scd.tensuu.ui.main

import android.os.Handler
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.SyncUser
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.User
import tr.xip.scd.tensuu.realm.model.UserFields
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.common.ui.mvp.SafeViewMvpPresenter
import tr.xip.scd.tensuu.ui.feed.FeedFragment
import tr.xip.scd.tensuu.ui.lists.ListsFragment
import tr.xip.scd.tensuu.ui.mypoints.MyPointsFragment
import tr.xip.scd.tensuu.ui.reports.ReportsFragment
import tr.xip.scd.tensuu.ui.students.StudentsFragment
import tr.xip.scd.tensuu.realm.util.RealmUtils

class MainPresenter : SafeViewMvpPresenter<MainView>() {
    private var realm: Realm? = null

    private var lastTag = ""

    fun init(fromSavedState: Boolean) {
        if (SyncUser.currentUser() != null) {
            realm = RealmUtils.syncedRealm()

            val user = realm?.where(User::class.java)
                    ?.equalTo(UserFields.EMAIL, Credentials.email)
                    ?.findFirst()

            if (user != null) {
                Credentials.loadFrom(user)
                if (!fromSavedState) {
                    view?.setSelectedContentId(MainActivity.defaultNavPageId)
                }
            } else {
                goToLogin()
            }
        } else {
            goToLogin()
        }
    }

    fun onAddClicked() {
        realm?.let {
            when (view?.getCurrentNavigationId()) {
                R.id.navigation_my_points -> view?.showAddPointDialog(it)
                R.id.navigation_lists -> view?.showAddListDialog(it)
                else -> return
            }
        }
    }

    fun onCreateOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.navigation_admin_tools)?.isVisible = Credentials.isAdmin
    }

    fun onOptionsMenuItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.navigation_admin_tools -> {
                view?.startAdminToolsActivity()
            }
            R.id.navigation_sign_out -> {
                view?.showSignOutDialog()
            }
        }
    }

    fun onNavigationItemSelectedListener(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var shouldShowFab = false

        when (item.itemId) {
            R.id.navigation_my_points -> {
                fragment = MyPointsFragment()
                shouldShowFab = true
            }
            R.id.navigation_feed -> {
                fragment = FeedFragment()
            }
            R.id.navigation_students -> {
                fragment = StudentsFragment()
            }
            R.id.navigation_lists -> {
                fragment = ListsFragment()
                shouldShowFab = true
            }
            R.id.navigation_reports -> {
                fragment = ReportsFragment()
            }
        }

        if (fragment != null) {
            val switch = Runnable {
                lastTag = item.itemId.toString()
                view?.setContentFragment(fragment!!, lastTag)
                view?.showFab(shouldShowFab)
            }

            // Find the previous fragment
            val frag = view?.getFragmentByTag(lastTag)
            if (frag != null && frag is AnimateableFragment) {
                // Play the animation if supported
                frag.animateExit()
                // Wait for it to end
                Handler().postDelayed(switch, frag.getExitDuration())
            } else {
                switch.run()
            }

            return true
        } else {
            return false
        }
    }

    fun onSignedOut() {
        Credentials.logout()
        view?.die()
    }

    fun goToLogin() {
        view?.startLoginActivity()
        view?.die()
    }
}