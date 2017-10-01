package tr.xip.scd.tensuu.ui.lists

import android.content.Context
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.realm.model.StudentList
import tr.xip.scd.tensuu.realm.model.StudentListFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class ListsPresenter : RealmPresenter<ListsView>() {

    fun init(context: Context) {
        view?.setAdapter(
                ListsAdapter(context, realm,
                        realm.where(StudentList::class.java)
                                .equalTo(StudentListFields.OWNER.EMAIL, Credentials.email)
                                .or()
                                .equalTo(StudentListFields.PUBLIC, true)
                                .findAllSorted(StudentListFields.NAME)
                )
        )
    }
}