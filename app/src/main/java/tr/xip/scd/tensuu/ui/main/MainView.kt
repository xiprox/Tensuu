package tr.xip.scd.tensuu.ui.main

import android.support.v4.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.realm.Realm

interface MainView : MvpView {
    fun setSelectedContentId(id: Int)
    fun setContentFragment(fragment: Fragment, tag: String)
    fun getFragmentByTag(tag: String): Fragment?

    fun showFab(show: Boolean = true)

    fun showAddPointDialog(realm: Realm)
    fun showSignOutDialog()
    fun startLoginActivity()
    fun startAdminToolsActivity()
    fun die()
}