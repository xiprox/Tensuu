package tr.xip.scd.tensuu.ui.admin

import tr.xip.scd.tensuu.realm.model.User
import tr.xip.scd.tensuu.realm.model.UserFields
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter

class AdminToolsPresenter : RealmPresenter<AdminToolsView>() {

    fun init() {
        val isAdmin = realm.where(User::class.java)
                .equalTo(UserFields.EMAIL, Credentials.email)
                .findFirst()
                ?.isAdmin ?: false

        if (!isAdmin) {
            view?.die()
        }
    }

    fun onAddClicked() {
        when (view?.getCurrentItem() ?: -1) {
            0 -> {
                view?.showAddUserDialog(realm)
            }
            1 -> {
                view?.showAddStudentDialog(realm)
            }
        }
    }
}