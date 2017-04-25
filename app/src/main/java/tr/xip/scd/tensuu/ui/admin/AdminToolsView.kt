package tr.xip.scd.tensuu.ui.admin

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.realm.Realm

interface AdminToolsView : MvpView {
    fun getCurrentItem(): Int

    fun showAddUserDialog(realm: Realm)
    fun showAddStudentDialog(realm: Realm)
    fun die()
}