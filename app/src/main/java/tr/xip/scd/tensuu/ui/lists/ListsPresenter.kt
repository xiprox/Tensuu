package tr.xip.scd.tensuu.ui.lists

import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.data.model.*
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class ListsPresenter : RealmPresenter<ListsView>() {

    fun init() {
        view?.setAdapter(
                ListsAdapter(App.context, realm,
                        realm.where(StudentList::class.java)
                                .equalTo(StudentListFields.OWNER.EMAIL, Credentials.email)
                                .or()
                                .equalTo(StudentListFields.PUBLIC, true)
                                .findAllSorted(StudentListFields.NAME)
                )
        )
    }
}