package tr.xip.scd.tensuu.ui.admin.user

import tr.xip.scd.tensuu.data.model.User
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class UsersPresenter : RealmPresenter<UsersView>() {

    fun init() {
        view?.setAdapter(
                UsersAdapter(realm.where(User::class.java).findAll(), {
                    view?.onUserClicked(it)
                })
        )
    }

    fun onUserClicked(user: User) {
        view?.startUserEditActivity(user)
    }
}