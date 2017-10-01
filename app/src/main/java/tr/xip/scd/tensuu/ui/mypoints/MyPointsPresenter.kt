package tr.xip.scd.tensuu.ui.mypoints

import io.realm.Sort
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.realm.model.PointFields
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter

class MyPointsPresenter : RealmPresenter<MyPointsView>() {

    fun init() {
        view?.setAdapter(PointsAdapter(
                realm.where(Point::class.java)
                        .equalTo(PointFields.FROM.EMAIL, Credentials.email)
                        .findAllSorted(PointFields.TIMESTAMP, Sort.DESCENDING)
        ))
    }
}